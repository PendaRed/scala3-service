## How to do an Rest service in Scala 3?  Jan 2022

For starters, we are sticking with Cats and http4s, as a brief glance at Zio doesn't show some step change in simplicity which is what I was hoping for.

I do like swagger for local testing, so I won't use Rho (scala 3 support missing as of now anyway), but will use Tapir.

### How to run it

FirstSvcMain -DDB_PASSWORD=xxxx

Then you can browse to http://localhost:8081/docs/ and you will see the swagger end point.

### Tech

- Scala 3 (for logic)
- Cats (for functional)
- FS2 (for streaming)
- Http4s (for rest etc)
- jdbc (for db, don't like the scala database libs - Slick, Dooble, Quill, whatever)
- Tapir (for swagger)
- sbt (for build)

### Design
All comms with the user has its own DTO's focused on the client.
All comms with the DB has its own DTO's focused on the schema.

The Web layer knows all about effects and cats, but the business logic and database remain unpolluted by that stuff.
Which means I can reuse it in a couple of years when Scala moves off these crazy libraries (I hope).

### Schema

While I will try and use amazon RDS eventually, the initial code runs against an old Postgres DB I have.

So
```sql
CREATE TABLE public.game_army_design (
	id serial NOT NULL,
	json text NULL,
	status text NULL,
	"version" int4 NULL,
	created_at timestamp NULL,
	created_by text NULL,
	updated_at timestamp NULL,
	updated_by text NULL
);
```

### Testing

Well, this has been hacked out in spare seconds, so none.

### Build

Is SBT mutli-project.  No need for multi-project but it is useful copy paste for me.