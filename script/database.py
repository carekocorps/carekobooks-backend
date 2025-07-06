from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, scoped_session
from config import DatabaseConfig

DATABASE_URL = f'postgresql://{DatabaseConfig.USERNAME}:{DatabaseConfig.PASSWORD}@{DatabaseConfig.HOST}:{DatabaseConfig.PORT}/{DatabaseConfig.NAME}'
engine = create_engine(DATABASE_URL, echo = True)
SessionLocal = scoped_session(sessionmaker(bind = engine, autocommit = False, autoflush = False))
