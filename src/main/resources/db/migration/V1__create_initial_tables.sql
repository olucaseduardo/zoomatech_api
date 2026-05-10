-- Create users table
CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL
);

-- Create refresh_tokens table
CREATE TABLE refresh_tokens
(
    id          UUID PRIMARY KEY,
    token       VARCHAR(255)             NOT NULL UNIQUE,
    user_id     UUID                     NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked     BOOLEAN                  NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Create role table (Member Roles)
CREATE TABLE role
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create member table
CREATE TABLE member
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    photo       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    lattes      VARCHAR(255) NOT NULL,
    role_id     UUID         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_member_role FOREIGN KEY (role_id) REFERENCES role (id)
);

-- Create service table
CREATE TABLE service
(
    id          UUID PRIMARY KEY,
    icon        VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create service_topic table
CREATE TABLE service_topic
(
    service_topic_id UUID PRIMARY KEY,
    topic            VARCHAR(255) NOT NULL,
    description      TEXT,
    service_id       UUID         NOT NULL,
    CONSTRAINT fk_topic_service FOREIGN KEY (service_id) REFERENCES service (id) ON DELETE CASCADE
);

-- Create service_topic_items table
CREATE TABLE service_topic_items
(
    service_topic_service_topic_id UUID NOT NULL,
    items                          VARCHAR(255),
    CONSTRAINT fk_items_topic FOREIGN KEY (service_topic_service_topic_id) REFERENCES service_topic (service_topic_id) ON DELETE CASCADE
);

-- Create work_performed table
CREATE TABLE work_performed
(
    id           UUID PRIMARY KEY,
    photo        VARCHAR(255),
    title        VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    performed_at VARCHAR(255) NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create service_performed join table
CREATE TABLE service_performed
(
    work_performed_id UUID NOT NULL,
    service_id        UUID NOT NULL,
    PRIMARY KEY (work_performed_id, service_id),
    CONSTRAINT fk_performed_work FOREIGN KEY (work_performed_id) REFERENCES work_performed (id) ON DELETE CASCADE,
    CONSTRAINT fk_performed_service FOREIGN KEY (service_id) REFERENCES service (id) ON DELETE CASCADE
);

-- Create system_configuration table
CREATE TABLE system_configuration
(
    id          UUID PRIMARY KEY,
    key         VARCHAR(255) NOT NULL UNIQUE,
    value       TEXT         NOT NULL,
    description TEXT         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
