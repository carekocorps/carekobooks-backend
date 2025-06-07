from abc import ABC, abstractmethod
import random

class BookProgressRequest:
    def __init__(self, payload: dict[str, any]):
        self.payload = payload

class IBookProgressRequestFactory(ABC):
    @abstractmethod
    def generate(self, book: dict[str, any], user: dict[str, any]) -> BookProgressRequest:
        pass

class BookProgressRequestFactory(IBookProgressRequestFactory):
    def generate(self, book: dict[str, any], user: dict[str, any]) -> BookProgressRequest:
        PROGRESS_STATUSES = 'READING', 'PLANS_TO_READ', 'FINISHED'
        payload = {
            'username': user.get('username'),
            'bookId': book.get('id'),
            'status': random.choice(PROGRESS_STATUSES),
            'isFavorite': bool(random.getrandbits(1)),
            'score': random.randint(0, 100)
        }

        return BookProgressRequest(payload)
