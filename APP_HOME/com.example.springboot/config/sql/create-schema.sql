DROP TABLE user IF EXISTS;

CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(15),
  password VARCHAR(50),
  last_name VARCHAR(15),
  first_name VARCHAR(30),
  creator_id BIGINT REFERENCES public.user(id),
  create_date TIMESTAMP,
  updater_id BIGINT REFERENCES public.user(id),
  update_date TIMESTAMP
);

DROP TABLE role IF EXISTS;

CREATE TABLE role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(25),
  name VARCHAR(25),
  description VARCHAR(255),
  creator_id BIGINT REFERENCES public.user(id),
  create_date TIMESTAMP,
  updater_id BIGINT REFERENCES public.user(id),
  update_date TIMESTAMP
);

ALTER TABLE user ADD role_id BIGINT AFTER first_name;

ALTER TABLE user ADD CONSTRAINT role_id_fk foreign key (role_id) REFERENCES public.role(id);

