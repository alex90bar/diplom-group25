create
user "post-service";
alter
user "post-service" with PASSWORD 'post-service';
create schema "post";
alter
schema "post" owner to "post-service";
