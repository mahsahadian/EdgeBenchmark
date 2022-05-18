import paho.mqtt.client as mqtt
from dotenv import load_dotenv
import os 
import pymongo
import ast


load_dotenv("sensor-variables.env")

count = 0 
myclient = pymongo.MongoClient("mongodb://root:example@" + os.getenv('INFLUXDB_DATABASE_IP') +":27017/")
mydb = myclient["applicationdb"]
mycol = mydb["sensors"]
cname = "Client"
client = mqtt.Client(cname)


def on_connect(client, userdata, flags, rc):
    """ The callback for when the client receives a CONNACK response from the server."""
    print('Connected with result code ' + str(rc))
    client.subscribe('topic')
# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    #current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    #print(msg)
    #timestamp = str(time.time())
    #print(msg.topic + ' ' + str(msg.payload))
    x = mycol.insert_one(ast.literal_eval(msg.payload))
    global count 
    count = count + 1 
    print(count)
    #sensor_data = _parse_mqtt_message(msg.topic, msg.payload.decode('utf-8'))
    #if sensor_data is not None:
    #    _send_sensor_data_to_influxdb(sensor_data)
    #print("a")
    #splits_ = str(msg.payload).split('XXX')
    #splits_ = str(msg.payload).split('XXX')
    #for i in range(len(splits_)):
    # data = ast.literal_eval(str(msg.payload))
    # data = ast.literal_eval(msg.payload.decode('utf-8'))
    # jsondata_body = [
    #     {
    #     "measurement": "t_spark_test1",
    #     "tags": {
    #         "camera_id": camera_id,
    #     },
    #     "transmitdelay":transmitdelay,
    #     "JPGQuality":JPGQuality,
    #     "fields": {
    #         "recieved_time": timestamp,
    #         "frame_id": data['frame_id'],
    #         "sent_time": data['sent_time'],
    #         "value": data['value']
    #     }
    # }
    # ]
    #save_influx(jsondata_body, str(msg.payload))
    #print(msg.topic, str(msg.payload))
    #thinktime or sleep aftersending

    #if splits_[i] == 'refresh':
    #client.reinitialise()
    #camera = Camera(camera_id, destination_cluster_ip, JPGQuality, transmitdelay, './imagesout')
    #camera.processVideoStream()
    #time.sleep(1)

    #val = splits_[1].replace('"', '')
    #print('recieved id: ', val)
    #if int(val) == 2222:
    #    camera = Camera(camera_id, destination_cluster_ip, JPGQuality, transmitdelay, './imagesout')
    #    camera.processVideoStream()


# Open a file with access mode 'a'
#with open("count.txt", "a") as file_object:
    # Append 'hello' at the end of file
 #   file_object.write("\n")
 #   file_object.write("")


client.on_connect = on_connect
client.on_message = on_message
client.connect(os.getenv('MQTT_SERVER_IP'), int(os.getenv('MQTT_SERVER_PORT')), 60)
client.subscribe("topic", qos=1)
print("103 for sure")
client.loop_forever()