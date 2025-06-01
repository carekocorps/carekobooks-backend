from common.provider.auth_provider import AuthProvider
from common.provider.user_provider import UserProvider
from common.provider.book_provider import BookProvider
from config import Config
import requests
import random

class BookProgressGenerator:
    __progress_statuses = ('READING', 'PLANS_TO_READ', 'FINISHED')
    
    def __init__(self, config: Config):
        self.__cookies = AuthProvider.cookies(config)
        self.__books = BookProvider.existing_books(config)
        self.__users = UserProvider.existing_users(config)
        self.__config = config

    def generate(self) -> None:
        for user in self.__users:
            for book in random.sample(self.__books, random.randint(0, len(self.__books))):
                try:
                    url = self.__config.book_progress_provider_url
                    payload = {
                        'username': user.get('username'),
                        'bookId': book.get('id'),
                        'status': random.choice(self.__progress_statuses),
                        'isFavorite': bool(random.getrandbits(1)),
                        'score': random.randint(0, 100)
                    }

                    response = requests.post(url, json = payload, cookies = self.__cookies)
                    print(response.text)                    
                except Exception:
                    continue
