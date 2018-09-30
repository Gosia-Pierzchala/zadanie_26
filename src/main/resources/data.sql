INSERT INTO USER (username, password, first_name, last_name, enabled) values ('admin', '{noop}admin', 'Jan', 'Kowalski', true);
INSERT INTO USER(username, password, first_name, last_name, enabled) values ('user', '{noop}user', 'Krzysztof', 'Nowak', true);

INSERT INTO USER_ROLE(username, role) values ('admin', 'ROLE_ADMIN');
INSERT INTO USER_ROLE(username, role) values ('user', 'ROLE_USER');