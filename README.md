# Transmissão de dados do Kafka Topic para o Spark usando o Spark Structured Streaming

Esta versão é otimizada para ser mais leve. Tudo é executado a partir de um simples comando. Não há dependência do seu computador além do docker e do docker-compose.

Este projeto é uma arquitetura de streaming simples kafka e spark.
Um arquivo docker-compose inicializa um cluster kafka e um cluster spark com todas as suas dependências.
Os produtores enviam mensagens de texto para kafka um tópico chamado "teste-tópico".
Você pode consumir mensagens com scripts de consumidores escritos em NodeJS e Python ou transmitir dados com spark streaming que simplesmente imprime no console todos os dados recebidos.

> Atenção: O streaming de dados só funciona com spark in scala por enquanto, versão python está em andamento

## Requisitos

- Docker & Docker Compose (https://docs.docker.com/engine/install/ubuntu/)
  - Docker >= 19.X.X
  - Compose docker ~ 1.29.2

> Por favor, certifique-se:

- você pode executar comandos com privilégios de root em seu computador
- sua porta 8080 não está em uso
- a sub-rede 172.18.0.0/24 não está em uso em seu computador

### 2. Execute docker-compose.yml

> Importante: Não feche o terminal depois de executar o docker-compose <br>

```
docker-compose up
```

> Nota: Aguarde até que todos os serviços estejam ativos (cerca de 1 a 2 minutos, o console ficará bastante ocioso)

### 3. Envie o job de streaming do Spark

> Nota: Certifique-se de ter privilégios de root

Em um novo terminal execute o comando

```bash
sudo chmod 777 jars_dir && \
docker exec -it spark \
Spark-submissão \
--packages "org.apache.spark:spark-sql-kafka-0-10_2.12:3.2.0" \
--master "Spark://172.18.0.10:7077" \
--class Transmissão \
--conf spark.jars.ivy=/opt/bitnami/spark/ivy \
ivy/spark-streaming-with-kafka_2.12-1.0.jar
```
