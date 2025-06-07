from typing import Optional
from faker import Faker
import requests
import logging

class ImageProvider:
    @staticmethod
    def fetch(url: str) -> Optional[bytes]:
        response = requests.get(url)
        if response.status_code != 200:
            logging.warning(f'Failed to fetch image from URL: {url}')
            return None
        return response.content
