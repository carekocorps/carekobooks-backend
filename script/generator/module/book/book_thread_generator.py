from common.provider.auth_provider import AuthProvider
from common.provider.user_provider import UserProvider
from common.provider.book_provider import BookProvider
from common.factory.book_thread_factory import IBookThreadFactory
from config import Config
import requests
import random

class BookThreadGenerator:
    def __init__(self, thread_factory: IBookThreadFactory, config: Config):
        self.__thread_factory = thread_factory
        self.__cookies = AuthProvider.cookies(config)
        self.__books = BookProvider.existing_books(config)
        self.__users = UserProvider.existing_users(config)
        self.__config = config

    def generate(self, max_users_per_thread: int = 3) -> None:
        for book in self.__books:
            for user in random.sample(self.__users, random.randint(0, max_users_per_thread)):
                try:
                    payload = self.__thread_factory.generate(book, user)
                    response = requests.post(self.__config.book_thread_provider_url, json = payload, cookies = self.__cookies)
                    print(response.text)
                except Exception:
                    continue
