from common.provider.image_provider import ImageProvider
from typing import Optional
from abc import ABC, abstractmethod
from faker import Faker
from typing import Any

class UserRepresentation:
    def __init__(self, payload: dict[str, Any]):
        self.payload = payload

class UserUpdateRequest:
    def __init__(self, payload: dict[str, Any], image: Optional[bytes]):
        self.payload = payload
        self.image = image

class IUserFactory(ABC):
    @abstractmethod
    def generate_representation(self) -> UserRepresentation:
        pass

    @abstractmethod
    def generate_update_request(self, username: str) -> UserUpdateRequest:
        pass

class UserFactory(IUserFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate_representation(self) -> UserRepresentation:
        payload = {
            'username': self.__faker.user_name(),
            'email': self.__faker.email(),
            'emailVerified': True,
            'enabled': True,
            'requiredActions': [],
            'credentials': [
                {
                    'type': 'password',
                    'value': self.__faker.password(length = 12, special_chars = True),
                    'temporary': False
                }
            ]
        }

        return UserRepresentation(payload)

    def generate_update_request(self, username: str) -> UserUpdateRequest:
        payload = {
            'username': username,
            'displayName': self.__faker.name(),
            'description': self.__faker.sentence(nb_words = 20),
            'retainCurrentImage': True
        }

        image = ImageProvider.fetch(self.__faker.image_url(500, 500))
        return UserUpdateRequest(payload, image)
