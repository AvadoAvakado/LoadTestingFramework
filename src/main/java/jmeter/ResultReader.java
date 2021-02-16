package jmeter;

import com.influxdb.client.write.Point;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import enums.JMeterReportColumns;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ResultReader {
    protected final CSVParser parser = new CSVParserBuilder().withIgnoreQuotations(false).build();
    protected final Map<String, Integer> columnNameIds = new HashMap<>();
    protected File currentReport;

    private void setNewReportColumnIds(String rawHeader) throws IOException {
        String[] columnNames = parser.parseLine(rawHeader);
        columnNameIds.clear();
        for (int i = 0; i < columnNames.length; i++) {
            columnNameIds.put(columnNames[i], i);
        }
    }

    public ResultReader(File resultReport) {
        setNewResultReport(resultReport);
    }

    public boolean setNewResultReport(File resultReport) {
        boolean result = true;
        currentReport = resultReport;
        try (BufferedReader reader = Files.newBufferedReader(currentReport.toPath())){
            setNewReportColumnIds(reader.readLine());
        } catch (IOException e) {
            //todo add logs
            result = false;
        }
        return result;
    }

    private String getReportColumnValue(JMeterReportColumns column, String[] splittedLine) {
        String columnValue = splittedLine[columnNameIds.get(column.toString())];
        return columnValue.startsWith("http") ? String.format("'%s'", columnValue) : columnValue;
    }

    public void addPointTags(Point point, String[] splittedLine, JMeterReportColumns... columns) {
        for (JMeterReportColumns column : columns) {
            addPointTag(point, splittedLine, column);
        }
    }

    public void addPointTag(Point point, String[] splittedLine, JMeterReportColumns column) {
        point.addTag(column.toString(), getReportColumnValue(column, splittedLine));
    }

    public void addPointFields(Point point, String[] splittedLine, JMeterReportColumns... columns) {
        for (JMeterReportColumns column : columns) {
            addPointField(point, splittedLine, column);
        }
    }

    public void addPointField(Point point, String[] splittedLine, JMeterReportColumns column) {
        point.addField(column.toString(), getReportColumnValue(column, splittedLine));
    }

    public abstract List<Point> getPoints(String measurementName);
}
