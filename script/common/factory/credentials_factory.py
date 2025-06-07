from abc import ABC, abstractmethod
from faker import Faker

class Credentials:
    def __init__(self, user_id: str, password: str, temporary: bool):
        self.user_id = user_id
        self.password = password
        self.temporary = temporary

class ICredentialsFactory(ABC):
    @abstractmethod
    def generate(self, user_id: str) -> Credentials:
        pass

class CredentialsFactory(ICredentialsFactory):
    def __init__(self, faker: Faker):
        self.__faker = faker
    
    def generate(self, user_id: str) -> Credentials:
        password = self.__faker.password()
        return Credentials(user_id, password, False)
