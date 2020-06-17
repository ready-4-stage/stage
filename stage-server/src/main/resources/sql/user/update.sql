UPDATE USERS
SET USERNAME = ?,
    PWD      = ?,
    MAIL     = ?,
    ROLES_ID = (SELECT ID
                FROM ROLES
                WHERE UPPER(DESCRIPTION) = ?)
WHERE ID = ?;

COMMIT;
