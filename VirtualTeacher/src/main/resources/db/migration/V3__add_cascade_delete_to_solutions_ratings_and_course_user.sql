ALTER TABLE course_user
    DROP FOREIGN KEY course_user_users_id_fk;

ALTER TABLE course_user
    ADD CONSTRAINT course_user_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE solutions
    DROP FOREIGN KEY solutions_users_id_fk;

ALTER TABLE solutions
    ADD CONSTRAINT solutions_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE ratings
    DROP FOREIGN KEY ratings_users_id_fk;

ALTER TABLE ratings
    ADD CONSTRAINT ratings_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;