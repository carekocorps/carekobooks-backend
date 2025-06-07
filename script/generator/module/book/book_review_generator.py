from common.factory.book_review_request_factory import IBookReviewRequestFactory
from common.manager.auth_manager import IAuthManager
from common.provider.api_provider import ApiProvider
from config import ApiConfig
import requests
import logging
import random
import urllib

class BookReviewGenerator:
    def __init__(self, request_factory: IBookReviewRequestFactory, auth_manager: IAuthManager):
        self.__request_factory = request_factory
        self.__auth_manager = auth_manager
    
    def generate(self, book_review_prob: float = 0.75, user_review_prob: float = 0.10) -> None:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'api/v1/books/reviews')
        books = ApiProvider.fetch_books()
        users = ApiProvider.fetch_users()

        for book in random.sample(books, int(len(books) * book_review_prob)):
            for user in random.sample(users, int(len(users) * user_review_prob)):
                try:
                    request = self.__request_factory.generate(book, user)
                    response = requests.post(url, json = request.payload, headers = self.__auth_manager.authorization_header)
                    logging.info(response.text)
                    response.raise_for_status()
                except Exception as e:
                    logging.error(e)
