CREATE DATABASE IF NOT EXISTS todo;
USE todo;
CREATE TABLE IF NOT EXISTS users (
                                  user_id INT NOT NULL AUTO_INCREMENT,
                                  name varchar(250) NOT NULL,
  PRIMARY KEY (user_id)
  );

CREATE TABLE IF NOT EXISTS todos (
                                  todo_id INT NOT NULL AUTO_INCREMENT,
                                  user_id INT NOT NULL,
                                  name varchar(250) NOT NULL,
                                  content varchar(250) NOT NULL,
  PRIMARY KEY (todo_id)
  );

INSERT INTO todos (user_id, name, content)
VALUES (123, "First Todo", "Make sure to add more todos");
