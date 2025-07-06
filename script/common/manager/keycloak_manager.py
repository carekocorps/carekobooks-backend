from common.factory.user_factory import IUserFactory
from abc import ABC, abstractmethod
from keycloak import KeycloakAdmin
from config import KeycloakConfig
from typing import Any

class IKeycloakManager(ABC):
    @abstractmethod
    def create(self) -> dict[str, Any]:
        pass

class KeycloakManager(IKeycloakManager):
    def __init__(self, user_factory: IUserFactory):
        self.__user_factory = user_factory
        self.__keycloak_admin = KeycloakAdmin(
            server_url = KeycloakConfig.SERVER_URL,
            realm_name = KeycloakConfig.REALM_NAME,
            client_id = KeycloakConfig.CLIENT_ID,
            client_secret_key = KeycloakConfig.CLIENT_SECRET,
            grant_type = 'client_credentials'
        )

    def create(self) -> dict[str, Any]:
        user_representation = self.__user_factory.generate_representation().payload
        user_id = self.__keycloak_admin.create_user(user_representation)
        return self.__keycloak_admin.get_user(user_id)
