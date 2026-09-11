ALTER TABLE system_configuration ALTER COLUMN value TYPE TEXT;
ALTER TABLE system_configuration ALTER COLUMN description TYPE TEXT;

INSERT INTO system_configuration (id, key, value, description, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'DESCRIPTION_TEAM',
    'Somos a empresa júnior de Zootecnia do IFMA Campus Caxias, fundada em 2015 como associação civil educacional. Gerida por estudantes com apoio de docentes e técnicos, atuamos como ponte estratégica entre a universidade e o produtor rural, oferecendo consultorias que promovem inovação, sustentabilidade e fortalecimento do agronegócio, contribuindo para o desenvolvimento do setor e a formação de profissionais preparados para os desafios do campo.',
    'Descrição institucional da empresa exibida na seção Sobre Nós',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (key) DO UPDATE
SET value = EXCLUDED.value,
    description = EXCLUDED.description,
    updated_at = CURRENT_TIMESTAMP;
