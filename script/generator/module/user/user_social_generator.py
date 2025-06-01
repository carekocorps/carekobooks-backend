from common.provider.auth_provider import AuthProvider
from common.provider.user_provider import UserProvider
from config import Config
import requests
import logging
import random
import urllib

class UserSocialGenerator:
    def __init__(self, config: Config):
        self.__config = config

    def generate(self, user_follow_prob: float = 0.35) -> None:
        cookies = AuthProvider.cookies(self.__config)
        users = UserProvider.existing_users(self.__config)

        for user in users:
            username = user.get('username')
            for target_user in random.sample(users, int(len(users) * user_follow_prob)):
                try:
                    target_username = target_user.get('username')
                    if username == target_username:
                        continue

                    url = urllib.parse.urljoin(self.__config.user_provider_url + '/', f'{username}/social/following/{target_username}')
                    response = requests.post(url, cookies = cookies)
                    response.raise_for_status()
                    logging.info(f'Status {response.status_code}: {username} -> {target_username}')
                except Exception as ex:
                    logging.error(ex)
