CREATE TABLE IF NOT EXISTS people
(
    id serial,
    name varchar(250) NOT NULL,
    age  int          NOT NULL,
    PRIMARY KEY (id)
);
