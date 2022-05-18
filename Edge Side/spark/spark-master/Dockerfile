#Spark Master
FROM arm32v7/ubuntu
ENV TZ=America/Toronto

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update

RUN apt-get install -y default-jdk scala git wget 
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
RUN apt install maven
ENV PATH $PATH:/opt/spark/bin:/opt/spark/sbin
ENTRYPOINT start-master.sh && tail -f /dev/null
