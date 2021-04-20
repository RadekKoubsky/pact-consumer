# Pact consumer
This repository contains pact consumer for creating contract tests.

## Pact Broker
To use pact broker with contract tests, run the following command:

```
docker-compose up
```
The command above will start local broker and postgres running in containers defined in the `docker-compose.yaml` file.


To stop and remove the running containers and its volumes, run:

```
docker-compose down
```
