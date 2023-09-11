INSERT INTO users (username, password, enabled, first_name, last_name, email, login_attempts) VALUES ('andres', '$2a$10$2Xrd6dvuzH5VJR0ME8.XjerYm7nTgfO9EV4wZ7qy1GE4FSxSDVzXK', true, 'Andres', 'Guzman', 'profesor@bolsadeideas.com', 0);
INSERT INTO users (username, password, enabled, first_name, last_name, email, login_attempts) VALUES ('jonaeseiza', '$2a$10$M2XWBTFFCKKE1OKHWkbW0ec91PauX0MCecKUNoSMxpCu9mi7cbwIy', true, 'Jonatan', 'Eseiza', 'jona.eseiza@bolsadeideas.com', 0);

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users_roles (user_id, roles_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, roles_id) VALUES (2, 2);
INSERT INTO users_roles (user_id, roles_id) VALUES (2, 1);