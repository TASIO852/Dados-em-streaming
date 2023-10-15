from kafka import KafkaConsumer

# Define a deserialization method for the messages
def deserializer(msg_bytes):
    return msg_bytes.decode('utf-8')

consumer = KafkaConsumer(
    "test-topic",
    bootstrap_servers='kafka:9092',
    group_id='test-consumer-group',
    auto_offset_reset='earliest',
    value_deserializer=deserializer
)

for msg in consumer:
    print(msg.value)
