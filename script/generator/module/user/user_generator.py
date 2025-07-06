from common.factory.user_factory import IUserFactory
from common.manager.auth_manager import IAuthManager
from common.manager.keycloak_manager import IKeycloakManager
from config import ApiConfig
from typing import Any
import urllib.parse
import requests
import logging
import json

class UserGenerator:
    def __init__(self, user_factory: IUserFactory, auth_manager: IAuthManager, keycloak_manager: IKeycloakManager):
        self.__user_factory = user_factory
        self.__auth_manager = auth_manager
        self.__keycloak_manager = keycloak_manager

    def __create(self) -> dict[str, Any]:
        user = self.__keycloak_manager.create()
        request = self.__user_factory.generate_update_request(user.get('username'))
        files = {
            'request': (None, json.dumps(request.payload), 'application/json'),
            'image': ('avatar.jpg', request.image, 'image/jpeg')
        }

        url = urllib.parse.urljoin(ApiConfig.BASE_URL, f'''v1/users/{user.get('username')}''')
        response = requests.put(url, files = files, headers = self.__auth_manager.authorization_header)
        response.raise_for_status()
        logging.info(response.text)
        return response.json()

    def generate(self, num_users: int = 25) -> None:
        for _ in range(num_users):
            try:
                self.__create().get('keycloakId')
            except Exception as e:
                logging.error(e)
