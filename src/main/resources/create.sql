CREATE SEQUENCE user_id_seq;
CREATE TABLE users
(
    id       BIGINT PRIMARY KEY DEFAULT NEXTVAL('user_id_seq'),
    email    VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255)  NOT NULL,
    enabled  BOOLEAN            DEFAULT FALSE
);

CREATE SEQUENCE confirmation_token_id_seq;
CREATE TABLE confirmation_tokens
(
    id           BIGINT PRIMARY KEY DEFAULT NEXTVAL('confirmation_token_id_seq'),
    user_id      BIGINT NOT NULL REFERENCES users (id),
    token        VARCHAR(50),
    created_date TIMESTAMP          DEFAULT CURRENT_TIMESTAMP,
    expiry_date  TIMESTAMP
);