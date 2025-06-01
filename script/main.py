from common.factory.book_factory import BookFactory
from common.factory.book_review_factory import BookReviewFactory
from common.factory.book_thread_factory import BookThreadFactory
from common.factory.book_thread_reply_factory import BookThreadReplyFactory
from common.factory.image_factory import ImageFactory
from common.factory.user_factory import UserFactory
from common.manager.mail_manager import MailSlurpManager
from generator.module.book.book_generator import BookGenerator
from generator.module.book.book_progress_generator import BookProgressGenerator
from generator.module.book.book_review_generator import BookReviewGenerator
from generator.module.book.book_thread_generator import BookThreadGenerator
from generator.module.book.book_thread_reply_generator import BookThreadReplyGenerator
from generator.module.user.user_generator import UserGenerator
from config import Config
from dotenv import load_dotenv
from faker import Faker
import os

load_dotenv()

def load_config() -> Config:
    return Config(
        auth_url = os.getenv('AUTH_URL'),
        auth_username = os.getenv('AUTH_USERNAME'),
        auth_password = os.getenv('AUTH_PASSWORD'),
        book_provider_url = os.getenv('BOOK_PROVIDER_URL'),
        book_genre_provider_url = os.getenv('BOOK_GENRE_PROVIDER_URL'),
        book_progress_provider_url = os.getenv('BOOK_PROGRESS_PROVIDER_URL'),
        book_thread_provider_url = os.getenv('BOOK_THREAD_PROVIDER_URL'),
        book_thread_reply_provider_url = os.getenv('BOOK_THREAD_REPLY_PROVIDER_URL'),
        book_review_provider_url = os.getenv('BOOK_REVIEW_PROVIDER_URL'),
        user_provider_url = os.getenv('USER_PROVIDER_URL'),
        user_signup_url = os.getenv('USER_PROVIDER_URL'),
        user_otp_verify_url = os.getenv('USER_OTP_VERIFY_URL'),
        mailslurp_api_key = os.getenv('MAILSLURP_API_KEY')
    )

def main() -> None:
    faker = Faker()
    image_factory = ImageFactory(faker)
    book_factory = BookFactory(image_factory)
    book_review_factory = BookReviewFactory(faker)
    book_thread_factory = BookThreadFactory(faker)
    book_thread_reply_factory = BookThreadReplyFactory(faker)
    user_factory = UserFactory(faker)

    config = load_config()
    mail_manager = MailSlurpManager(config)
    user_generator = UserGenerator(mail_manager, image_factory, user_factory, config)
    book_generator = BookGenerator(book_factory, config)
    book_progress_generator = BookProgressGenerator(config)
    book_review_generator = BookReviewGenerator(book_review_factory, config)
    book_thread_generator = BookThreadGenerator(book_thread_factory, config)
    book_thread_reply_generator = BookThreadReplyGenerator(book_thread_reply_factory, config)

    user_generator.generate()
    book_generator.generate()
    book_progress_generator.generate()
    book_review_generator.generate()
    book_thread_generator.generate()
    book_thread_reply_generator.generate()

if __name__ == '__main__':
    main()
