# HelloTj

## How to build
```sh
cd soy && make
./mvnw package --update-snapshots
```

## How to run locally
```sh
# Tab 1
docker run -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword postgres

# Tab 2
pgcli postgres://postgres:mysecretpassword@localhost:5432/postgres
# postgres@localhost:postgres> \i migration.sql
export GOOG_CREDS=$(heroku config:get GOOG_CREDS -a your-app-name)
java -jar target/hello-1.0-SNAPSHOT.jar server config.yml
```

Then open http://localhost:8080

