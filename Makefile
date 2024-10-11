DATABASE_DOCKER_COMPOSE_FILE=./infra/database/docker-compose-db.yaml
LOAD_TEST_DOCKER_COMPOSE_FILE=./infra/load-test/docker-compose-load-test.yaml
KAFKA_DOCKER_COMPOSE_FILE=./infra/kafka/docker-compose-kafka.yaml

.PHONY: db-up db-down load-up load-down

db-up:
	docker-compose -f $(DATABASE_DOCKER_COMPOSE_FILE) up -d

db-down:
	docker-compose -f $(DATABASE_DOCKER_COMPOSE_FILE) down

load-up:
	docker-compose -f $(LOAD_TEST_DOCKER_COMPOSE_FILE) up -d

load-down:
	docker-compose -f $(LOAD_TEST_DOCKER_COMPOSE_FILE) down

kafka-up:
	@docker-compose -f $(KAFKA_DOCKER_COMPOSE_FILE) up -d

kafka-down:
	@docker-compose -f $(KAFKA_DOCKER_COMPOSE_FILE) down