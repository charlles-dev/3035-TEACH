# Executar todos ou escolher
# make docker-run      → inicia todos
# make docker-ex1      → só Exercício 1
# make docker-ex2      → só Exercício 2  
# make docker-desafio   → só Desafio
# make docker-stop     →Para tudo

.PHONY: docker-run docker-ex1 docker-ex2 docker-desafio docker-stop

docker-run:
	docker-compose up -d

docker-ex1:
	docker-compose up -d postgres
	docker-compose up -d exercicio1

docker-ex2:
	docker-compose up -d postgres
	docker-compose up -d exercicio2

docker-desafio:
	docker-compose up -d postgres
	docker-compose up -d desafio

docker-stop:
	docker-compose down