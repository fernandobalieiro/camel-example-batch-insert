# camel-example-batch-insert

Example Repository for `Nice to meet you, Camel` presentation:
https://docs.google.com/presentation/d/1Z-NWwgoZZKls_75qj9JLZaVSmTgKBGihmaVtY1n2Sdc

## Testing
```bash
git clone https://github.com/fernandobalieiro/camel-example-batch-insert.git
cd camel-example-file
docker volume prune -f; docker compose down -v; docker compose up -d --remove-orphans
mvn clean camel:run
```
