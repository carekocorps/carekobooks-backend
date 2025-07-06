from datetime import datetime, timezone
from sqlalchemy import Column, String, Integer, DateTime
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import declarative_base
import uuid

Base = declarative_base()

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key = True, autoincrement = True)
    keycloak_id = Column(UUID(as_uuid = True), unique = True, nullable = False, default = uuid.uuid4)
    username = Column(String(50), unique = True, nullable = False)
    display_name = Column(String(50), nullable = True)
    description = Column(String(1000), nullable = True)
    created_at = Column(DateTime(timezone = True), default = lambda: datetime.now(timezone.utc))
    updated_at = Column(DateTime(timezone = True), default = lambda: datetime.now(timezone.utc), onupdate = lambda: datetime.now(timezone.utc))
