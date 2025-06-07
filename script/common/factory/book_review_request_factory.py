from abc import ABC, abstractmethod
from faker import Faker
import random

class BookRequest:
    def __init__(self, payload: dict[str, any]):
        self.payload = payload

class IBookReviewRequestFactory(ABC):
    @abstractmethod
    def generate(self, book: dict[str, any], user: dict[str, any]) -> BookRequest:
        pass

class BookReviewRequestFactory(IBookReviewRequestFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, book: dict[str, any], user: dict[str, any]) -> BookRequest:
        payload = {
            'title': self.__faker.sentence(nb_words = 20),
            'content': self.__faker.sentence(nb_words = 150),
            'score': random.randint(0, 100),
            'username': user.get('username'),
            'bookId': book.get('id')
        }

        return BookRequest(payload)
