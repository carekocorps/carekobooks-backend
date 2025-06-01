from common.provider.auth_provider import AuthProvider
from common.provider.book_provider import BookProvider
from common.provider.user_provider import UserProvider
from common.factory.book_review_factory import BookReviewFactory
from config import Config
import requests
import random

class BookReviewGenerator:
    def __init__(self, review_factory: BookReviewFactory, config: Config):
        self.__review_factory = review_factory
        self.__cookies = AuthProvider.cookies(config)
        self.__books = BookProvider.existing_books(config)
        self.__users = UserProvider.existing_users(config)
        self.__config = config
    
    def generate(self, book_review_prob: float = 0.8, user_review_prob: float = 0.2) -> None:
        url = self.__config.book_review_provider_url
        for book in random.sample(self.__books, int(len(self.__books) * book_review_prob)):
            try:
                for user in random.sample(self.__users, int(len(self.__users) * user_review_prob)):
                    payload = self.__review_factory.generate(book, user)
                    response = requests.post(url, json = payload, cookies = self.__cookies)
                    response.raise_for_status()
                    print(response.text)
            except Exception:
                continue
