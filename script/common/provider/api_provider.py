from config import ApiConfig
from typing import Any
import requests
import urllib.parse

class ApiProvider:
    @staticmethod
    def fetch_books() -> list[dict[str, Any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'v1/books')
        return ApiProvider.__fetch(url)

    @staticmethod
    def fetch_book_genres() -> list[dict[str, Any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'v1/books/genres')
        return ApiProvider.__fetch(url)

    @staticmethod
    def fetch_book_threads() -> list[dict[str, Any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'v1/books/threads')
        return ApiProvider.__fetch(url)   

    @staticmethod
    def fetch_users() -> list[dict[str, Any]]:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'v1/users')
        return [user for user in ApiProvider.__fetch(url) if user.get('username') != 'admin']

    @staticmethod
    def __fetch(url: str) -> list[dict[str, Any]]:
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
