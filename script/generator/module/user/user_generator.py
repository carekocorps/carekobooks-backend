import urllib.parse
from common.factory.user_request_factory import IUserRequestFactory
from common.manager.verification_manager import IVerificationManager
from common.manager.mail_manager import IMailManager
from config import ApiConfig
import requests
import logging
import urllib
import json

class UserGenerator:
    def __init__(self, user_request_factory: IUserRequestFactory, verification_manager: IVerificationManager, mail_manager: IMailManager):
        self.__user_request_factory = user_request_factory
        self.__verification_manager = verification_manager
        self.__mail_manager = mail_manager

    def __create(self, email: str) -> dict[str, any]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/users')
        request = self.__user_request_factory.generate(email)
        files = {
            'request': (None, json.dumps(request.payload), 'application/json'),
            'image': ('avatar.jpg', request.image, 'image/jpeg')
        }

        response = requests.post(url, files = files)
        response.raise_for_status()
        logging.info(response.text)
        return response.json()

    def generate(self, num_users: int = 25) -> None:
        for _ in range(num_users):
            try:
                keycloak_id = self.__create(self.__mail_manager.email_address).get('keycloakId')
                self.__verification_manager.verify(keycloak_id)
                self.__mail_manager.refresh()
            except Exception as e:
                logging.error(e)
        self.__mail_manager.close()
