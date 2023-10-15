# Use uma imagem base Python
FROM python:3

# Diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo de requisitos e instale as dependências
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copie todo o diretório atual para o diretório de trabalho (supondo que seu Dockerfile esteja no diretório raiz do projeto)
COPY . .

# Execute o consumidor
CMD ["python", "-u", "./consumer.py"]
