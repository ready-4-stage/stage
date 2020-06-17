SELECT USERS.ID,
       USERNAME,
       PWD,
       MAIL,
       DESCRIPTION AS ROLE_DESCRIPTION,
       LAST_NAME,
       FIRST_NAME,
       PLACE_OF_BIRTH,
       PHONE,
       ADDRESS,
       IBAN,
       BIRTHDAY
FROM STUDENT
         INNER JOIN USERS ON STUDENT.ID = USERS.ID
         INNER JOIN ROLES ON USERS.ROLES_ID = ROLES.ID
WHERE USERS.ID = ?;

COMMIT;
