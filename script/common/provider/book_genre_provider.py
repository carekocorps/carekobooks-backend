from config import Config
import requests

class BookGenreProvider:
    @staticmethod
    def existing_genres(config: Config) -> list[dict]:
        genres = []
        page_num = 0

        while True:
            params = {
                'pageNumber': page_num,
                'pageSize': 50,
                'orderBy': 'created-at',
                'isAscendingOrder': False
            }

            response = requests.get(config.book_genre_provider_url, params = params)
            response.raise_for_status()
            content = response.json().get('content')

            if not content:
                break

            genres.extend(content)
            page_num += 1
        return genres
    
    @staticmethod
    def existing_genre_names(config: Config) -> list[str]:
        return [genre['name'] for genre in BookGenreProvider.existing_genres(config)]
