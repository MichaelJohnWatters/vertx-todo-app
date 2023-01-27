-- Creation of product table
CREATE TABLE IF NOT EXISTS todo (
                                     todo_id INT NOT NULL,
                                     user_ud INT NOT NULL,
                                     name varchar(250) NOT NULL,
                                     content varchar(250) NOT NULL,
  PRIMARY KEY (product_id)
  );

CREATE TABLE IF NOT EXISTS user (
                                     user_ud INT NOT NULL,
                                     name varchar(250) NOT NULL
  PRIMARY KEY (product_id)
  );
