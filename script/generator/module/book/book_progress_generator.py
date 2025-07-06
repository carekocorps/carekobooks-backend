from common.factory.book_progress_request_factory import IBookProgressRequestFactory
from common.manager.auth_manager import IAuthManager
from common.provider.api_provider import ApiProvider
from config import ApiConfig
import requests
import logging
import random
import urllib

class BookProgressGenerator:
    def __init__(self, request_factory: IBookProgressRequestFactory, auth_manager: IAuthManager):
        self.__request_factory = request_factory
        self.__auth_manager = auth_manager

    def generate(self, user_progress_prob: float = 0.25) -> None:
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'v1/books/progresses') 
        books = ApiProvider.fetch_books()
        users = ApiProvider.fetch_users()

        for user in users:
            for book in random.sample(books, int(len(books) * user_progress_prob)):
                try:
                    request = self.__request_factory.generate(book, user)
                    response = requests.post(url, json = request.payload, headers = self.__auth_manager.authorization_header)
                    logging.info(response.text)                    
                except Exception as e:
                    logging.error(e)
