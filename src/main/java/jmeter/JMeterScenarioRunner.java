package jmeter;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import utils.FileUtils;
import utils.PropertiesUtil;

import java.io.File;
import java.io.IOException;

public class JMeterScenarioRunner {
    private final StandardJMeterEngine jMeterEngine;
    private final static String JMETER_RESULT_TEMPLATE = System.getProperty("user.dir") + File.separator + "jmeterResults" + File.separator + "%s.jtl";

    public JMeterScenarioRunner() throws IOException{
        this(System.getProperty("user.dir") + File.separator + PropertiesUtil.getJMeterRunnerPropertiesPath(), PropertiesUtil.getJMeterRunnerHomePath());
    }

    public JMeterScenarioRunner(String pathToJMeterProperties, String pathToJMeterHome) throws IOException {
        jMeterEngine = new StandardJMeterEngine();
        JMeterUtils.loadJMeterProperties(pathToJMeterProperties);
        JMeterUtils.setJMeterHome(pathToJMeterHome);
        JMeterUtils.initLocale();
        SaveService.loadProperties();
    }

    public boolean executeJMeterScenario(String pathToScenario) {
        boolean isExecuted = false;
        try {
            HashTree testPlanTree = prepareTestPlanTree(pathToScenario);
            // Run JMeter Test
            jMeterEngine.configure(testPlanTree);
            jMeterEngine.run();
            isExecuted = true;
        } catch (IOException e) {
            //todo make logs
        }
        return isExecuted;
    }

    private HashTree prepareTestPlanTree(String fullPathToScenario) throws IOException {
        File scenario = new File(fullPathToScenario);
        HashTree testPlanTree = SaveService.loadTree(scenario);
        Summariser summariser = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summariser = new Summariser(summariserName);
        }
        String resultFilePath = String.format(JMETER_RESULT_TEMPLATE, FileUtils.getFileNameWithoutExtension(scenario));
        ResultCollector logger = new ResultCollector(summariser);
        logger.setFilename(resultFilePath);
        testPlanTree.add(testPlanTree.getArray()[0], logger);
        return testPlanTree;
    }
}
