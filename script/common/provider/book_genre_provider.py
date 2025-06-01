from config import Config
import requests

class BookGenreProvider:
    @staticmethod
    def existing_genres(config: Config) -> list[dict]:
        params = {
            'pageSize': 50,
            'orderBy': 'created-at',
            'isAscendingOrder': False
        }

        response = requests.get(config.book_genre_provider_url, params = params)
        response.raise_for_status()
        return response.json().get('content')
    
    @staticmethod
    def existing_genre_names(config: Config) -> list[str]:
        return [genre['name'] for genre in BookGenreProvider.existing_genres(config)]
