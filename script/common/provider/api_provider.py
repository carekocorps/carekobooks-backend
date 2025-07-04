from config import ApiConfig
import requests
import urllib

class ApiProvider:
    @staticmethod
    def fetch_books() -> list[dict[str, any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/books')
        return ApiProvider.__fetch(url)

    @staticmethod
    def fetch_book_genres() -> list[dict[str, any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/books/genres')
        return ApiProvider.__fetch(url)

    @staticmethod
    def fetch_book_threads() -> list[dict[str, any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/books/threads')
        return ApiProvider.__fetch(url)   

    @staticmethod
    def fetch_users() -> list[dict[str, any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/users')
        return [user for user in ApiProvider.__fetch(url) if user.get('username') != 'admin']

    @staticmethod
    def __fetch(url: str) -> list[dict[str, any]]:
        items = []
        params = {
            'pageNumber': 0,
            'pageSize': 50,
            'orderBy': 'created-at',
            'isAscendingOrder': False
        }

        while True:
            response = requests.get(url, params = params)
            response.raise_for_status()
            content = response.json().get('content')

            if not content:
                break

            items.extend(content)
            params['pageNumber'] += 1
        
        return items
