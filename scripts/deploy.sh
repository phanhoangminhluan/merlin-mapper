mvn clean install -o

mvn deploy:deploy-file \
-Durl=file:///Users/luan.phm/engineering/Projects/merlin/merlin-mapper/deployed-jar \
-Dfile=./target/merlin-mapper-1.0-SNAPSHOT.jar \
-DgroupId=com.merlin \
-DartifactId=merlin-mapper \
-Dpackaging=jar \
-Dversion=1.0-SNAPSHOT
