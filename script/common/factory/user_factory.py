from abc import ABC, abstractmethod
from faker import Faker

class IUserFactory:
    @abstractmethod
    def generate_username(self) -> str:
        pass

    @abstractmethod
    def generate(self, username:str, email: str) -> dict:
        pass

class UserFactory(IUserFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker

    def generate_username(self) -> str:
        return self.__faker.user_name()

    def generate(self, username: str, email: str) -> dict:
        return {
            'username': username,
            'email': email,
            'displayName': self.__faker.name(),
            'password': self.__faker.password(length = 10),
            'description': self.__faker.sentence(nb_words = 20)
        }
