use `virtual_teacher`;
/*=========================Create tables===============================*/

create table roles
(
    id   int auto_increment
        primary key,
    role varchar(30) not null
);

create table topics
(
    id    int auto_increment
        primary key,
    topic varchar(32) not null
);

create table users
(
    id              int auto_increment
        primary key,
    email           varchar(50)  not null,
    password        varchar(64)  not null,
    first_name      varchar(50)  not null,
    last_name       varchar(50)  not null,
    role_id         int          not null,
    profile_picture varchar(100) not null,
    constraint users_pk
        unique (email),
    constraint users_roles_fk
        foreign key (role_id) references roles (id)
);

create table courses
(
    id            int auto_increment
        primary key,
    title         varchar(50) not null,
    topic_id      int         not null,
    start_date    datetime    not null,
    creator_id    int         not null,
    is_published  tinyint(1)  not null,
    passing_grade decimal     not null,
    constraint courses_topics_fk
        foreign key (topic_id) references topics (id),
    constraint courses_users_id_fk
        foreign key (creator_id) references users (id)
);

create table course_description
(
    course_id   int           not null,
    description varchar(1000) not null,
    constraint course_description_pk
        unique (course_id),
    constraint course_description_course_description_fk
        foreign key (course_id) references courses (id)
            ON DELETE CASCADE
);

create table course_user
(
    course_id int        not null,
    user_id   int        not null,
    ongoing   tinyint(1) not null,
    constraint course_user_courses_id_fk
        foreign key (course_id) references courses (id),
    constraint course_user_users_id_fk
        foreign key (user_id) references users (id)
);

create table lectures
(
    id              int auto_increment
        primary key,
    title           varchar(50)  not null,
    video_url       varchar(100) not null,
    assignment_task varchar(100) null,
    course_id       int          not null,
    constraint lectures_courses_id_fk
        foreign key (course_id) references courses (id)
            ON DELETE CASCADE
);

create table assignments
(
    assignment_solution varchar(100) not null,
    user_id             int          not null,
    lecture_id          int          not null,
    id                  int          not null
        primary key,
    constraint assignments_lectures_id_fk
        foreign key (lecture_id) references lectures (id),
    constraint assignments_users_id_fk
        foreign key (user_id) references users (id)
);

create table grades
(
    id            int auto_increment
        primary key,
    grade         decimal not null,
    assignment_id int     not null,
    constraint grades_assignments_id_fk
        foreign key (assignment_id) references assignments (id)
);

create table lecture_description
(
    lecture_id  int           not null,
    description varchar(1000) not null,
    constraint lecture_description_pk
        unique (lecture_id),
    constraint lecture_description_lectures_fk
        foreign key (lecture_id) references lectures (id)
            ON DELETE CASCADE
);

create table ratings
(
    rating    decimal      not null,
    comment   varchar(300) not null,
    user_id   int          not null,
    course_id int          not null,
    id        int          not null
        primary key,
    constraint ratings_courses_id_fk
        foreign key (course_id) references courses (id),
    constraint ratings_users_id_fk
        foreign key (user_id) references users (id)
);

