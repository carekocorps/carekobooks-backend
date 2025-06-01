class Config:
    def __init__(
        self,
        auth_url: str,
        auth_username: str,
        auth_password: str,
        book_provider_url: str,
        book_genre_provider_url: str,
        book_progress_provider_url: str,
        book_thread_provider_url: str,
        book_thread_reply_provider_url: str,
        book_review_provider_url: str,
        user_provider_url: str,
        user_signup_url: str,
        user_otp_verify_url: str,
        mailslurp_api_key: str
    ):
        self.auth_provider_url = auth_url
        self.auth_username = auth_username
        self.auth_password = auth_password
        self.book_provider_url = book_provider_url
        self.book_genre_provider_url = book_genre_provider_url
        self.book_progress_provider_url = book_progress_provider_url
        self.book_thread_provider_url = book_thread_provider_url
        self.book_thread_reply_provider_url = book_thread_reply_provider_url
        self.book_review_provider_url = book_review_provider_url
        self.user_provider_url = user_provider_url
        self.user_signup_url = user_signup_url
        self.user_otp_verify_url = user_otp_verify_url
        self.mailslurp_api_key = mailslurp_api_key
