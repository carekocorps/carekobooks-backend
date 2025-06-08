from common.provider.image_provider import ImageProvider
from typing import Optional
from abc import ABC, abstractmethod
from faker import Faker

class UserRequest:
    def __init__(self, payload: dict[str, any], image: Optional[bytes]):
        self.payload = payload
        self.image = image

class IUserRequestFactory(ABC):
    @abstractmethod
    def generate(self) -> UserRequest:
        pass

class UserRequestFactory(IUserRequestFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self) -> UserRequest:
        payload = {
            'username': self.__faker.user_name(),
            'displayName': self.__faker.name(),
            'email': self.__faker.email(),
            'description': self.__faker.sentence(nb_words = 25)
        }

        image = ImageProvider.fetch(self.__faker.image_url(500, 500))
        return UserRequest(payload, image)
