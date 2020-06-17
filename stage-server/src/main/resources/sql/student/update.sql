UPDATE USERS
SET USERNAME = ?,
    PWD      = ?,
    MAIL     = ?,
    ROLES_ID = (SELECT ID
                FROM ROLES
                WHERE UPPER(DESCRIPTION) = ?)
WHERE ID = ?;

UPDATE STUDENT
SET LAST_NAME      = ?,
    FIRST_NAME     = ?,
    PLACE_OF_BIRTH = ?,
    PHONE          = ?,
    ADDRESS        = ?,
    IBAN           = ?,
    BIRTHDAY       = ?
WHERE ID = ?;

COMMIT;
