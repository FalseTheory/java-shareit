CREATE TABLE IF NOT EXISTS users (
	id serial NOT NULL UNIQUE,
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS items (
	id serial NOT NULL UNIQUE,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	available boolean NOT NULL,
	owner_id bigint NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS comments (
	id serial NOT NULL UNIQUE,
	item_id bigint NOT NULL,
	text varchar(255) NOT NULL,
	created_at timestamp without time zone NOT NULL,
	author_id bigint NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS bookings (
	id serial NOT NULL UNIQUE,
	"start_time" timestamp without time zone NOT NULL,
	"end_time" timestamp without time zone NOT NULL,
	status varchar(30) NOT NULL,
	booker_id bigint NOT NULL,
	item_id bigint NOT NULL,
	PRIMARY KEY (id)
);


ALTER TABLE items ADD CONSTRAINT "items_fk4" FOREIGN KEY ("owner_id") REFERENCES "users"("id");
ALTER TABLE comments ADD CONSTRAINT "comments_fk1" FOREIGN KEY ("item_id") REFERENCES "items"("id");

ALTER TABLE comments ADD CONSTRAINT "comments_fk3" FOREIGN KEY ("author_id") REFERENCES "users"("id");
ALTER TABLE bookings ADD CONSTRAINT "bookings_fk4" FOREIGN KEY ("booker_id") REFERENCES "users"("id");

ALTER TABLE bookings ADD CONSTRAINT "bookings_fk5" FOREIGN KEY ("item_id") REFERENCES "items"("id");
