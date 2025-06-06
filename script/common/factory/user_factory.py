from abc import ABC, abstractmethod
from faker import Faker

class IUserFactory(ABC):
    @abstractmethod
    def generate(self) -> dict[str, str]:
        pass

class UserFactory(IUserFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self) -> dict[str, str]:
        return {
            'username': self.__faker.user_name(),
            'displayName': self.__faker.name(),
            'email': self.__faker.email(),
            'description': self.__faker.sentence(nb_words = 25)
        }
