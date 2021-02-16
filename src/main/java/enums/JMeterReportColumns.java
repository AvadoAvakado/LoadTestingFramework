package enums;

public enum JMeterReportColumns {
    TIME_STAMP("timeStamp"), ELAPSED("elapsed"), LABEL("label"),
    RESPONSE_CODE("responseCode"), RESPONSE_MESSAGE("responseMessage"),
    THREAD_NAME("threadName"), DATA_TYPE("dataType"), SUCCESS("success"),
    FAILURE_MESSAGE("failureMessage"), BYTES("bytes"), SENT_BYTES("sentBytes"),
    GRP_THREADS("grpThreads"), ALL_THREADS("allThreads"), URL("URL"),
    LATENCY("Latency"), IDLE_TIME("IdleTime"), CONNECT("Connect");

    private String name;

    JMeterReportColumns(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
