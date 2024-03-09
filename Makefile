compile: # compile project
	@./mvnw clean compile test-compile


package:
	@./mvnw package -DskipTests

start-api:
	@./mvnw clean spring-boot:run

start-jar: package
	@java -jar ./target/demo-codigo-*.jar

# Tests

unit-test:
	@./mvnw test

integration-test:
	@./mvnw test -P integration-test

test: unit-test integration-test

report-allure: unit-test
	@./mvnw allure:serve