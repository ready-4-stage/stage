INSERT INTO USERS (ID, USERNAME, PWD, ROLES_ID, MAIL)
VALUES (?, ?, ?, (
    SELECT ID
    FROM ROLES
    WHERE UPPER(DESCRIPTION) = ?
    ), ?);

INSERT INTO STUDENT (ID, LAST_NAME, FIRST_NAME, PLACE_OF_BIRTH, PHONE, ADDRESS, IBAN, BIRTHDAY)
VALUES (?, ?, ?, ?, ?, ?, ?, ?);

COMMIT;
