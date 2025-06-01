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
    def email_address(self):
        pass

    @abstractmethod
    def refresh(self):
        pass

    @abstractmethod
    def latest_email(self, timeout_ms: int):
        pass

    @abstractmethod
    def close(self):
        pass

class MailSlurpManager(IMailManager):
    def __init__(self, config: Config):
        mailslurp_config = mailslurp_client.Configuration()
        mailslurp_config.api_key['x-api-key'] = config.mailslurp_api_key
        self.__api_client = mailslurp_client.ApiClient(mailslurp_config)
        self.__inbox_controller = mailslurp_client.InboxControllerApi(self.__api_client)
        self.__inbox = None
    
    @property
    def inbox(self):
        if self.__inbox is None:
            self.renew_inbox()
        return self.__inbox

    @property
    def email_address(self):
        return self.inbox.email_address

    def latest_email(self, timeout_ms: int = 60000):
        return self.__inbox_controller.get_latest_email_in_inbox(inbox_id = self.inbox.id, timeout_millis = timeout_ms)
    
    def refresh(self):
        self.__inbox = self.__inbox_controller.create_inbox()

    def close(self):
        self.__api_client.close()
