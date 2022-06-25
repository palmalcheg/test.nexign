
	CREATE TABLE IF NOT EXISTS PARTICIPANT (
	    id SERIAL PRIMARY KEY,
	    name TEXT NOT NULL,
	    city TEXT NOT NULL,
	    age  integer NOT NULL
	);
	
	CREATE TABLE IF NOT EXISTS WINNER (
	    id SERIAL PRIMARY KEY,
	    name TEXT NOT NULL,
	    city TEXT NOT NULL,
	    age  integer NOT NULL,
	    amount integer NOT NULL
	);
	

