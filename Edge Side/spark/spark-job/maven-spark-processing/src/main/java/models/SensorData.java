package models;

public class SensorData {
    String measurement_name ;
    String camera_id ;
    String beforesparktime ;
    String frame_id ;
    String FromSensor_time ;
    String value ;
    String transmitdelay ;
    String JPGQuality ;

    public SensorData(String measurement_name ,
                      String camera_id ,
                      String beforesparktime ,
                      String frame_id ,
                      String FromSensor_time ,
                      String value ,
                      String transmitdelay ,
                      String JPGQuality){
    this.measurement_name = measurement_name ;
    this.camera_id = camera_id ;
    this.beforesparktime = beforesparktime ;
    this.frame_id = frame_id ;
    this.FromSensor_time = FromSensor_time ;
    this.value = value ;
    this.transmitdelay = transmitdelay ;
    this.JPGQuality = JPGQuality ;
    }

    public String getMeasurement_name() {
        return measurement_name;
    }

    public String getCamera_id() {
        return camera_id;
    }

    public String getBeforesparktime() {
        return beforesparktime;
    }

    public String getFrame_id() {
        return frame_id;
    }

    public String getFromSensor_time() {
        return FromSensor_time;
    }

    public String getValue() {
        return value;
    }

    public String getTransmitdelay() {
        return transmitdelay;
    }

    public String getJPGQuality() {
        return JPGQuality;
    }
}
