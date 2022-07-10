create table users (
  id                    bigserial primary key,
  username              varchar(200) not null,
  password              varchar(200) not null,
  status                varchar(200)
);

insert into users (username, password, status)
values
('user', '$2a$12$IeSISPXHcU5E0.NRhubPuOYB9xldysjuvDqS0zQjuDQLTJgAPXQmq', 'ACTIVE');


create table kind (
    id                      bigserial primary key,
    name                    varchar(255)
);

create table animal (
    id                      bigserial primary key,
    kind_id                 bigint references kind (id),
    birthday                date,
    gender                  varchar(1),
    nickname                varchar(200)
);

create table animal_user (
    id                      bigserial primary key,
    animal_id               bigint references animal (id),
    user_id                 bigint references users (id)
);


insert into kind (name)
values
('Cat'),
('Dog'),
('Bird');
--
--insert into products (title, price, category_id)
--values
--('Bread', 1, 1),
--('Samsung V100', 2, 3),
--('Acer X1000', 3, 2);
