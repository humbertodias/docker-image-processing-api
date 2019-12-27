dep:
	apk --update add fontconfig ttf-dejavu

package:
	mvn package

run:
	mvn spring-boot:run

docker-build:
	docker build . -t image-processing-api

docker-run:
	docker run -p 8080:8080 image-processing-api