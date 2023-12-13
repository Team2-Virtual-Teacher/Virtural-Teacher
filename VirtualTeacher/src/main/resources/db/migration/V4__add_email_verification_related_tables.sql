use `virtual_teacher`;


CREATE TABLE tokens
(
    id              int auto_increment
    primary key,
    token           varchar(500)  not null,
    created_at        datetime  not null,
    expires_at      datetime  not null,
    confirmed_at       datetime  ,
    user_email         varchar(50)          not null,
    constraint tokens_users_fk
    foreign key (user_email) references users (email) on delete cascade
);

ALTER TABLE users
ADd COLUMN is_verified boolean NOT NULL DEFAULT false;

UPDATE users
SET users.is_verified = true
WHERE users.is_verified = false;