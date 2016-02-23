Database connection defined at `src/main/resources/META-INF/persistence.xml`.

Run `main.Main` Java class, it will insert tons of records into the database
using schema per tenant multitenancy type.

A simple watch on Java VisualVM shows that heap usage goes up indefinitely.

