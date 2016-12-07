import com.ffan.marketing.eagle.bean.Line;
import com.ffan.marketing.eagle.bean.MetricPoint;
import com.ffan.marketing.eagle.utils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by bob on 2016/11/30.
 */
public class BeanUtilsTest {
    @Test
    public void toLineTest() {
        String lineStr = "abdae13d|v1|||1479276020732|10.213.42.58-1479203075.82-13905-240|live-anchor|10.213.42.58|lottery.send|count=1,amount=5.32|lotteryType=liebian,lotterySource=live";

        Line line = BeanUtils.toLine(lineStr);
        Assert.assertEquals("abdae13d", line.getMagic());
        Assert.assertEquals(1479276020732L, line.getTimestamp());
        Assert.assertEquals("10.213.42.58-1479203075.82-13905-240", line.getTraceID());
        Assert.assertEquals("live-anchor", line.getAppName());
        Assert.assertEquals("10.213.42.58", line.getIp());
        Assert.assertEquals("lottery.send", line.getMetric());
        Assert.assertEquals(line.getFields().get("count").toString(), "1.0");
        Assert.assertEquals(line.getFields().get("amount").toString(), "5.32");
        Assert.assertEquals(line.getTags().get("lotteryType"), "liebian");
        Assert.assertEquals(line.getTags().get("lotterySource"), "live");
        System.out.println(BeanUtils.toPoint(line));

    }

    @Test
    public void toLine1Test() {
        String lineStr = "abdae13d|v1|||1479276020732|10.213.42.58-1479203075.82-13905-240|live-anchor|10.213.42.58|lottery.send|count=1,amount=5.32|lotteryType=liebian,lotterySource=live|";

        Line line = BeanUtils.toLine(lineStr);
        Assert.assertEquals("abdae13d", line.getMagic());
        Assert.assertEquals(1479276020732L, line.getTimestamp());
        Assert.assertEquals("10.213.42.58-1479203075.82-13905-240", line.getTraceID());
        Assert.assertEquals("live-anchor", line.getAppName());
        Assert.assertEquals("10.213.42.58", line.getIp());
        Assert.assertEquals("lottery.send", line.getMetric());
        Assert.assertEquals(line.getFields().get("count").toString(), "1.0");
        Assert.assertEquals(line.getFields().get("amount").toString(), "5.32");
        Assert.assertEquals(line.getTags().get("lotteryType"), "liebian");
        Assert.assertEquals(line.getTags().get("lotterySource"), "live");
    }


    @Test
    public void toLineIterableTest() {
        String lineStr = "abdae13d|v1|||1479276020732|10.213.42.58-1479203075.82-13905-240|live-anchor|10.213.42.58|lottery.send|count=1,amount=5.32|lotteryType=liebian,lotterySource=live";

        Line line = BeanUtils.toLine(lineStr);
        Iterable<MetricPoint> iter = BeanUtils.toPoints(line);
        System.out.println(iter);
    }
}
