from abc import ABC, abstractmethod
from faker import Faker
import requests

class IImageFactory(ABC):
    @abstractmethod
    def generate_by_url(self, url: str) -> bytes:
        pass

    @abstractmethod
    def generate(self, width: int, height: int) -> bytes:
        pass

class ImageFactory(IImageFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate_by_url(self, url: str) -> bytes:
        response = requests.get(url)
        response.raise_for_status()
        return response.content

    def generate(self, width: int = 500, height: int = 500) -> bytes:
        url = self.__faker.image_url(width, height)
        return self.generate(url, width, height)
