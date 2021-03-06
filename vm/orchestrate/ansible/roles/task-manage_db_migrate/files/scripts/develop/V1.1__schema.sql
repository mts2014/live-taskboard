create table events (
  type varchar(100),
  publisher varchar(100),
  occurred timestamp,
  body bytea
);

create table event_processes (
  type varchar(100) unique,
  last_event_id integer
);

create table event_tracks (
  category varchar(50),
  key varchar(100),
  occurred timestamp,
  state varchar(10),

  constraint uc_track_key unique (category, key)
);

create table groups (
  group_id varchar(100) unique not null,
  owner_member_id varchar(100) not null,
  name varchar(100) not null,
  description varchar(100)
);

create table members (
  member_id varchar(100) unique not null,
  name varchar(100) not null,
  type varchar(10) not null,
  email varchar(100)
);

create table groups_members (
  group_id varchar(100) not null,
  member_id varchar(100) not null,
  admin boolean not null,
  constraint uc_groups_members unique (group_id, member_id)
);

create table group_joins (
  application_id varchar(100) unique not null,
  applicant_id varchar(100) not null,
  group_id varchar(100) not null,
  status varchar(10) not null,
  applied_time timestamp not null
);

create table tasks (
  task_id varchar(100) not null,
  group_id varchar(100) not null,
  status varchar(10) not null,
  name varchar(100) not null,
  deadline date,
  assigned varchar(100),
  memo varchar(1000),
  constraint uc_tasks unique (task_id, group_id)
);

create table task_id_gen (
  group_id varchar(100) unique not null,
  task_id_num integer
);


