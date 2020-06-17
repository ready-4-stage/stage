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
