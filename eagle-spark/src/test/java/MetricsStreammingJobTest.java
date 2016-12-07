import com.ffan.marketing.eagle.job.MetricsStreammingJob;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by bob on 2016/11/29.
 */
public class MetricsStreammingJobTest {

    @Test
    public void executeTest() {
        Properties props = System.getProperties();
        props.setProperty("spark.master", "local[4]");
        MetricsStreammingJob.execute();
    }
}
