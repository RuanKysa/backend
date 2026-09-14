-- A categoria passou de enum para texto livre no cadastro de oficinas.
-- Mantem os dados existentes e a obrigatoriedade do campo.
ALTER TABLE oficinas DROP CONSTRAINT IF EXISTS oficinas_categoria_check;
