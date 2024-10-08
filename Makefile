DATABASE_DOCKER_COMPOSE_FILE=docker-compose-db.yaml
LOAD_TEST_DOCKER_COMPOSE_FILE=./load-test/docker-compose-load-test.yaml

.PHONY: db-up db-down load-up load-down

db-up:
	docker-compose -f $(DATABASE_DOCKER_COMPOSE_FILE) up -d

db-down:
	docker-compose -f $(DATABASE_DOCKER_COMPOSE_FILE) down

load-up:
	docker-compose -f $(LOAD_TEST_DOCKER_COMPOSE_FILE) up -d

load-down:
	docker-compose -f $(LOAD_TEST_DOCKER_COMPOSE_FILE) down