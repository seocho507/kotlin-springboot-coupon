DATABASE_DOCKER_COMPOSE_FILE=docker-compose-db.yaml

.PHONY: db-up db-down db-up-logs db-status

db-up:
	docker-compose -f $(DATABASE_DOCKER_COMPOSE_FILE) up -d

db-down:
	docker-compose -f $(DATABASE_DOCKER_COMPOSE_FILE) down
