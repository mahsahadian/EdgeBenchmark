import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import java.io.IOException; 
import org.apache.spark.streaming.mqtt.MQTTUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.Instant;
import java.util.List;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
public class SparkMain {
	  private static char[] token = "kbgEJQEHfZwpgCZaaX21aPbLdSB86U0Y9-XUH7r_1aIsgr7QSdjU9O5PASrp62bYmNhIB01zrC7w_Ep3pIHaUQ==".toCharArray();
	  private static String org = "polymtl";
	  private static String bucket = "sensors";
	public static void on_RDD(JSONObject data , String beforesparktime ){
		InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://132.207.170.25:8088", token, org, bucket);
		WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
		Point point = Point.measurement("t_spark_test1").addTag("camera_id", data.getString("camera_id") ).addField("location", "Main Lobby")

				.addField("beforeSpark_time", beforesparktime)
				.addField("frame_id", data.getString("frame_id"))
				.addField("FromSensor_time", data.getString("FromSensor_time"))
				.addField("value", data.getString("value"))
				.addField("transmitdelay", data.getString("transmitdelay"))
				.addField("JPGQuality", data.getString("JPGQuality")) ;


		writeApi.writePoint(point);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

			
			SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("sensors");
	        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
	        jssc.sparkContext().setLogLevel("ERROR");
	        String brokerUrl = "tcp://132.207.170.59:1883";

	        String topic = "topic";
	        JavaReceiverInputDStream<String> mqttStream = MQTTUtils.createStream(jssc, brokerUrl, topic);
	        
	        JavaDStream<JSONObject> sensorDetailsStream = mqttStream.map(x -> {
	            try {
	                return new JSONObject(x);
	            }catch (JSONException err){
	                System.out.println(err);
	            }
	            return null;
	        });
	        sensorDetailsStream.foreachRDD(
	                (VoidFunction<JavaRDD<JSONObject>>) ( rdd) -> {
	                    String beforesparktime = "0" ;
	                    rdd.foreach(new VoidFunction<JSONObject>() {

	                        @Override
	                        public void call(JSONObject s) throws Exception {
	                            System.out.println("-------------------------------------------");
	                            System.out.println("Time " + beforesparktime +":");
	                            System.out.println("-------------------------------------------");
	                            SparkMain.on_RDD(s,beforesparktime); ;
	                        }
	                    });
	                }
	        );
	        try {
	            jssc.start();
	            jssc.awaitTermination();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	}

}
