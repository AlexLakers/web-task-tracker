CREATE USER taskmanageradmin WITH ENCRIPTED PASSWORD 'taskManagerAdmin';

CREATE DATABASE task_manager_repository OWNER taskmanageradmin;


CREATE TABLE IF NOT EXISTS account
(
    birthday   DATE,
    id         BIGINT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    login      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    createdat  TIMESTAMP(6) WITH TIME ZONE,
                                updatedat  TIMESTAMP(6) WITH TIME ZONE
                                );


CREATE TABLE account_role
(
    role_id  INT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    account_id bigint  NOT NULL REFERENCES  account ON DELETE CASCADE
);

ALTER TABLE account_role OWNER TO taskmanageradmin;


CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE CHECK (name IN ('ADMIN','TEST','USER')),
    createdat TIMESTAMP(6) WITHTIME ZONE,
    updatedat TIMESTAMP(6) WITH TIME ZONE
                               );

ALTER TABLE role OWNER TO taskmanageradmin;


CREATE TABLE IF NOT EXISTS role_permissions(
                                               role_id INTEGER REFERENCES role(id),
    permission VARCHAR(64),
    PRIMARY KEY (role_id,permission)
    );

ALTER TABLE IF NOT EXISTS role_permissions OWNER TO taskmanageradmin;


CREATE TABLE task
(
    id   BIGSERIAL PRIMARY KEY
        creation_date DATE NOT NULL,
    deadline_date DATE NOT NULL,
    customer_id   BIGINT NOT NULL REFERENCES account(id),
    performer_id  BIGINT NOT NULL REFERENCES account(id),
    description   VARCHAR(1024) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    priority      VARCHAR(255) NOT NULL UNIQUE CHECK (name IN ('LOW','MDM','HGH')),
    status        VARCHAR(255) NOT NULL UNIQUE CHECK (name IN ('PROGRESS','PERFORMED','CANCELLED')),
    createdat TIMESTAMP(6) WITHTIME ZONE,
    updatedat TIMESTAMP(6) WITH TIME ZONE
        number        INT
);

ALTER TABLE IF NOT EXISTS task OWNER TO taskmanageradmin;
