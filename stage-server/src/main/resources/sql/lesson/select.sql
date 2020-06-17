SELECT LESSON.ID               AS LESSON_ID,      -- Lesson
       BEGIN,
       END,
       CONTENT,

       ROOM_ID,                                   -- Room
       ROOM.NAME               AS ROOM_NAME,
       ROOM.SUITABILITY        AS ROOM_SUITABILITY,

       LESSON_TAPE.ID          AS LESSON_TYPE_ID, -- LessonType
       LESSON_TYPE.DESCRIPTION AS LESSON_TYPE_DESCRIPTION,

       USERS.ID                AS USER_ID,        -- Teacher
       USERNAME,
       PWD,
       MAIL,
       ROLES.ID                AS ROLE_ID,
       ROLES.DESCRIPTION       AS ROLE_DESCRIPTION,
       LAST_NAME,
       FIRST_NAME,
       PLACE_OF_BIRTH,
       PHONE,
       ADDRESS,
       IBAN,
       BIRTHDAY,
       HOURLY_RATE
FROM LESSON
         INNER JOIN ROOM ON ROOM.ID = LESSON.ROOM_ID
         INNER JOIN TEACHER ON TEACHER.ID = LESSON.TEACHER_ID
         INNER JOIN LESSON_TYPE ON LESSON_TYPE.ID = LESSON.LESSON_TYPE_ID
         INNER JOIN USERS ON STUDENT.ID = USERS.ID
         INNER JOIN ROLES ON USERS.ROLES_ID = ROLES.ID;
COMMIT;
