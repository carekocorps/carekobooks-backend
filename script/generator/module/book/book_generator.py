from common.factory.book_factory import IBookFactory
from common.provider.auth_provider import AuthProvider
from common.provider.book_genre_provider import BookGenreProvider
from config import Config
import requests

class BookGenerator:
    def __init__(self, book_factory: IBookFactory, config: Config):
        self.__cookies = AuthProvider.cookies(config)
        self.__books = book_factory.generate(BookGenreProvider.existing_genre_names(config))
        self.__config = config

    def generate(self) -> None:
        for book in self.__books:
            try:
                files = {
                    'request': (None, book.payload, 'application/json'),
                    'image': ('cover.png', book.image, 'image/jpeg')
                }

                response = requests.post(self.__config.book_provider_url, files = files, cookies = self.__cookies)
                response.raise_for_status()
                print(response.text)
            except Exception:
                continue
