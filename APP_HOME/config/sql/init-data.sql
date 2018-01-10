
insert into role(id, code, name, description, create_date) values (1, 'ADMIN', 'Admin', 'Admin User', CURRENT_DATE());
insert into role(id, code, name, description, create_date) values (2, 'SUPER_ADMIN', 'Super Admin', 'Super Admin User', CURRENT_DATE());

insert into user(id, username, password, last_name, first_name, role_id, create_date) values (1, 'admin', '123', 'Leang', 'Sotheara', 1, CURRENT_DATE());
insert into user(username, password, last_name, first_name, role_id, creator_id, create_date) values ('sok', 'sok', 'Ly', 'Sok', 1, 1, CURRENT_DATE());
insert into user(username, password, last_name, first_name, role_id, creator_id, create_date) values ('vathna', '123', 'Lay', 'Vathana', 1, 1, CURRENT_DATE());
insert into user(username, password, last_name, first_name, role_id, creator_id, create_date) values ('sovan', 'dara', 'Sok', 'Sovannara', 1, 1, CURRENT_DATE());
insert into user(username, password, last_name, first_name, role_id, creator_id, create_date) values ('sinara', 'dara', 'Lay', 'Sinara', 1, 1, CURRENT_DATE());
