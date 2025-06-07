from abc import ABC, abstractmethod
from faker import Faker

class BookThreadRequest:
    def __init__(self, payload: dict[str, any]):
        self.payload = payload

class IBookThreadRequestFactory(ABC):
    @abstractmethod
    def generate(self, book: dict[str, any], user: dict[str, any]) -> BookThreadRequest:
        pass

class BookThreadRequestFactory(IBookThreadRequestFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, book: dict[str, any], user: dict[str, any]) -> BookThreadRequest:
        payload = {
            'username': user.get('username'),
            'bookId': book.get('id'),
            'title': self.__faker.sentence(nb_words = 10),
            'description': self.__faker.sentence(nb_words = 100)
        }

        return BookThreadRequest(payload)
