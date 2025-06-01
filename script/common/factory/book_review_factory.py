from abc import ABC, abstractmethod
from faker import Faker
import random

class IBookReviewFactory(ABC):
    @abstractmethod
    def generate(self) -> dict:
        pass

class BookReviewFactory(IBookReviewFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, book: dict, user: dict) -> dict:
        return {
            'title': self.__faker.sentence(nb_words = 20),
            'content': self.__faker.sentence(nb_words = 150),
            'score': random.randint(0, 100),
            'username': user.get('username'),
            'bookId': book.get('id')
        }
