CREATE TABLE edital
(
    id                 UUID PRIMARY KEY,
    titulo             VARCHAR(255) NOT NULL,
    numero_edital      VARCHAR(50),
    descricao          TEXT         NOT NULL,
    status             VARCHAR(30)  NOT NULL DEFAULT 'ABERTO',
    categoria          VARCHAR(50)  NOT NULL,
    arquivo_path       VARCHAR(255) NOT NULL,
    nome_original      VARCHAR(255),
    content_type       VARCHAR(100),
    tamanho_bytes      BIGINT,
    data_publicacao    DATE         NOT NULL,
    data_encerramento  DATE,
    link_inscricao     VARCHAR(500),
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE edital_timeline
(
    id                 UUID PRIMARY KEY,
    edital_id          UUID         NOT NULL REFERENCES edital(id) ON DELETE CASCADE,
    titulo             VARCHAR(255) NOT NULL,
    tipo               VARCHAR(40)  NOT NULL,
    descricao          TEXT,
    data_evento        DATE         NOT NULL,
    arquivo_path       VARCHAR(255),
    nome_original      VARCHAR(255),
    content_type       VARCHAR(100),
    tamanho_bytes      BIGINT,
    link_externo       VARCHAR(500),
    destaque           BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_edital_status ON edital (status);
CREATE INDEX idx_edital_data_publicacao ON edital (data_publicacao DESC);
CREATE INDEX idx_edital_timeline_edital_id ON edital_timeline (edital_id);
CREATE INDEX idx_edital_timeline_data ON edital_timeline (data_evento DESC);
