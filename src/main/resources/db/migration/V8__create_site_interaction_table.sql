CREATE TABLE site_interaction
(
    id            UUID PRIMARY KEY,
    tipo          VARCHAR(50)  NOT NULL,
    categoria     VARCHAR(100) NOT NULL,
    acao          VARCHAR(100) NOT NULL,
    rotulo        VARCHAR(255),
    referencia_id VARCHAR(255),
    origem_pagina VARCHAR(255),
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_site_interaction_created_at ON site_interaction(created_at);
CREATE INDEX idx_site_interaction_tipo ON site_interaction(tipo);
CREATE INDEX idx_site_interaction_categoria ON site_interaction(categoria);
CREATE INDEX idx_site_interaction_acao ON site_interaction(acao);
