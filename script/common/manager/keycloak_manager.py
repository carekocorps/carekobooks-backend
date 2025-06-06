from common.factory.credentials_factory import ICredentialsFactory
from abc import ABC, abstractmethod
from keycloak import KeycloakOpenID, KeycloakAdmin
from config import ApiConfig, KeycloakConfig

class IKeycloakManager(ABC):
    @abstractmethod
    def access_token(self) -> str:
        pass

    @abstractmethod
    def verify(self, keycloak_id: str) -> None:
        pass

class KeycloakManager(IKeycloakManager):
    def __init__(self, credentials_factory: ICredentialsFactory):
        self.__credentials_factory = credentials_factory
        self.__keycloak_openid = KeycloakOpenID(
            server_url = KeycloakConfig.SERVER_URL,
            realm_name = KeycloakConfig.REALM_NAME,
            client_id = KeycloakConfig.CLIENT_ID,
            client_secret_key = KeycloakConfig.CLIENT_SECRET
        )

        self.__keycloak_admin = KeycloakAdmin(
            server_url = KeycloakConfig.SERVER_URL,
            realm_name = KeycloakConfig.REALM_NAME,
            client_id = KeycloakConfig.CLIENT_ID,
            client_secret_key = KeycloakConfig.CLIENT_SECRET,
            grant_type = 'client_credentials'
        )

    def access_token(self) -> str:
        token = self.__keycloak_openid.token(ApiConfig.ADMIN_USERNAME, ApiConfig.ADMIN_PASSWORD)
        return token.get('access_token')

    def verify(self, keycloak_id: str) -> None:
        self.__keycloak_admin.set_user_password(
            user_id = keycloak_id,
            password = self.__credentials_factory.generate(),
            temporary = False
        )
        
        self.__keycloak_admin.update_user(
            user_id = keycloak_id,
            payload = {
                'emailVerified': True,
                'requiredActions': []
            }
        )

