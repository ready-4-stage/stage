UPDATE USERS
SET USERNAME = ?,
    PWD      = ?,
    MAIL     = ?,
    ROLES_ID = ?
WHERE ID = ?;

COMMIT;
