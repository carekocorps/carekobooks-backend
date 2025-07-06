from abc import ABC, abstractmethod
from faker import Faker
from typing import Any

class BookThreadReplyRequest:
    def __init__(self, payload: dict[str, Any]):
        self.payload = payload

class IBookThreadReplyRequestFactory(ABC):
    @abstractmethod
    def generate(self, username: str, thread_id: int) -> BookThreadReplyRequest:
        pass

class BookThreadReplyRequestFactory(IBookThreadReplyRequestFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, username: str, thread_id: int) -> BookThreadReplyRequest:
        payload = {
            'username': username,
            'threadId': thread_id,
            'content': self.__faker.sentence(nb_words = 30)
        }

        return BookThreadReplyRequest(payload)
