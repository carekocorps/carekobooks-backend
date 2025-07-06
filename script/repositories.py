from database import SessionLocal
from models import User

class UserRepository:
    @staticmethod
    def create(user: User) -> User:
        with SessionLocal() as session:
            with session.begin():
                session.add(user)
                session.flush()
                session.refresh(user)
            return user
