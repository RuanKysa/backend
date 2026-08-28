CREATE TABLE matricula_programas_sociais (
    matricula_id varchar(255) NOT NULL,
    programa_social varchar(30) NOT NULL,
    CONSTRAINT pk_matricula_programas_sociais PRIMARY KEY (matricula_id, programa_social),
    CONSTRAINT fk_programas_sociais_matricula FOREIGN KEY (matricula_id)
        REFERENCES matriculas(id) ON DELETE CASCADE
);

INSERT INTO matricula_programas_sociais (matricula_id, programa_social)
SELECT id, programa_social
FROM matriculas
WHERE programa_social IS NOT NULL;

ALTER TABLE matriculas ADD COLUMN programa_social_outros varchar(255);
