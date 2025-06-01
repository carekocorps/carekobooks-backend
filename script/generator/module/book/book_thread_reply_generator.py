from common.provider.auth_provider import AuthProvider
from common.provider.user_provider import UserProvider
from common.provider.book_thread_provider import BookThreadProvider
from common.factory.book_thread_reply_factory import IBookThreadReplyFactory
from config import Config
import urllib
import requests
import random

class BookThreadReplyGenerator:
    def __init__(self, reply_factory: IBookThreadReplyFactory, config: Config):
        self.__reply_factory = reply_factory
        self.__cookies = AuthProvider.cookies(config)
        self.__users = UserProvider.existing_users(config)
        self.__threads = BookThreadProvider.existing_threads(config)
        self.__config = config

    def __generate_base_reply(self, book_thread_id: int):
        url = self.__config.book_thread_reply_provider_url
        payload = self.__reply_factory.generate(random.choice(self.__users).get('username'), book_thread_id)
        response = requests.post(url, json = payload, cookies = self.__cookies)
        response.raise_for_status()
        print(response.text)
        return response.json()

    def __generate_base_reply_child(self, reply_response: dict):
        url = urllib.parse.urljoin(self.__config.book_thread_reply_provider_url, f'{reply_response.get('id')}/children')
        payload = self.__reply_factory.generate(random.choice(self.__users).get('username'), reply_response.get('thread').get('id'))
        response = requests.post(url, json = payload, cookies = self.__cookies)
        response.raise_for_status()
        print(response.text)
        return response.json()

    def generate(self, max_base_replies_per_thread: int = 8, max_children_per_base_reply: int = 3) -> None:
        for book_thread in self.__threads:
            try:
                for _ in range(random.randint(0, max_base_replies_per_thread)):
                    response = self.__generate_base_reply(book_thread.get('id'))
                    for _ in range(random.randint(0, max_children_per_base_reply)):
                        self.__generate_base_reply_child(response)
            except Exception:
                continue
