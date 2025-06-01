from common.factory.image_factory import IImageFactory
from abc import ABC, abstractmethod
import requests

class IBookFactory(ABC):
    @abstractmethod
    def generate(self) -> dict:
        pass

class BookRequestParams:
    def __init__(self, payload: dict, image: bytes):
        self.payload = payload
        self.image = image

class BookFactory(IBookFactory):
    def __init__(self, image_factory: IImageFactory):
        self.__image_factory = image_factory

    def __raw_books(self, genre_name: str) -> list[dict]:
        url = f'https://www.googleapis.com/books/v1/volumes?q=subject:{genre_name}&maxResults=40'
        response = requests.get(url)
        response.raise_for_status()
        return response.json().get('items', [])

    def generate(self, genre_names: list[str]) -> list[BookRequestParams]:
        books = []
        for genre_name in genre_names:
            for raw_book in self.__raw_books(genre_name):
                try:
                    info = raw_book.get('volumeInfo')
                    payload = {
                        'title': info.get('title'),
                        'synopsis': info.get('description'),
                        'authorName': ', '.join(info.get('authors', ['Unknown Author'])),
                        'publisherName': info.get('publisher', 'Unknown Publisher'),
                        'publishedAt': info.get('publishedDate', '1900-01-01')[:4] + '-01-01',
                        'pageCount': info.get('pageCount'),
                        'genres': (genre_name)
                    }

                    if not payload.get('pageCount'):
                        continue

                    image_url = info.get('imageLinks', {}).get('thumbnail')
                    if image_url is None:
                        continue

                    image = self.__image_factory.generate_by_url(image_url)
                    books.append(BookRequestParams(payload, image))
                except Exception:
                    continue
        return books
