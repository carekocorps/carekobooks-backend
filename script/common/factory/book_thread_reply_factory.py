from abc import ABC, abstractmethod
from faker import Faker

class IBookThreadReplyFactory(ABC):
    @abstractmethod
    def generate(self, username: str, thread_id: int) -> dict:
        pass

class BookThreadReplyFactory(IBookThreadReplyFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, username: str, thread_id: int):
        return {
            'content': self.__faker.sentence(nb_words = 30),
            'username': username,
            'threadId': thread_id
        }
