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
    id          int auto_increment
        primary key,
    email       varchar(50)  not null,
    password    varchar(500) not null,
    first_name  varchar(50)  not null,
    last_name   varchar(50)  not null,
    role_id     int          not null,
    picture_url varchar(500) not null,
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
            on delete cascade
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
            on delete cascade
);

create table lectures
(
    id             int auto_increment
        primary key,
    title          varchar(50)  not null,
    video_url      varchar(500) not null,
    assignment_url varchar(500) null,
    course_id      int          not null,
    constraint lectures_courses_id_fk
        foreign key (course_id) references courses (id)
            on delete cascade
);

create table lecture_description
(
    lecture_id  int           not null,
    description varchar(1000) not null,
    constraint lecture_description_pk
        unique (lecture_id),
    constraint lecture_description_lectures_fk
        foreign key (lecture_id) references lectures (id)
            on delete cascade
);

create table ratings
(
    rating    decimal      not null,
    comment   varchar(300) not null,
    user_id   int          not null,
    course_id int          not null,
    id        int auto_increment
        primary key,
    constraint ratings_courses_id_fk
        foreign key (course_id) references courses (id)
            on delete cascade ,
    constraint ratings_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);

create table solutions
(
    solution_url varchar(500)     not null,
    user_id      int              not null,
    lecture_id   int              not null,
    id           int auto_increment
        primary key,
    grade        double default 0 not null,
    constraint solutions_lectures_id_fk
        foreign key (lecture_id) references lectures (id)
            on delete cascade,
    constraint solutions_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);
