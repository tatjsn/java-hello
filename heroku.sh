mkdir app
cd app
cp ../target/hello-1.0-SNAPSHOT.jar .
echo \# Download java binary
curl -LO https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.5%2B10/OpenJDK11U-jdk_x64_linux_hotspot_11.0.5_10.tar.gz
tar xzvf OpenJDK11U-jdk_x64_linux_hotspot_11.0.5_10.tar.gz
rm OpenJDK11U-jdk_x64_linux_hotspot_11.0.5_10.tar.gz
echo \# Debug: Show files
ls -la
cd ..
echo \# Create slug.tgz
tar czfv slug.tgz ./app

curl -X POST \
-H "Authorization: Bearer $HEROKU_API_KEY" \
-H 'Content-Type: application/json' \
-H 'Accept: application/vnd.heroku+json; version=3' \
-d '{"process_types":{"web":"./jdk-11.0.5+10/bin/java -Ddw.server.applicationConnectors[0].port=$PORT -Ddw.database.user=$JDBC_DATABASE_USERNAME -Ddw.database.password=$JDBC_DATABASE_PASSWORD -Ddw.database.url=$JDBC_DATABASE_URL -jar hello-1.0-SNAPSHOT.jar server config.yml"}}' \
https://api.heroku.com/apps/tj-hello/slugs > res.json

SLUG_URL=$(cat res.json | jq -r .blob.url)
SLUG_ID=$(cat res.json | jq -r .id)

curl -X PUT \
-H "Content-Type:" \
--data-binary @slug.tgz $SLUG_URL

curl -X POST \
-H "Authorization: Bearer $HEROKU_API_KEY" \
-H "Accept: application/vnd.heroku+json; version=3" \
-H "Content-Type: application/json" \
-d '{"slug":"'$SLUG_ID'"}' \
-n https://api.heroku.com/apps/tj-hello/releases
