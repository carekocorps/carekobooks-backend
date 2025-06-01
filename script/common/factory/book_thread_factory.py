from abc import ABC, abstractmethod
from faker import Faker

class IBookThreadFactory(ABC):
    @abstractmethod
    def generate(self) -> dict:
        pass

class BookThreadFactory(IBookThreadFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, book: dict, user: dict):
        return {
            'username': user.get('username'),
            'bookId': book.get('id'),
            'title': self.__faker.sentence(nb_words = 10),
            'description': self.__faker.sentence(nb_words = 100)
        }
