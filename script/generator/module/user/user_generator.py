import urllib.parse
from common.factory.user_factory import IUserFactory
from common.manager.keycloak_manager import IKeycloakManager
from config import ApiConfig
import requests
import logging
import urllib

class UserGenerator:
    def __init__(self, user_factory: IUserFactory, keycloak_manager: IKeycloakManager):
        self.__user_factory = user_factory
        self.__keycloak_manager = keycloak_manager

    def __create(self) -> dict[str, any]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/users')
        payload = self.__user_factory.generate()
        response = requests.post(url, json = payload)
        response.raise_for_status()

        logging.info(response.text)
        return response.json()

    def generate(self, num_users: int = 25) -> None:
        for _ in range(num_users):
            try:
                keycloak_id = self.__create().get('keycloakId')
                self.__keycloak_manager.verify(keycloak_id)
            except Exception as ex:
                logging.error(ex)
