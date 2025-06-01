from common.provider.auth_provider import AuthProvider
from common.provider.user_provider import UserProvider
from common.provider.book_provider import BookProvider
from common.factory.book_thread_factory import IBookThreadFactory
from config import Config
import requests
import logging
import random

class BookThreadGenerator:
    def __init__(self, thread_factory: IBookThreadFactory, config: Config):
        self.__thread_factory = thread_factory
        self.__config = config

    def generate(self, user_review_prob: float = 0.05) -> None:
        cookies = AuthProvider.cookies(self.__config)
        books = BookProvider.existing_books(self.__config)
        users = UserProvider.existing_users(self.__config)

        for book in books:
            for user in random.sample(users, int(len(books) * user_review_prob)):
                try:
                    payload = self.__thread_factory.generate(book, user)
                    response = requests.post(self.__config.book_thread_provider_url, json = payload, cookies = cookies)
                    logging.info(response.text)
                except Exception as ex:
                    logging.error(ex)
