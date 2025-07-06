from abc import ABC, abstractmethod
from faker import Faker
from typing import Any
import random

class BookRequest:
    def __init__(self, payload: dict[str, Any]):
        self.payload = payload

class IBookReviewRequestFactory(ABC):
    @abstractmethod
    def generate(self, book: dict[str, Any], user: dict[str, Any]) -> BookRequest:
        pass

class BookReviewRequestFactory(IBookReviewRequestFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, book: dict[str, Any], user: dict[str, Any]) -> BookRequest:
        payload = {
            'title': self.__faker.sentence(nb_words = 20),
            'content': self.__faker.sentence(nb_words = 150),
            'score': random.randint(0, 100),
            'username': user.get('username'),
            'bookId': book.get('id')
        }

        return BookRequest(payload)
