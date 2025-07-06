from common.manager.auth_manager import IAuthManager
from common.provider.api_provider import ApiProvider
from config import ApiConfig
import requests
import logging
import random
import urllib

class UserSocialGenerator:
    def __init__(self, auth_manager: IAuthManager):
        self.__auth_manager = auth_manager

    def generate(self, user_follow_prob: float = 0.35) -> None:
        users = ApiProvider.fetch_users()
        for user in users:
            username = user.get('username')
            for target_user in random.sample(users, int(len(users) * user_follow_prob)):
                target_username = target_user.get('username')
                if username == target_username:
                    continue

                try:
                    url = urllib.parse.urljoin(ApiConfig.BASE_URL, f'v1/users/{username}/social/following/{target_username}')
                    response = requests.post(url, headers = self.__auth_manager.authorization_header)
                    response.raise_for_status()
                    logging.info(f'Status {response.status_code}: {username} -> {target_username}')
                except Exception as e:
                    logging.error(e)
