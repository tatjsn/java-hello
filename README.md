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
export AUTH_DOMAIN=$(heroku config:get AUTH_DOMAIN -a your-app-name)
export AUTH_CLIENT_ID=$(heroku config:get AUTH_CLIENT_ID -a your-app-name)
export AUTH_CLIENT_SECRET=$(heroku config:get AUTH_CLIENT_SECRET -a your-app-name)
java -jar target/hello-1.0-SNAPSHOT.jar server config.yml
```

Then open http://localhost:8080

