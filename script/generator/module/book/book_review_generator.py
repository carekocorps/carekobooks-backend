from common.provider.auth_provider import AuthProvider
from common.provider.book_provider import BookProvider
from common.provider.user_provider import UserProvider
from common.factory.book_review_factory import IBookReviewFactory
from config import Config
import requests
import logging
import random

class BookReviewGenerator:
    def __init__(self, review_factory: IBookReviewFactory, config: Config):
        self.__review_factory = review_factory
        self.__config = config
    
    def generate(self, book_review_prob: float = 0.75, user_review_prob: float = 0.10) -> None:
        url = self.__config.book_review_provider_url
        cookies = AuthProvider.cookies(self.__config)
        books = BookProvider.existing_books(self.__config)
        users = UserProvider.existing_users(self.__config)

        for book in random.sample(books, int(len(books) * book_review_prob)):
            try:
                for user in random.sample(users, int(len(users) * user_review_prob)):
                    payload = self.__review_factory.generate(book, user)
                    response = requests.post(url, json = payload, cookies = cookies)
                    logging.info(response.text)
                    response.raise_for_status()
            except Exception as ex:
                logging.error(ex)
