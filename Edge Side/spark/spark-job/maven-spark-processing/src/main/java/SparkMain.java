import org.apache.spark.SparkConf;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.mqtt.MQTTUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class SparkMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		SparkConf conf = new SparkConf().setAppName("sensors");
		conf.setMaster("spark://132.207.170.59:7077");
		conf.set("spark.executor.memory", "1g");
		conf.set("spark.driver.memory", "2g");
		conf.set("spark.memory.fraction","0.9");
		conf.set("spark.memory.storageFraction","0.2");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		jssc.sparkContext().setLogLevel("WARN");
		String brokerUrl = "tcp://132.207.170.59:1883";
		String topic = "topic";

		JavaReceiverInputDStream<String> mqttStream = MQTTUtils.createStream(jssc, brokerUrl, topic, StorageLevel.MEMORY_AND_DISK());

		JavaDStream<JSONObject> sensorDetailsStream = mqttStream.map(x -> {
			try {
				return new JSONObject(x);
			}catch (JSONException err){
				err.printStackTrace();
			}
			return null;
		});
		JavaDStream<JSONObject> sensorDetailsStreamfiltered = sensorDetailsStream
				.filter((JSONObject data) -> data.getInt("size") < 148000 && data.getInt("size") > 141000) ;

		sensorDetailsStreamfiltered.foreachRDD(new SaveRDD());
		try {
			jssc.start();
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}