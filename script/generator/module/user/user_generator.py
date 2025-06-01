from common.factory.user_factory import IUserFactory
from common.factory.image_factory import IImageFactory
from common.manager.mail_manager import IMailManager
from config import Config
import requests
import time
import re

class UserGenerator:
    __otp_pattern = re.compile(
        r'''font-family: 'Roboto',\s*monospace.*?>([a-zA-Z0-9]{8})</p>''',
        re.DOTALL
    )

    def __init__(self, mail_manager: IMailManager, image_factory: IImageFactory, user_factory: IUserFactory, config: Config):
        self.__mail_manager = mail_manager
        self.__image_factory = image_factory
        self.__user_factory = user_factory
        self.__config = config

    def __signup(self, username: str) -> None:
        url = self.__config.user_signup_url
        payload = self.__user_factory.generate(username, self.__mail_manager.email_address)
        image = self.__image_factory.generate()
        files = {
            'request': (None, payload, 'application/json'),
            'image': ('avatar.jpg', image, 'image/jpeg')
        }

        response = requests.post(url, files = files)
        response.raise_for_status()

    def __extract_otp(self) -> str | None:
        content = self.__mail_manager.latest_email()
        match = self.__otp_pattern.search(content)
        if not match:
            return None
        return match.group(1)

    def __verify_otp(self, username: str) -> None:
        url = self.__config.user_otp_verify_url
        payload = {
            'username': username,
            'otp': self.__extract_otp(),
            'validationType': 'REGISTRATION'
        }

        response = requests.post(url, json = payload)
        response.raise_for_status()
        print(f'User {username} was created successfully')

    def generate(self, num_users: int = 25, delay: int = 15) -> None:
        for _ in range(num_users):
            try:
                username = self.__user_factory.generate_username()
                self.__signup(username)

                time.sleep(delay)
                self.__verify_otp(username)
                self.__mail_manager.refresh()
            except Exception:
                continue
        self.__mail_manager.close()
