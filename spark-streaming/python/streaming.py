from pyspark.sql import SparkSession

# Criação da sessão Spark
spark = SparkSession.builder \
    .appName("Spark Kafka Streaming") \
    .master("spark://172.18.0.10:7077") \
    .config("spark.jars.packages", "org.apache.spark:spark-sql-kafka-0-10_2.12:3.2.0") \
    .getOrCreate()

# Configuração do nível de log (opcional)
# spark.sparkContext.setLogLevel("ERROR")

# Leitura de streaming a partir do Kafka
df = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "172.18.0.9:9093") \
    .option("subscribe", "test-topic") \
    .option("startingOffsets", "earliest") \
    .load()

# Seleção das colunas de chave e valor (cast para STRING)
df = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")

# Definição da consulta de saída para a saída no console
query = df.writeStream \
    .outputMode("update") \
    .format("console") \
    .start()

try:
    # Aguarda até que a consulta seja terminada manualmente
    query.awaitTermination()
except KeyboardInterrupt:
    # Interrompe a consulta manualmente ao receber um KeyboardInterrupt (Ctrl+C)
    query.stop()
finally:
    # Encerra a sessão Spark quando a consulta é encerrada
    spark.stop()
