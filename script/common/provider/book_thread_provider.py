from config import Config
import requests

class BookThreadProvider:
    @staticmethod
    def existing_threads(config: Config):
        threads = []
        page_num = 0

        while True:
            params = {
                'pageNumber': page_num,
                'pageSize': 25,
                'orderBy': 'created-at',
                'isAscendingOrder': False
            }

            response = requests.get(config.book_thread_provider_url, params = params)
            response.raise_for_status()
            content = response.json().get('content')

            if not content:
                break

            threads.extend(content)
            page_num += 1
        return threads
