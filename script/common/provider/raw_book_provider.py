import requests

class RawBookProvider:
    @staticmethod
    def fetch(genre_name: str):
        url = f'https://www.googleapis.com/books/v1/volumes?q=subject:{genre_name}&maxResults=40'
        response = requests.get(url)
        response.raise_for_status()
        return response.json().get('items', [])
