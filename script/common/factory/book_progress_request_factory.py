from abc import ABC, abstractmethod
from typing import Any
import random

class BookProgressRequest:
    def __init__(self, payload: dict[str, Any]):
        self.payload = payload

class IBookProgressRequestFactory(ABC):
    @abstractmethod
    def generate(self, book: dict[str, Any], user: dict[str, Any]) -> BookProgressRequest:
        pass

class BookProgressRequestFactory(IBookProgressRequestFactory):
    def generate(self, book: dict[str, Any], user: dict[str, Any]) -> BookProgressRequest:
        PROGRESS_STATUSES = 'READING', 'PLANS_TO_READ', 'FINISHED'
        payload = {
            'username': user.get('username'),
            'bookId': book.get('id'),
            'status': random.choice(PROGRESS_STATUSES),
            'isFavorite': bool(random.getrandbits(1)),
            'score': random.randint(0, 100)
        }

        return BookProgressRequest(payload)
