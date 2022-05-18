#Spark JOB
FROM arm32v7/ubuntu
ENV TZ=America/Toronto

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update && apt-get install -y default-jdk scala git wget software-properties-common 
RUN add-apt-repository -y ppa:deadsnakes/ppa

RUN apt-get update && apt-get install -y python3.8 python3-pip libjpeg-dev zlib1g-dev maven 
RUN pip3 install  influxdb  python-dotenv Pillow



RUN wget https://archive.apache.org/dist/spark/spark-3.2.1/spark-3.2.1-bin-hadoop3.2.tgz
RUN tar xvf spark-*
RUN mv spark-3.2.1-bin-hadoop3.2 /opt/spark
RUN echo "export SPARK_HOME=/opt/spark" >> ~/.profile
RUN echo "export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin" >> ~/.profile
RUN echo "export PYSPARK_PYTHON=/usr/bin/python3" >> ~/.profile
RUN export SPARK_HOME=/opt/spark  

RUN export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

RUN export PYSPARK_PYTHON=/usr/bin/python3

RUN . ~/.profile

ENV PATH $PATH:/opt/spark/bin:/opt/spark/sbin
COPY edge-variables.env /edge-variables.env
RUN mvn dependency:get -Dartifact=com.influxdb:influxdb-client-java:4.3.0
RUN mvn dependency:get -Dartifact=org.apache.bahir:spark-streaming-mqtt_2.11:2.4.0
RUN mvn dependency:get -Dartifact=com.drewnoakes:metadata-extractor:2.17.0
COPY maven-spark-processing/target/maven-spark-processing-1.0-SNAPSHOT.jar /

ENTRYPOINT spark-submit  --packages com.influxdb:influxdb-client-java:4.3.0,org.apache.bahir:spark-streaming-mqtt_2.11:2.4.0,com.drewnoakes:metadata-extractor:2.17.0  --class SparkMain maven-spark-processing-1.0-SNAPSHOT.jar && tail -f /dev/null
#ENTRYPOINT spark-submit  --packages com.influxdb:influxdb-client-java:4.3.0,org.apache.bahir:spark-streaming-mqtt_2.11:2.4.0 --class SparkMain maven-spark-processing-1.0-SNAPSHOT.jar && tail -f /dev/null
