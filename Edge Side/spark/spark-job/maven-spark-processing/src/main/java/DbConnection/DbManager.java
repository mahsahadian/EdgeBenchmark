package DbConnection;

import models.SensorData;

public interface DbManager {

     void connect(char[] token, String org, String bucket);
     void save(SensorData data);

}
