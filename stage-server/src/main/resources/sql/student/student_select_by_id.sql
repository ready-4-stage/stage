SELECT USERS.ID,
       USERNAME,
       PASSWORD,
       MAIL,
       ROLE_ID,
       LAST_NAME,
       FIRST_NAME,
       PLACE_OF_BIRTH,
       PHONE,
       ADDRESS,
       IBAN,
       BIRTHDAY
FROM STUDENT
         INNER JOIN USERS ON STUDENT.ID = USERS.ID
WHERE USERS.ID = ?;