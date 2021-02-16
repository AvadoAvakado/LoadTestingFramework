package jmeter;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import enums.JMeterReportColumns;

public class DefaultResultReader extends ResultReader {
    public DefaultResultReader(File resultReport) {
        super(resultReport);
    }

    @Override
    public List<Point> getPoints(String measurementName) {
        List<Point> points = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(currentReport.toPath())){
            //skip header
            reader.readLine();
            String line = reader.readLine();
            String[] splittedLine;
            while (line != null) {
                splittedLine = parser.parseLine(line);
                Point point = Point.measurement(measurementName);
                addPointTags(point, splittedLine, JMeterReportColumns.LABEL, JMeterReportColumns.THREAD_NAME,
                        JMeterReportColumns.DATA_TYPE, JMeterReportColumns.GRP_THREADS, JMeterReportColumns.ALL_THREADS,
                        JMeterReportColumns.URL);
                addPointFields(point, splittedLine, JMeterReportColumns.ELAPSED, JMeterReportColumns.RESPONSE_CODE,
                        JMeterReportColumns.RESPONSE_MESSAGE, JMeterReportColumns.SUCCESS, JMeterReportColumns.FAILURE_MESSAGE,
                        JMeterReportColumns.BYTES, JMeterReportColumns.SENT_BYTES, JMeterReportColumns.LATENCY,
                        JMeterReportColumns.IDLE_TIME, JMeterReportColumns.CONNECT);
                point.time(Long.parseLong(splittedLine[columnNameIds.get(JMeterReportColumns.TIME_STAMP.toString())]),
                        WritePrecision.MS);
                points.add(point);
                line = reader.readLine();
            }
        } catch (IOException e) {
            //todo add logs
        }
        return points;
    }
}
