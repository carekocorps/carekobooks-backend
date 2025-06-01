from abc import ABC, abstractmethod
from faker import Faker
from typing import Optional
import requests
import logging

class IImageFactory(ABC):
    @abstractmethod
    def generate_by_url(self, url: str) -> Optional[bytes]:
        pass

    @abstractmethod
    def generate(self, width: int, height: int) -> Optional[bytes]:
        pass

class ImageFactory(IImageFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate_by_url(self, url: str) -> Optional[bytes]:
        response = requests.get(url)
        if response.status_code != 200:
            logging.warning(response.text)
            return None
        return response.content

    def generate(self, width: int = 500, height: int = 500) -> Optional[bytes]:
        url = self.__faker.image_url(width, height)
        return self.generate_by_url(url)
