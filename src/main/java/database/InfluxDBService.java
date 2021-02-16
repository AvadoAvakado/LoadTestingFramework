package database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.write.Point;
import jmeter.DefaultResultReader;
import utils.PropertiesUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

public class InfluxDBService {
    private final InfluxDBClient influxDBClient;

    public InfluxDBService(String serverURL, String username, String password, String bucketName) {
        influxDBClient = InfluxDBClientFactory.createV1(serverURL, username,
                password.toCharArray(), bucketName, null);
    }

    public InfluxDBService() {
        this(PropertiesUtil.getInfluxDbServerUrl(), PropertiesUtil.getInfluxDbUsername(),
                PropertiesUtil.getInfluxDbPassword(), PropertiesUtil.getInfluxDbBucketName());
    }

    public InfluxDBService(String bucketName) {
        this(PropertiesUtil.getInfluxDbServerUrl(), PropertiesUtil.getInfluxDbUsername(),
                PropertiesUtil.getInfluxDbPassword(), bucketName);
    }

    public void loadData(List<Point> points) {
        loadData(points, null);
    }

    public void loadData(List<Point> points, Map<String,String> additionalTags) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            for (Point point : points) {
                if (additionalTags != null) {
                    for (Map.Entry<String, String> tag : additionalTags.entrySet()) {
                        point.addTag(tag.getKey(), tag.getValue());
                    }
                }
                writeApi.writePoint(point);
            }
            writeApi.flush();
        }
    }

    public static void main(String[] args) {
        WriteApi writeApi = new InfluxDBService().influxDBClient.getWriteApi();
        List<Point> points = new DefaultResultReader(new File(System.getProperty("user.dir")
                + File.separator + "jmeterResults" + File.separator + "wikiLoadTesting.jtl"))
                .getPoints("wikiLoadPageLoading");
        String s = "a ";
        for (Point point : points) {
            writeApi.writePoint(point);
        }
        writeApi.flush();
        writeApi.close();
    }
}
