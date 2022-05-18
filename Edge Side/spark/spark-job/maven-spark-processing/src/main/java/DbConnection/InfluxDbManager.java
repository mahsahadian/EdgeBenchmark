package DbConnection;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import models.SensorData;
import okhttp3.OkHttpClient;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class InfluxDbManager implements DbManager {
    WriteApiBlocking writeApi ;

    @Override
    public void connect(char[] token, String org, String bucket) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                //	.connectTimeout(40, TimeUnit.SECONDS)
                //	.readTimeout(60, TimeUnit.SECONDS)
                	.writeTimeout(60, TimeUnit.SECONDS)
                ;

        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url("http://132.207.170.25:8086")
                .okHttpClient(okHttpClientBuilder)
                .authenticateToken(token)
                .org(org)
                .bucket(bucket)
                .build();

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create(options);
         writeApi = influxDBClient.getWriteApiBlocking();
    }

    @Override
    public void save(SensorData data) {

        Point point = Point.measurement(data.getMeasurement_name()).addTag("camera_id", data.getCamera_id())

                .addField("beforeInfluxDB", String.valueOf(Timestamp.from(Instant.now())))
                .addField("beforesparktime", data.getBeforesparktime())
                .addField("frame_id", data.getFrame_id())
                .addField("FromSensor_time", data.getFromSensor_time())
                .addField("value", data.getValue())
                .addField("transmitdelay", data.getTransmitdelay())
                .addField("JPGQuality", data.getJPGQuality()) ;

        writeApi.writePoint(point);
    }
}
