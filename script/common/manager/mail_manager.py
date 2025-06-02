from abc import ABC, abstractmethod
from config import Config
import mailslurp_client

class IMailManager(ABC):
    @property
    @abstractmethod
    def inbox(self):
        pass

    @property
    @abstractmethod
    def email_address(self) -> str:
        pass

    @property
    @abstractmethod
    def latest_email(self, timeout_ms: int = 60000) -> str:
        pass

    @abstractmethod
    def refresh(self) -> None:
        pass

    @abstractmethod
    def close(self) -> None:
        pass

class MailSlurpManager(IMailManager):
    def __init__(self, config: Config):
        self.__mailslurp_config = mailslurp_client.Configuration()
        self.__mailslurp_config.api_key['x-api-key'] = config.mailslurp_api_key
        self.__api_client = mailslurp_client.ApiClient(self.__mailslurp_config)
        self.__inbox_controller = mailslurp_client.InboxControllerApi(self.__api_client)
        self.__wait_controller = mailslurp_client.WaitForControllerApi(self.__api_client)
        self.__inbox = None

    @property
    def inbox(self):
        if self.__inbox is None:
            self.refresh()
        return self.__inbox

    @property
    def email_address(self) -> str:
        return self.inbox.email_address

    @property
    def latest_email(self, timeout_ms: int = 60000) -> None:
        email = self.__wait_controller.wait_for_latest_email(inbox_id = self.inbox.id, timeout = timeout_ms)
        return email.body
    
    def refresh(self) -> None:
        self.__inbox = self.__inbox_controller.create_inbox()

    def close(self) -> None:
        self.__api_client.close()
