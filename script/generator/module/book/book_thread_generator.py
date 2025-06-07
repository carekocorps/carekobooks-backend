from common.factory.book_thread_request_factory import IBookThreadRequestFactory
from common.manager.auth_manager import IAuthManager
from common.provider.api_provider import ApiProvider
from config import ApiConfig
import requests
import logging
import random
import urllib

class BookThreadGenerator:
    def __init__(self, request_factory: IBookThreadRequestFactory, auth_manager: IAuthManager):
        self.__request_factory = request_factory
        self.__auth_manager = auth_manager

    def generate(self, user_review_prob: float = 0.05) -> None:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/books/threads')
        books = ApiProvider.fetch_books()
        users = ApiProvider.fetch_users()

        for book in books:
            for user in random.sample(users, int(len(books) * user_review_prob)):
                try:
                    request = self.__request_factory.generate(book, user)
                    response = requests.post(url, json = request.payload, headers = self.__auth_manager.authorization_header)
                    logging.info(response.text)
                except Exception as e:
                    logging.error(e)
