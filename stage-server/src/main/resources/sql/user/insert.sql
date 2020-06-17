INSERT INTO USERS (ID, USERNAME, PWD, ROLES_ID, MAIL)
VALUES (?, ?, ?, (
    SELECT ID
    FROM ROLES
    WHERE UPPER(DESCRIPTION) = ?
    ), ?);

COMMIT;
