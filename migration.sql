create table admin (id serial primary key, hash varchar);
create table foo (id serial primary key, name varchar);
alter table foo add column image varchar;
insert into foo (name, image) values ('You', 'https://i.imgur.com/Vxx2ZJZm.jpeg');
