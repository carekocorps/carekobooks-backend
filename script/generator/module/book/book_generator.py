from common.factory.book_factory import IBookFactory
from common.provider.auth_provider import AuthProvider
from common.provider.book_genre_provider import BookGenreProvider
from config import Config
import requests
import logging
import json

class BookGenerator:
    def __init__(self, book_factory: IBookFactory, config: Config):
        self.__book_factory = book_factory
        self.__config = config

    def generate(self) -> None:
        cookies = AuthProvider.cookies(self.__config)
        genre_names = BookGenreProvider.existing_genre_names(self.__config)
        books = self.__book_factory.generate(genre_names)

        for book in books:
            try:
                files = {
                    'request': (None, json.dumps(book.payload), 'application/json'),
                    'image': ('cover.png', book.image, 'image/jpeg')
                }

                response = requests.post(self.__config.book_provider_url, files = files, cookies = cookies)
                logging.info(response.text)
                response.raise_for_status()
            except Exception as ex:
                logging.error(ex)
