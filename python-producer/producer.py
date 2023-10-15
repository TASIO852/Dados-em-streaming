from kafka import KafkaAdminClient, KafkaProducer
from kafka.admin import NewTopic
import requests

# Inicie o cliente administrativo
admin_client = KafkaAdminClient(
    bootstrap_servers="kafka:9092", 
    client_id='admin'
)

# Nome do tópico que você quer criar
topic_name = "test-topic"

# Criar o tópico
topic = NewTopic(name=topic_name, num_partitions=1, replication_factor=1)
admin_client.create_topics(new_topics=[topic], validate_only=False)

# Aqui continua o resto do seu código...
producer = KafkaProducer(bootstrap_servers="kafka:9092")
API_URL = "https://jsonplaceholder.typicode.com/posts"

while True:

    # Fazer a requisição GET para a API
    response = requests.get(API_URL)
    response.raise_for_status()  # Isso lançará uma exceção se a requisição falhar

    # Converter a resposta em JSON
    data = response.json()

    # Para simplificar, vamos pegar apenas o primeiro post e converter em string
    first_post = str(data[0])

    # Compor a mensagem para o Kafka
    message = b"Logs do airflow " + bytes(first_post, "utf-8")

    producer.send("test-topic", key=b"python-message", value=message)
    print(message)
    time.sleep(2)
