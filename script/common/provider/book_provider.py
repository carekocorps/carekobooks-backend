from config import Config
import requests

class BookProvider:
    @staticmethod
    def existing_books(config: Config):
        params = {
            'pageSize': 50,
            'orderBy': 'created-at',
            'isAscendingOrder': False
        }

        response = requests.get(config.book_provider_url, params = params)
        response.raise_for_status()
        return response.json().get('content')
