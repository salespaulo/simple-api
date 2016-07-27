-- Create database
CREATE DATABASE simpleapi;

\c simpleapi;

-- Table of users
CREATE TABLE users (
  id    BIGSERIAL PRIMARY KEY NOT NULL,
  email VARCHAR(200) UNIQUE NOT NULL,
  senha VARCHAR(20) NOT NULL CHECK(char_length(senha) > 5),
  age   INT
);

-- Table of user followers
CREATE TABLE user_followers (
  user_id          BIGSERIAL NOT NULL REFERENCES users(id),
  followed_user_id BIGSERIAL NOT NULL REFERENCES users(id),
  PRIMARY KEY (user_id, followed_user_id)
);

