CREATE TABLE IF NOT EXISTS arquivos_upload (
    id varchar(255) PRIMARY KEY,
    nome_arquivo varchar(255) NOT NULL,
    tipo_conteudo varchar(255) NOT NULL,
    categoria varchar(255) NOT NULL,
    conteudo bytea NOT NULL,
    data_upload timestamp NOT NULL
);
