package utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesUtil {
    private static final String JMETER_RUNNER_LOCAL_PROPERTIES_NAME = "jmeterRunnerLocal.properties";
    private static final String JMETER_RUNNER_PROPERTIES_NAME = "jmeterRunner.properties";
    private static final String RESOURCE_FOLDER_PATH = System.getProperty("user.dir") + File.separator
            + "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    private static final Properties runnerProperties = new Properties();

    static {
        boolean isLocalPropertiesExist = new File(RESOURCE_FOLDER_PATH + JMETER_RUNNER_LOCAL_PROPERTIES_NAME)
                .exists();
        try (final InputStreamReader reader = new InputStreamReader(
                PropertiesUtil.class.getResourceAsStream("/" + (isLocalPropertiesExist ? JMETER_RUNNER_LOCAL_PROPERTIES_NAME
                        : JMETER_RUNNER_PROPERTIES_NAME)), StandardCharsets.UTF_8)) {
            runnerProperties.load(reader);
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

    public static String getJMeterRunnerPropertiesPath() {
        return getJMeterRunnerProperty("jmeter.path.properties");
    }

    public static String getJMeterRunnerHomePath() {
        return getJMeterRunnerProperty("jmeter.path.home");
    }

    private PropertiesUtil() {};
}
