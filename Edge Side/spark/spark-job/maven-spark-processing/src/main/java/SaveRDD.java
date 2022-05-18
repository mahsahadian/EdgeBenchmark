import DbConnection.DbManager;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import models.SensorData;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Time;
import org.json.JSONObject;
import DbConnection.InfluxDbManager;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.UUID;


import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.ImageMetadataReader;

import javax.imageio.ImageIO;


public class SaveRDD  implements VoidFunction2<JavaRDD<JSONObject>, Time>, Externalizable {

    private static DbManager dbManager ;

    public SaveRDD(){
        if (SaveRDD.dbManager == null){
            SaveRDD.dbManager = new InfluxDbManager();
            char[] token = "eX7DNDEOP-OpE_3Amz2Yi2P7oiUeaufmF2DakNCa3ljHDBccPpHW86QTAI1Prd0txBqYPEl1sbHUvUSjVknZng==".toCharArray();
            String org = "polymtl";
            String bucket = "sensors1";
            SaveRDD.dbManager.connect(token,org,bucket);
        }

    }

    @Override
    public void call(JavaRDD<JSONObject> rdd, Time time) throws Exception {
        String beforesparktime = time.toString() ;

        rdd.foreach(new VoidFunction<JSONObject>() {

            @Override
            public void call(JSONObject data) throws Exception {
                System.out.println("-------------------------------------------");
                System.out.println("Time " + beforesparktime +":");
                System.out.println("-------------------------------------------");
                System.out.println(" Saving data of frame id :" + data.getString("frame_id") );


                SensorData sensorData = new SensorData(
                        data.getString("measurement_name"),
                        data.getString("camera_id"),
                        beforesparktime,
                        data.getString("frame_id"),
                        data.getString("FromSensor_time"),
                        data.getString("value"),
                        data.getString("transmitdelay"),
                        data.getString("JPGQuality")
                );
                extractInfos(data.getString("value"),data.getString("count"));
                SaveRDD.dbManager.save(sensorData);
            }
        });

    }

    public void extractInfos(String image,String count){


        // There are multiple ways to get a Metadata object for a file

        //
        // SCENARIO 1: UNKNOWN FILE TYPE
        //
        // This is the most generic approach.  It will transparently determine the file type and invoke the appropriate
        // readers.  In most cases, this is the most appropriate usage.  This will handle JPEG, TIFF, GIF, BMP and RAW
        // (CRW/CR2/NEF/RW2/ORF) files and extract whatever metadata is available and understood.
        //
        try {
            byte[] data =  Base64.getDecoder().decode(image);
            //byte[] data = image.getBytes();

            InputStream is = new ByteArrayInputStream(data);
            BufferedImage newBi = ImageIO.read(is);
            String uniqueID = UUID.randomUUID().toString();

            File file = new File("/frame" + uniqueID + ".jpg");
            ImageIO.write(newBi , "jpg", file);

            Metadata metadata = ImageMetadataReader.readMetadata(file);

            print(metadata, "Using ImageMetadataReader");
            file.delete();

        } catch (ImageProcessingException | IOException e) {
            print(e);
        }
    }
    private static void print(Metadata metadata, String method)
    {
        System.out.println();
        System.out.println("-------------------------------------------------");
        System.out.print(' ');
        System.out.print(method);
        System.out.println("-------------------------------------------------");
        System.out.println();

        //
        // A Metadata object contains multiple Directory objects
        //
        for (Directory directory : metadata.getDirectories()) {

            //
            // Each Directory stores values in Tag objects
            //
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }

            //
            // Each Directory may also contain error messages
            //
            for (String error : directory.getErrors()) {
                System.err.println("ERROR: " + error);
            }
        }
    }

    private static void print(Exception exception)
    {
        System.err.println("EXCEPTION: " + exception);
    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }
}
