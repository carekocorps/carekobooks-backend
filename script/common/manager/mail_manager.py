from abc import ABC, abstractmethod
from config import MailSlurpConfig
import mailslurp_client

class IMailManager(ABC):
    @property
    @abstractmethod
    def email_address(self) -> str:
        pass

    @abstractmethod
    def refresh(self) -> None:
        pass

    @abstractmethod
    def close(self) -> None:
        pass

class MailSlurpManager(IMailManager):
    def __init__(self):
        config = mailslurp_client.Configuration()
        config.api_key['x-api-key'] = MailSlurpConfig.API_KEY
        self.__api_client = mailslurp_client.ApiClient(config)
        self.__inbox_controller = mailslurp_client.InboxControllerApi(self.__api_client)
        self.__inbox = None

    @property
    def inbox(self):
        if self.__inbox is None:
            self.refresh()
        return self.__inbox

    @property
    def email_address(self) -> str:
        return self.inbox.email_address

    def refresh(self) -> None:
        self.__inbox = self.__inbox_controller.create_inbox()

    def close(self) -> None:
        self.__api_client.close()
