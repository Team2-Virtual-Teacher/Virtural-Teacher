create table roles
(
    id   int auto_increment
        primary key,
    role varchar(32) not null
);

create table topics
(
    id    int auto_increment
        primary key,
    topic varchar(32) not null
);

create table courses
(
    id         int auto_increment
        primary key,
    title      varchar(50) not null,
    topic      int         not null,
    start_date datetime    not null,
    constraint courses_topics_fk
        foreign key (topic) references topics (id)
);

create table course_description
(
    id          int           not null,
    description varchar(1000) not null,
    constraint course_description_course_description_fk
        foreign key (id) references courses (id)
);

create table lectures
(
    id         int auto_increment
        primary key,
    title      varchar(50)  not null,
    video_url  varchar(100) not null,
    assignment varchar(100) null,
    course_id  int          not null,
    constraint lectures_courses_id_fk
        foreign key (course_id) references courses (id)
);

create table users
(
    id         int auto_increment
        primary key,
    email      varchar(50) not null,
    password   varchar(64) not null,
    first_name varchar(50) not null,
    last_name  varchar(50) not null,
    role_id    int         not null,
    constraint users_roles_fk
        foreign key (role_id) references roles (id)
);

create table course_user
(
    course_id int not null,
    user_id   int not null,
    constraint course_user_courses_id_fk
        foreign key (course_id) references courses (id),
    constraint course_user_users_id_fk
        foreign key (user_id) references users (id)
);

