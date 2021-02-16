package jmetertests;

public class WikiLoadTesting extends BaseJMeterTest {

    //todo replace such passing jmeter scenario to enums
    @org.testng.annotations.Test
    public void test(){
        jMeterRunner.get().executeJMeterScenario(String
                .format(getJmeterScenarioFilePathTemplate(), "wikiLoadTesting"));
    }
}
