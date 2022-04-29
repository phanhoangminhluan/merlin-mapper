mvn clean install -o

mvn deploy:deploy-file \
-Durl=file://deployed-jar \
-Dfile=./target/merlin-mapper-1.0-SNAPSHOT.jar \
-DgroupId=com.merlin \
-DartifactId=merlin-mapper \
-Dpackaging=jar \
-Dversion=1.0-SNAPSHOT
