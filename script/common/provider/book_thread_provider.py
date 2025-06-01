from config import Config
import requests

class BookThreadProvider:
    @staticmethod
    def existing_threads(config: Config):
        params = {
            'pageSize': 50,
            'orderBy': 'created-at',
            'isAscendingOrder': False
        }

        response = requests.get(config.book_thread_provider_url, params = params)
        response.raise_for_status()
        return response.json().get('content')
