from dotenv import load_dotenv
from os import getenv

load_dotenv()

class ApiConfig:
    BASE_URL = getenv('API_BASE_URL') + '/'
    ADMIN_USERNAME = getenv('API_ADMIN_USERNAME')
    ADMIN_PASSWORD = getenv('API_ADMIN_PASSWORD')

class KeycloakConfig:
    SERVER_URL = getenv('KEYCLOAK_SERVER_URL')
    REALM_NAME = getenv('KEYCLOAK_REALM_NAME')
    CLIENT_ID = getenv('KEYCLOAK_CLIENT_ID')
    CLIENT_SECRET = getenv('KEYCLOAK_CLIENT_SECRET')

class DatabaseConfig:
    HOST = getenv('DATABASE_HOST')
    PORT = getenv('DATABASE_PORT')
    NAME = getenv('DATABASE_NAME')
    USERNAME = getenv('DATABASE_USERNAME')
    PASSWORD = getenv('DATABASE_PASSWORD')
