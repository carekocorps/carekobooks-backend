from config import Config
import requests

class UserProvider:
    @staticmethod
    def existing_users(config: Config) -> list[dict]:
        params = {
            'isEnabled': True,
            'pageSize': 50,
            'orderBy': 'created-at',
            'isAscendingOrder': False
        }

        response = requests.get(config.user_provider_url, params = params)
        response.raise_for_status()
        return response.json().get('content')
