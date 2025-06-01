from common.provider.auth_provider import AuthProvider
from common.provider.user_provider import UserProvider
from common.provider.book_provider import BookProvider
from config import Config
import requests
import logging
import random

class BookProgressGenerator:
    __progress_statuses = ('READING', 'PLANS_TO_READ', 'FINISHED')
    
    def __init__(self, config: Config):
        self.__config = config

    def generate(self, user_progress_prob: float = 0.25) -> None:
        cookies = AuthProvider.cookies(self.__config)
        books = BookProvider.existing_books(self.__config)
        users = UserProvider.existing_users(self.__config)

        for user in users:
            for book in random.sample(books, int(len(books) * user_progress_prob)):
                try:
                    url = self.__config.book_progress_provider_url
                    payload = {
                        'username': user.get('username'),
                        'bookId': book.get('id'),
                        'status': random.choice(self.__progress_statuses),
                        'isFavorite': bool(random.getrandbits(1)),
                        'score': random.randint(0, 100)
                    }

                    response = requests.post(url, json = payload, cookies = cookies)
                    logging.info(response.text)                    
                except Exception as ex:
                    logging.error(ex)
