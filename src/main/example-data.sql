TRUNCATE TABLE game_genres CASCADE;
TRUNCATE TABLE game CASCADE;

-- Insert some base rows in "game" table
INSERT INTO game (id, title, release, publisher, description, price)
VALUES
    (1,'The Legend of Zelda', 1986, 'Nintendo', 'Classic NES adventure', 25.50),
    (2,'Final Fantasy', 1987, 'Square', 'A journey of four warriors', 19.99),
    (3,'Super Mario Bros', 1985, 'Nintendo', 'Iconic side-scrolling platformer', 29.99),
    (4,'Monster Hunter: World', 2018, 'Capcom', '', 29.99),
    (5,'Monster Hunter: Wilds', 2025, 'Capcom', '', 69.99),
    (6,'Okami', 2017, 'Capcom', '', 9.99),
    (7,'Elden Ring', 2022, 'FromSoftware', '', 35.99),
    (8,'The Sims 2', 2004, 'EA', '', 19.99),
    (9,'The Sims 3', 2009, 'EA', '', 19.99);

-- Insert genres for each game in "game_genres"
INSERT INTO game_genres (game_id, genres)
VALUES
    (1, 'Adventure'),
    (1, 'Action'),
    (2, 'RPG'),
    (3, 'Platformer'),
    (4, 'Adventure'),
    (4, 'RPG'),
    (5, 'Adventure'),
    (5, 'RPG'),
    (6, 'Action'),
    (6, 'Adventure'),
    (7, 'RPG'),
    (7, 'Action'),
    (8, 'Social simulation'),
    (9, 'Social simulation');