CREATE DATABASE IF NOT EXISTS todo;
USE todo;
CREATE TABLE IF NOT EXISTS users (
                                  user_id INT NOT NULL,
                                  name varchar(250) NOT NULL,
  PRIMARY KEY (user_id)
  );

CREATE TABLE IF NOT EXISTS todos (
                                  todo_id INT NOT NULL,
                                  user_id INT NOT NULL,
                                  name varchar(250) NOT NULL,
                                  content varchar(250) NOT NULL,
  PRIMARY KEY (todo_id)
  );
