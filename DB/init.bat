@echo off
echo initializing database create...
echo.

set PGPASSWORD=qwerty123
set PGUSER=postgres
set PGDATABASE=hotel_db

echo going DDL-script...
psql -h localhost -U %PGUSER% -d %PGDATABASE% -f init_db.sql

echo  test data...
psql -h localhost -U %PGUSER% -d %PGDATABASE% -f test_data.sql

echo complete!
pause