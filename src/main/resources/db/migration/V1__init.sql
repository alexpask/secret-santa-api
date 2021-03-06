CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(50) PRIMARY KEY,
  username VARCHAR (50) UNIQUE NOT NULL,
  password VARCHAR (100) NOT NULL,
  email VARCHAR (50) NOT NULL,
  role VARCHAR (50) NOT NULL
);

CREATE TABLE IF NOT EXISTS secret_santa (
  id VARCHAR(50) PRIMARY KEY,
  user_id VARCHAR(50) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS participant (
  id VARCHAR(50) PRIMARY KEY,
  santa_secret_id VARCHAR(50)  REFERENCES  secret_santa(id),
  name VARCHAR (50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  email_verified BOOLEAN NOT NULL,
  email_verification_code VARCHAR(50)
);
