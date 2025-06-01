from config import Config
import requests

class UserProvider:
    @staticmethod
    def existing_users(config: Config) -> list[dict]:
        users = []
        page_num = 0

        while True:
            params = {
                'isEnabled': True,
                'pageNumber': page_num,
                'pageSize': 25,
                'orderBy': 'created-at',
                'isAscendingOrder': False
            }

            response = requests.get(config.user_provider_url, params = params)
            response.raise_for_status()
            content = [user for user in response.json().get('content') if user.get('username') not in ('string', 'admin')]

            if not content:
                break

            users.extend(content)
            page_num += 1
        return users
