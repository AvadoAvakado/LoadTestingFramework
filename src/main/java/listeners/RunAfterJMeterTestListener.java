package listeners;

import basetests.BaseJMeterTest;
import com.influxdb.client.write.Point;
import jmeter.DefaultResultReader;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.File;
import java.util.List;

public class RunAfterJMeterTestListener implements IInvokedMethodListener {
    private final static String JMETER_RESULT_TEMPLATE = System.getProperty("user.dir") + File.separator +
            "jmeterResults" + File.separator + "%s.jtl";

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        String methodName = method.getTestMethod().getMethodName();
        List<Point> measurements = new DefaultResultReader(new File(String.format(JMETER_RESULT_TEMPLATE, methodName)))
                .getPoints(methodName);
        BaseJMeterTest.getInfluxDbService().loadData(measurements);
        //todo make some logs
    }
}
