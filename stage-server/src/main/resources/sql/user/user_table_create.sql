CREATE TABLE IF NOT EXISTS USER
(
    ID       INT  NOT NULL,
    USERNAME TEXT NOT NULL,
    PASSWORD INT  NOT NULL,
    MAIL     TEXT NOT NULL,
    ROLE_ID  INT  NOT NULL,

    PRIMARY KEY (ID),
    FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID)
);
