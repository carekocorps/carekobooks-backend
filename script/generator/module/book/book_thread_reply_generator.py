from common.factory.book_thread_reply_request_factory import IBookThreadReplyRequestFactory
from common.manager.auth_manager import IAuthManager
from common.provider.api_provider import ApiProvider
from config import ApiConfig
from typing import Any
import urllib.parse
import requests
import logging
import random

class BookThreadReplyGenerator:
    def __init__(self, request_factory: IBookThreadReplyRequestFactory, auth_manager: IAuthManager):
        self.__request_factory = request_factory
        self.__auth_manager = auth_manager

    def __generate_base_reply(self, thread_id: int, users: list[dict[str, Any]]):
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, 'v1/books/threads/replies')
        request = self.__request_factory.generate(random.choice(users).get('username'), thread_id)
        response = requests.post(url, json = request.payload, headers = self.__auth_manager.authorization_header)
        logging.info(response.text)
        response.raise_for_status()
        return response.json()

    def __generate_base_reply_child(self, reply_response: dict[str, Any], users: list[dict[str, Any]]):
        url = urllib.parse.urljoin(ApiConfig.BASE_URL, f'''v1/books/threads/replies/{reply_response.get('id')}/children''')
        request = self.__request_factory.generate(random.choice(users).get('username'), reply_response.get('thread').get('id'))
        response = requests.post(url, json = request.payload, headers = self.__auth_manager.authorization_header)
        logging.info(response.text)
        response.raise_for_status()
        return response.json()

    def generate(self, max_base_replies_per_thread: int = 8, max_children_per_base_reply: int = 3) -> None:
        threads = ApiProvider.fetch_book_threads()
        users = ApiProvider.fetch_users()

        for book_thread in threads:
            for _ in range(random.randint(0, max_base_replies_per_thread)):
                try:
                    response = self.__generate_base_reply(book_thread.get('id'), users)
                    for _ in range(random.randint(0, max_children_per_base_reply)):
                        self.__generate_base_reply_child(response, users)
                except Exception as e:
                    logging.error(e)
