DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    user_id    bigint auto_increment primary key,
    email      varchar(30)  not null,
    nickname   varchar(30)  not null,
    password   varchar(255) not null,
    created_at datetime     not null,
    updated_at datetime     not null,
    last_login_at datetime  null,

    UNIQUE INDEX idx_user_email(email)
);