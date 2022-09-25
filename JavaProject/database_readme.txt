How to open database in external tools:

1. Close the model if it is running
2. Open one of software tools supporting connection to HSQLDB database, for example:
   - Execute Query: http://executequery.org
   - SQL Workbench/J: http://www.sql-workbench.net
3. Ensure the tool has hsqldb.jar driver installed (if no, use this one: lib/database/hsqldb.jar)
4. Connect to the database using the following url:

jdbc:hsqldb:file:F:\PortProject\JavaProject\database\db

5. After finished, disconnect the tool and run the model if needed.
