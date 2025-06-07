from common.provider.image_provider import ImageProvider
from common.provider.raw_book_provider import RawBookProvider
from abc import ABC, abstractmethod
from typing import Optional
import logging

class BookRequest:
    def __init__(self, payload: dict[str, any], image: Optional[bytes]):
        self.payload = payload
        self.image = image

class IBookRequestFactory(ABC):
    @abstractmethod
    def generate(self, genre_names: list[str]) -> list[BookRequest]:
        pass

class BookRequestFactory(IBookRequestFactory):
    def generate(self, genre_names: list[str]) -> list[BookRequest]:
        book_requests = []
        for genre_name in genre_names:
            for raw_book in RawBookProvider.fetch(genre_name):
                info = raw_book.get('volumeInfo')
                payload = {
                    'title': info.get('title'),
                    'synopsis': info.get('description'),
                    'authorName': ', '.join(info.get('authors', ['Unknown Author'])),
                    'publisherName': info.get('publisher', 'Unknown Publisher'),
                    'publishedAt': info.get('publishedDate', '1900-01-01')[:4] + '-01-01',
                    'pageCount': info.get('pageCount'),
                    'genres': [genre_name]
                }

                image_url = info.get('imageLinks', {}).get('thumbnail')
                if image_url is None:
                    continue

                try:
                    image = ImageProvider.fetch(image_url)
                    book_request = BookRequest(payload, image)
                    book_requests.append(book_request)
                    logging.info(book_request)
                except Exception as e:
                    logging.error(e)

        return book_requests
