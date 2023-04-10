# Streaming de dados do Kafka Topic para Spark usando Spark Structured Streaming V2.0

Esta versão é otimizada para ser mais leve. Tudo é executado a partir de um simples comando. Não há nenhuma dependência em seu computador além do docker e do docker-compose.

## fluxo de trabalho

Este projeto é uma arquitetura simples de kafka e streaming de spark.
Um arquivo docker-compose inicializa um cluster kafka e um cluster spark com todas as suas dependências.
Os produtores enviam mensagens de texto para kafka um tópico chamado "test-topic".
Você pode consumir mensagens com scripts de consumidores escritos em NodeJS e Python ou transmitir dados com streaming de spark, que simplesmente imprime no console todos os dados recebidos.

> Aviso: o streaming de dados só funciona com spark em scala por enquanto, a versão python está em andamento

<img src="architecture.png" />

Tudo é automático neste projeto.

Tudo o que você precisa fazer é executar um script simples que acionará tudo.

Você pode mergulhar mais fundo no código e brincar com ele para sujar as mãos 😊

## Requisitos

- Docker e Docker Compose (https://docs.docker.com/engine/install/ubuntu/)
  - janela de encaixe >= 19.X.X
  - docker-compose ~1.29.2

> Certifique-se de que:

- você pode executar comandos com privilégios de root em seu computador
- sua porta 8080 não está em uso
- a sub-rede 172.18.0.0/24 não está em uso em seu computador

## Estrutura de pastas do projeto

```
.
├── architecture.png........ # Arquitetura do projeto
├── clean-env.sh............ # Limpa o ambiente
├── docker-compose.yml...... # Criar kafka e acender clusters
├── nodejs-consumer......... # Consome mensagens de kafka
│ ├── consumidor.js
│ ├── Dockerfile
│ ├── pacote.json
│ └── package-lock.json
├── nodejs-producer......... # Produz mensagens para kafka
│ ├── Dockerfile
│ ├── pacote.json
│ └── produtor.js
├── python-consumer......... # Consome mensagens para kafka
│ ├── consumidor.py
│ └── Dockerfile
├── python-producer......... # Produz mensagens para kafka
│ ├── Dockerfile
│ └── produtor.py
├── README.md
└── spark-streaming......... # Consumir dados de streaming do kafka e afundar no console
      ├── python.............. # Streaming com python (Work In Progress)
      └── scala............... # Streaming com scala
```

## Executando serviços

| nome do serviço              | endereço[:porta] |
| ---------------------------- | ---------------- |
| tratador                     | 172.18.0.8:2181  |
| kafka (do host)              | 172.18.0.9:9093  |
| kafka (dentro do recipiente) | 172.18.0.9:9092  |
| spark mestre                 | 172.18.0.10:7077 |
| spark interface do usuário   | 172.18.0.10:8080 |
| spark trabalhador 1          | 172.18.0.11      |
| spark trabalhador 2          | 172.18.0.12      |
| spark-streaming-kafka        | 172.18.0.13      |
| nodejs-produtor              | 172.18.0.14      |
| nodejs-consumer              | 172.18.0.15      |
| produtor de python           | 172.18.0.16      |
| python-consumidor            | 172.18.0.17      |

O projeto cria um nome de rede docker "kafka-spark" no intervalo de endereços 172.18.0.0/24

## Começando

> Observação: você pode acessar os arquivos docker-compose.yml ou run.sh para entender melhor como as coisas funcionam.

### 1. Clone o repositório e o cd na pasta

> Nota: Certifique-se de estar no diretório <kafka-spark-streaming-docker>

```
      git clone https://github.com/MDiakhate12/kafka-spark-streaming-docker.git
      cd kafka-spark-streaming-docker/
```

### 2. Execute docker-compose.yml

> Importante: Não feche o terminal depois de executar o docker-compose <br>

```
docker-compose
```

> Nota: Aguarde até que todos os serviços estejam ativos (cerca de 1 a 2 minutos, o console ficará bastante ocioso)

### 3. Envie o trabalho de streaming do Spark

> Nota: Certifique-se de ter privilégios de root

Em um novo terminal, execute o comando

```bash
sudo chmod 777 jars_dir && \
docker exec -it spark \
enviar spark \
--packages "org.apache.spark:spark-sql-kafka-0-10_2.12:3.2.0" \
--master "spark://172.18.0.10:7077" \
--class Transmissão \
--conf spark.jars.ivy=/opt/bitnami/spark/ivy \
ivy/spark-streaming-with-kafka_2.12-1.0.jar
```

Depois que tudo estiver definido, sua saída deve ficar assim:

![Captura de tela de 15/11/2021 15/05/41](https://user-images.githubusercontent.com/46793415/141721499-a248453e-4a7f-4d5e-88ea-c353de7922b9.png)

É isso 🎉🎉 Parabéns.

## Olha o resultado

> Observação: a IU do Spark está disponível em http://172.18.0.10:8080

Em um novo terminal, você pode ver os logs de cada serviço executando:

```
docker-compose logs -f [SERVICE_NAME]
```

Os serviços disponíveis são:

1. tratador
2. kafka
3. spark
4. spark-worker-1
5. trabalhador-spark-2
6. spark-streaming-kafka
7. nodejs-produtor
8. nodejs-consumer
9. produtor de python
10. consumidor de python
