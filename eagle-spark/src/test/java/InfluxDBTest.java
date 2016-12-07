import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.junit.Test;

/**
 * Created by bob on 2016/12/5.
 */
public class InfluxDBTest {

    @Test
    public void createDBTest() {

        InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.99.100:32769", "marketingMonitor", "D7b9BoL3");
        Point point = Point.measurement("test").addField("count", 5).tag("source", "live").build();
        influxDB.write("marketingMonitorDB", "", point);
    }
}
