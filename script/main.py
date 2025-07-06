from common.factory.book_request_factory import BookRequestFactory
from common.factory.book_progress_request_factory import BookProgressRequestFactory
from common.factory.book_review_request_factory import BookReviewRequestFactory
from common.factory.book_thread_request_factory import BookThreadRequestFactory
from common.factory.book_thread_reply_request_factory import BookThreadReplyRequestFactory
from common.factory.user_factory import UserFactory
from common.manager.auth_manager import KeycloakAuthManager
from common.manager.keycloak_manager import KeycloakManager
from generator.module.book.book_generator import BookGenerator
from generator.module.book.book_progress_generator import BookProgressGenerator
from generator.module.book.book_review_generator import BookReviewGenerator
from generator.module.book.book_thread_generator import BookThreadGenerator
from generator.module.book.book_thread_reply_generator import BookThreadReplyGenerator
from generator.module.user.user_generator import UserGenerator
from generator.module.user.user_social_generator import UserSocialGenerator
from faker import Faker
import logging

logging.basicConfig(
    level = logging.INFO,
    format = '%(asctime)s - %(levelname)s - %(message)s'
)

def main() -> None:
    faker = Faker()
    user_factory = UserFactory(faker)
    book_request_factory = BookRequestFactory()
    book_progress_request_factory = BookProgressRequestFactory()
    book_review_request_factory = BookReviewRequestFactory(faker)
    book_thread_request_factory = BookThreadRequestFactory(faker)
    book_thread_reply_request_factory = BookThreadReplyRequestFactory(faker)

    auth_manager = KeycloakAuthManager()
    keycloak_manager = KeycloakManager(user_factory)

    user_generator = UserGenerator(user_factory, auth_manager, keycloak_manager)
    user_social_generator = UserSocialGenerator(auth_manager)
    book_generator = BookGenerator(book_request_factory, auth_manager)
    book_progress_generator = BookProgressGenerator(book_progress_request_factory, auth_manager)
    book_review_generator = BookReviewGenerator(book_review_request_factory, auth_manager)
    book_thread_generator = BookThreadGenerator(book_thread_request_factory, auth_manager)
    book_thread_reply_generator = BookThreadReplyGenerator(book_thread_reply_request_factory, auth_manager)

    user_generator.generate()
    user_social_generator.generate()
    book_generator.generate()
    book_review_generator.generate()
    book_thread_generator.generate()
    book_progress_generator.generate()
    book_thread_reply_generator.generate()

if __name__ == '__main__':
    main()
