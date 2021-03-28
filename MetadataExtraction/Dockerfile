FROM python:3.8

WORKDIR /code

COPY requirements.txt .

RUN pip install -r requirements.txt
RUN apt-get update && apt-get install -y libimage-exiftool-perl

COPY ./ .

CMD ["python", "./hello.py"]


