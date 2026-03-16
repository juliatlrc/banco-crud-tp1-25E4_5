-- ============================================================
-- Rick & Morty API — DDL + dados iniciais
-- Executado automaticamente na PRIMEIRA inicialização do banco
-- ============================================================

-- Tabela de personagens
CREATE TABLE IF NOT EXISTS characters (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    status     VARCHAR(50),
    species    VARCHAR(100),
    gender     VARCHAR(50),
    origin     VARCHAR(200),
    location   VARCHAR(200),
    image      VARCHAR(500),
    created    TIMESTAMP DEFAULT NOW()
);

-- Tabela de episódios
CREATE TABLE IF NOT EXISTS episodes (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(200) NOT NULL,
    air_date     VARCHAR(100),
    episode_code VARCHAR(20)
);

-- Tabela de localizações
CREATE TABLE IF NOT EXISTS locations (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(200) NOT NULL,
    type       VARCHAR(100),
    dimension  VARCHAR(100)
);

-- ============================================================
-- Inserts — personagens
-- ============================================================
INSERT INTO characters (name, status, species, gender, origin, location, image) VALUES
('Rick Sanchez',   'Alive', 'Human',  'Male',   'Earth (C-137)',    'Citadel of Ricks', 'https://rickandmortyapi.com/api/character/avatar/1.jpeg'),
('Morty Smith',    'Alive', 'Human',  'Male',   'unknown',          'Earth (C-137)',     'https://rickandmortyapi.com/api/character/avatar/2.jpeg'),
('Summer Smith',   'Alive', 'Human',  'Female', 'Earth (Replacement Dimension)', 'Earth (Replacement Dimension)', 'https://rickandmortyapi.com/api/character/avatar/3.jpeg'),
('Beth Smith',     'Alive', 'Human',  'Female', 'Earth (Replacement Dimension)', 'Earth (Replacement Dimension)', 'https://rickandmortyapi.com/api/character/avatar/4.jpeg'),
('Jerry Smith',    'Alive', 'Human',  'Male',   'Earth (Replacement Dimension)', 'Earth (Replacement Dimension)', 'https://rickandmortyapi.com/api/character/avatar/5.jpeg'),
('Abadango Cluster Princess', 'Alive', 'Alien', 'Female', 'Abadango', 'Abadango', 'https://rickandmortyapi.com/api/character/avatar/6.jpeg'),
('Abradolf Lincler', 'unknown', 'Human', 'Male', 'Earth (Replacement Dimension)', 'Testicle Monster Dimension', 'https://rickandmortyapi.com/api/character/avatar/7.jpeg'),
('Adjudicator Rick', 'Dead', 'Human', 'Male', 'unknown', 'unknown', 'https://rickandmortyapi.com/api/character/avatar/8.jpeg'),
('Agency Director', 'Dead', 'Human', 'Male', 'Earth (Replacement Dimension)', 'Earth (Replacement Dimension)', 'https://rickandmortyapi.com/api/character/avatar/9.jpeg'),
('Alan Rails',     'Dead',    'Human',  'Male',   'unknown',          'Interdimensional Cable', 'https://rickandmortyapi.com/api/character/avatar/10.jpeg');

-- ============================================================
-- Inserts — episódios
-- ============================================================
INSERT INTO episodes (name, air_date, episode_code) VALUES
('Pilot',                          'December 2, 2013',   'S01E01'),
('Lawnmower Dog',                  'December 9, 2013',   'S01E02'),
('Anatomy Park',                   'December 16, 2013',  'S01E03'),
('M. Night Shaym-Aliens!',         'January 13, 2014',   'S01E04'),
('Meeseeks and Destroy',           'January 20, 2014',   'S01E05'),
('Rick Potion #9',                 'January 27, 2014',   'S01E06'),
('Raising Gazorpazorp',            'March 10, 2014',     'S01E07'),
('Rixty Minutes',                  'March 17, 2014',     'S01E08'),
('Something Ricked This Way Comes','March 24, 2014',     'S01E09'),
('Close Rick-counters of the Rick Kind', 'April 7, 2014','S01E10');

-- ============================================================
-- Inserts — localizações
-- ============================================================
INSERT INTO locations (name, type, dimension) VALUES
('Earth (C-137)',                   'Planet',     'Dimension C-137'),
('Abadango',                        'Cluster',    'unknown'),
('Citadel of Ricks',                'Space station', 'unknown'),
('Worldender''s lair',              'Planet',     'unknown'),
('Anatomy Park',                    'Microverse', 'Dimension C-137'),
('Interdimensional Cable',          'TV',         'unknown'),
('Immortality Field Resort',        'Resort',     'unknown'),
('Post-Apocalyptic Earth',          'Planet',     'Post-Apocalyptic Dimension'),
('Purge Planet',                    'Planet',     'Replacement Dimension'),
('Venzenulon 7',                    'Planet',     'unknown');
