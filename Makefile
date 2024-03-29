package:
	mvn package

run:
	mvn spring-boot:run

docker-build:
	DOCKER_BUILDKIT=1 docker build . -t image-processing-api

docker-run:
	docker run -p 8080:8080 image-processing-api

update:
	mvn versions:use-latest-releases

clean:
	mvn clean