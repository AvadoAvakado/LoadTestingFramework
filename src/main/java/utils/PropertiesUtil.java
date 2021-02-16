package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesUtil {
    private static final String PROPERTY_NAME_PATTERN = "%s%s.properties";
    private static final String JMETER_RUNNER_LOCAL_PROPERTIES_NAME = String.format(PROPERTY_NAME_PATTERN, "jmeterRunner", "Local");
    private static final String JMETER_RUNNER_PROPERTIES_NAME = String.format(PROPERTY_NAME_PATTERN, "jmeterRunner", "");
    private static final String INFLUX_DB_LOCAL_PROPERTIES_NAME = String.format(PROPERTY_NAME_PATTERN, "influxDB", "Local");
    private static final String INFLUX_DB_PROPERTIES_NAME = String.format(PROPERTY_NAME_PATTERN, "influxDB", "");
    private static final String RESOURCE_FOLDER_PATH = System.getProperty("user.dir") + File.separator
            + "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    private static final Properties runnerProperties = new Properties();
    private static final Properties influxDbProperties = new Properties();

    static {
        boolean isLocalJMeterPropertiesExist = new File(RESOURCE_FOLDER_PATH + JMETER_RUNNER_LOCAL_PROPERTIES_NAME)
                .exists();
        boolean isLocalInfluxDBPropertiesExist = new File(RESOURCE_FOLDER_PATH + INFLUX_DB_LOCAL_PROPERTIES_NAME)
                .exists();
        try (InputStreamReader readerJmeter = new InputStreamReader(
                PropertiesUtil.class.getResourceAsStream("/" + (isLocalJMeterPropertiesExist ? JMETER_RUNNER_LOCAL_PROPERTIES_NAME
                        : JMETER_RUNNER_PROPERTIES_NAME)), StandardCharsets.UTF_8);
            InputStreamReader readerInfluxDB = new InputStreamReader(
                    PropertiesUtil.class.getResourceAsStream("/" + (isLocalInfluxDBPropertiesExist ? INFLUX_DB_LOCAL_PROPERTIES_NAME:
                            INFLUX_DB_PROPERTIES_NAME))
            )) {
            runnerProperties.load(readerJmeter);
            influxDbProperties.load(readerInfluxDB);
        } catch (IOException e) {
            //todo make logs
        }
    }

    private static String getProperty(Properties properties, String key) throws NullPointerException {
        final String property = properties.getProperty(key);
        if (property != null) {
            return property;
        }
        throw new NullPointerException(String.format("Key %s not exists", key));
    }

    private static String getJMeterRunnerProperty(String key) {
        return getProperty(runnerProperties, key);
    }

    private static String getInfluxDbProperty(String key) {
        return getProperty(influxDbProperties, key);
    }

    public static String getJMeterRunnerPropertiesPath() {
        return getJMeterRunnerProperty("jmeter.path.properties");
    }

    public static String getJMeterRunnerHomePath() {
        return getJMeterRunnerProperty("jmeter.path.home");
    }

    public static String getInfluxDbServerUrl() {
        return getInfluxDbProperty("serverURL");
    }

    public static String getInfluxDbUsername() {
        return getInfluxDbProperty("username");
    }

    public static String getInfluxDbPassword() {
        return getInfluxDbProperty("password");
    }

    public static String getInfluxDbBucketName() {
        return getInfluxDbProperty("bucketName");
    }

    private PropertiesUtil() {};
}
