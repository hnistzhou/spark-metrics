import com.ffan.marketing.eagle.job.MetricsComputationJob;
import org.junit.Test;

import java.util.Properties;

import static junit.framework.TestCase.fail;

/**
 * Created by bob on 2016/11/29.
 */
public class MetricsComputationJobTest {

    @Test
    public void computeTest() {
        Properties props = System.getProperties();
        props.setProperty("spark.master", "local[4]");
        try {

            MetricsComputationJob.compute();
        } catch (Exception e) {

            fail(e.getMessage());
        }
    }
}
