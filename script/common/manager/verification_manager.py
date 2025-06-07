from common.factory.credentials_factory import ICredentialsFactory
from abc import ABC, abstractmethod
from keycloak import KeycloakAdmin
from config import KeycloakConfig

class IVerificationManager(ABC):
    @abstractmethod
    def verify(self, id: str) -> None:
        pass

class KeycloakVerificationManager(IVerificationManager):
    def __init__(self, credentials_factory: ICredentialsFactory):
        self.__credentials_factory = credentials_factory
        self.__keycloak_admin = KeycloakAdmin(
            server_url = KeycloakConfig.SERVER_URL,
            realm_name = KeycloakConfig.REALM_NAME,
            client_id = KeycloakConfig.CLIENT_ID,
            client_secret_key = KeycloakConfig.CLIENT_SECRET,
            grant_type = 'client_credentials'
        )

    def verify(self, id: str) -> None:
        credentials = self.__credentials_factory.generate(id)
        self.__keycloak_admin.set_user_password(credentials.user_id, credentials.password, credentials.temporary)
        self.__keycloak_admin.update_user(
            user_id = id,
            payload = {
                'emailVerified': True,
                'requiredActions': []
            }
        )
