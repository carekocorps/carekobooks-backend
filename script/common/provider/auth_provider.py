from config import Config
import requests

class AuthProvider:
    @staticmethod
    def cookies(config: Config) -> dict:
        payload = {
            'username': config.auth_username,
            'password': config.auth_username
        }

        session = requests.Session()
        response = session.post(config.auth_provider_url, json = payload)
        response.raise_for_status()
        return session.cookies.get_dict()
