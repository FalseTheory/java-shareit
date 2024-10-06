CREATE TABLE IF NOT EXISTS "users" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL,
	"email" varchar(255) NOT NULL UNIQUE,
	PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS "items" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL,
	"description" varchar(255) NOT NULL,
	"available" boolean NOT NULL,
	"ownerId" bigint NOT NULL,
	PRIMARY KEY ("id")
);

ALTER TABLE "items" ADD CONSTRAINT "items_fk4" FOREIGN KEY ("ownerId") REFERENCES "users"("id");
