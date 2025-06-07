from abc import ABC, abstractmethod
from keycloak import KeycloakOpenID
from config import ApiConfig, KeycloakConfig
import time

class IAuthManager(ABC):
    @property
    @abstractmethod
    def authorization_header(self) -> dict[str, str]:
        pass

class KeycloakAuthManager(IAuthManager):
    def __init__(self):
        self.__token = None
        self.__token_issued_at = None
        self.__keycloak_open_id = KeycloakOpenID(
            server_url = KeycloakConfig.SERVER_URL,
            realm_name = KeycloakConfig.REALM_NAME,
            client_id = KeycloakConfig.CLIENT_ID,
            client_secret_key = KeycloakConfig.CLIENT_SECRET
        )

    @property
    def token(self) -> dict[str, any]:
        if self.__token is None:
            self.__token = self.__keycloak_open_id.token(ApiConfig.ADMIN_USERNAME, ApiConfig.ADMIN_PASSWORD)
            self.__token_issued_at = time.time()
        elif time.time() > self.__token_issued_at + self.__token.get('expires_in'):
            self.__token = self.__keycloak_open_id.refresh_token(self.__token.get('refresh_token'))
            self.__token_issued_at = time.time()
        return self.__token

    @property
    def authorization_header(self) -> dict[str, str]:
        return {
            'Authorization': f'''Bearer {self.token.get('access_token')}'''
        }
