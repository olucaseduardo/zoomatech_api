CREATE TABLE evento
(
    id          UUID PRIMARY KEY,
    photo       VARCHAR(255),
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    categoria   VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
