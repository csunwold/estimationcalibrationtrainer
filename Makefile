all: clean build

build:
	mvn package

clean:
	mvn clean

run:
	java -jar cli/target/cli-1.0-SNAPSHOT-jar-with-dependencies.jar