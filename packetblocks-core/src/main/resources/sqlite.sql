CREATE TABLE IF NOT EXISTS block_bindings
(
    world        TEXT    NOT NULL,
    x            REAL    NOT NULL,
    y            REAL    NOT NULL,
    z            REAL    NOT NULL,
    cx           INTEGER NOT NULL,
    cz           INTEGER NOT NULL,
    resource_key TEXT    NOT NULL,
    PRIMARY KEY (world,x,y,z,cx,cz)
);