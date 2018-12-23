CREATE TABLE IF NOT EXISTS users (
  id serial PRIMARY KEY,
  username VARCHAR (50) UNIQUE NOT NULL,
  password VARCHAR (100) NOT NULL,
  email VARCHAR (50) NOT NULL,
  role VARCHAR (50) NOT NULL
);

CREATE TABLE IF NOT EXISTS secret_santa (
  id serial PRIMARY KEY,
  user_id INTEGER REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS participant (
  id serial PRIMARY KEY,
  santa_secret_id INTEGER  REFERENCES  secret_santa(id),
  name VARCHAR (50) NOT NULL,
  email VARCHAR(50) NOT NULL
);