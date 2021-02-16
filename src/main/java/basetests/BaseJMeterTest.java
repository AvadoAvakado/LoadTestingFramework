package basetests;

import database.InfluxDBService;
import jmeter.JMeterScenarioRunner;
import listeners.RunAfterJMeterTestListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.io.File;
import java.io.IOException;

@Listeners({RunAfterJMeterTestListener.class})
public abstract class BaseJMeterTest {
    protected static final ThreadLocal<InfluxDBService> influxDBService = new ThreadLocal<>();
    protected static final ThreadLocal<JMeterScenarioRunner> jMeterRunner = new ThreadLocal<>();

    public static InfluxDBService getInfluxDbService() {
        return influxDBService.get();
    }

    private void initInfluxDbService() {
        influxDBService.set(new InfluxDBService());
    }

    private void initJMeterRunner() throws IOException {
        jMeterRunner.set(new JMeterScenarioRunner());
    }

    @BeforeMethod
    public void initMainServices() throws IOException{
        try {
            if (jMeterRunner.get() == null) {
                initJMeterRunner();
            }
        } catch (IOException e) {
            //todo make logs
            throw e;
        }
        if (influxDBService.get() == null) {
            initInfluxDbService();
        }
    }

    protected String getJmeterScenarioFilePathTemplate() {
        return System.getProperty("user.dir") + File.separator + "src" + File.separator
                + "test" + File.separator + "resources" + File.separator + "jmeterscenarios"
                + File.separator + "%s.jmx";
    }
}
