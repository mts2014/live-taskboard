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

create table widgets (
  category_id varchar(100) not null,
  widget_id varchar(100) not null,
  x integer not null,
  y integer not null,
  width integer not null,
  height integer not null,

  constraint uc_widget_key unique (category_id, widget_id)
);
