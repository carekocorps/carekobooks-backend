from abc import ABC, abstractmethod
from faker import Faker
from typing import Any

class BookThreadRequest:
    def __init__(self, payload: dict[str, Any]):
        self.payload = payload

class IBookThreadRequestFactory(ABC):
    @abstractmethod
    def generate(self, book: dict[str, Any], user: dict[str, Any]) -> BookThreadRequest:
        pass

class BookThreadRequestFactory(IBookThreadRequestFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, book: dict[str, Any], user: dict[str, Any]) -> BookThreadRequest:
        payload = {
            'username': user.get('username'),
            'bookId': book.get('id'),
            'title': self.__faker.sentence(nb_words = 10),
            'description': self.__faker.sentence(nb_words = 100)
        }

        return BookThreadRequest(payload)
