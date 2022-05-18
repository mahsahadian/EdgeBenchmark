# IoTBenchmarkTool
Benchmarking tool for IoT environments

EdgeBenchmark is a tool for benchmarking the edge device. It is capable of monitoring the resources through different load.
Measurements such as CPU, Memory, DiskI/O and Network.
Also, We follow the similar steps as EMU-IoT-Gateway to generate the photos simulating camera sensors(simulating the real sensors).

The motivation is to create a tool to monitor the Raspberrypi in terms of performance metrics.


Main Features:
-Easy to use: Edgebenchmark is a tool combined multiple testing functions so users 
do not need to switch different tools.
-Testing report and result: Supporting storing testing information and results
for further query or analysis.
Visualize results: Integration with Grafana to visualize the results.

We recommend using Linux or Raspbian systems.

Prerequisites of EdgeBenchmarkTool
To use the tool, you need to have:

-Linux 20
-Raspberry PI 4

Installation requirements on Rpi:

-Docker
-Docker-compose

##Instruction:
###Sensor side:
Running the docker-compose file on the Sensor side to send the camera data to the Raspberry Pi.

`sudo docker-compose up`


If you want to scale up to [n] number of cameras to try different load use:

`sudo docker-compose up --scale app=[n]`

If n>200 add timeout option as follows:

`sudo COMPOSE_HTTP_TIMEOUT=[optional] docker-compose up --scale app=[n]`

### Edge side:
2.1: Instalation Requirements:
-Install python
-Install docker 
change the telegraf.conf file as follows:
# Configuration for sending metrics to InfluxDB
urls = ["IP of the Influxdb VM:Port"] --recommend 8086
database = "DBname"
skip_database_creation = true
# HTTP Basic Auth
  username = "telegraf"
  password = "telegraf"

2.2:Run docker-compose.yml file

`sudo docker-compose up`

2.3:Monitoring
 -Change monitoring.py based on what you the changes on telegraf.conf

client = InfluxDBClient('Influxdb IP', Influxdb port, 'telegraf', 'telegraf', 'DBname')

 -Then run the monitoring.py on the edge to collect the measurements such as CPU, Memory, DiskI/O and Network
3.DB side
-Install influxdb
-you can connect the Influxdb to Grafana for visualizaton or convert the measurements to csv file

Note: after a set of experiments you have to reset the systems and dockers to remove the loads.
`sudo docker-compose down`
`sudo service docker restart`






