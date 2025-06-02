from config import Config
import requests

class BookProvider:
    @staticmethod
    def existing_books(config: Config) -> list[dict]:
        books = []
        page_num = 0

        while True:
            params = {
                'pageNumber': page_num,
                'pageSize': 50,
                'orderBy': 'created-at',
                'isAscendingOrder': False
            }

            response = requests.get(config.book_provider_url, params = params)
            response.raise_for_status()
            content = response.json().get('content')

            if not content:
                break

            books.extend(content)
            page_num += 1

        return books
