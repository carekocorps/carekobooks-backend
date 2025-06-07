import urllib.parse
from common.factory.book_request_factory import IBookRequestFactory
from common.manager.auth_manager import IAuthManager
from common.provider.api_provider import ApiProvider
from config import ApiConfig
import requests
import logging
import urllib
import json

class BookGenerator:
    def __init__(self, request_factory: IBookRequestFactory, auth_manager: IAuthManager):
        self.__request_factory = request_factory
        self.__auth_manager = auth_manager

    def generate(self) -> None:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/books')
        genre_names = [genre.get('name') for genre in ApiProvider.fetch_book_genres()]
        books = self.__request_factory.generate(genre_names)

        for book in books:
            try:
                files = {
                    'request': (None, json.dumps(book.payload), 'application/json'),
                    'image': ('cover.png', book.image, 'image/jpeg')
                }

                response = requests.post(url, files = files, headers = self.__auth_manager.authorization_header)
                logging.info(response.text)
                response.raise_for_status()
            except Exception as e:
                logging.error(e)
