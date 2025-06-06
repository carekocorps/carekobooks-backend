from abc import ABC, abstractmethod
from faker import Faker

class ICredentialsFactory(ABC):
    @abstractmethod
    def generate(self) -> str:
        pass

class CredentialsFactory(ICredentialsFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self) -> str:
        return self.__faker.password()
