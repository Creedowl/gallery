# Gallery

采用`spring boot`开发的简易图床网站，设计模式的课程设计

前端地址 [gallery-frontend](https://github.com/Creedowl/gallery-frontend)

## 技术栈

- Spring Boot
- Spring Security
- Mybatis Plus
- jwt
- Postgres
- SpringDoc (swagger)

## sql

```sql
create table public."user"
(
	id serial not null
		constraint user_pkey
			primary key,
	username text not null,
	password text not null,
	is_admin boolean default false,
	locked boolean default false
);

create table public.upload_file
(
	id serial not null
		constraint upload_file_pkey
			primary key,
	user_id integer not null,
	count integer not null,
	origin_filename text not null,
	filename text not null,
	type integer not null,
	deleted boolean default false
);
```