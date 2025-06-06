from common.factory.credentials_factory import CredentialsFactory
from common.manager.keycloak_manager import KeycloakManager
from faker import Faker

faker = Faker()
credentials_factory = CredentialsFactory(faker)
keycloak_manager = KeycloakManager(credentials_factory)
access_token = keycloak_manager.access_token()
print(access_token)
