package com.collector.tool.jasperserver;

import com.collector.constant.ConstantJasperServer;
import com.collector.enums.CollectorRoute;
import com.collector.model.InsertCollectorsWebRequest;
import com.collector.utils.CollectorUtils;
import com.collector.utils.CommonsCollectionUtils;
import com.collector.utils.JasperRestUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GetMerchantIdByOrderIdTest {

  private final CollectorRoute COLLECTOR_ROUTE = CollectorRoute.MERCHANT_CREATED;
  private final int PARTITION_SIZE_REQUEST_REST = 200;
  private final int PARTITION_SIZE_PAYLOAD = 1000;
  private final String FOLDER_CSV = "C:\\Users\\singgih.perdana\\Documents\\perbaikan data agustus 2023\\csv";

  @Test
  @SneakyThrows
  public void go(){

    //multiple file with separated comma
    String file = "K.csv";

    JasperRestUtils restUtils = new JasperRestUtils();
    restUtils.loginToServer();

    String toBaseFilename =  Arrays.stream(file.split(","))
        .filter(StringUtils::isNotEmpty)
        .map(String::trim)
        .map(FilenameUtils::getBaseName)
        .collect(Collectors.joining("_"));

    Set<String> entityIds = getFilenames(file)
        .stream()
        .map(this::collectEntityIds)
        .collect(HashSet::new, Set::addAll, Set::addAll);

    processFile(toBaseFilename, restUtils, entityIds);
  }

  @SneakyThrows
  private void processFile(String toFilename, JasperRestUtils restUtils, Set<String> entityIds){
    Set<String> entityIdsResult = CommonsCollectionUtils.toMapPartitionList(
        entityIds, PARTITION_SIZE_REQUEST_REST)
        .entrySet()
        .stream()
        .map(data->prosesReport(data.getKey(), restUtils, "'"+ data.getValue().stream().collect(Collectors.joining("','")) + "'"))
        .collect(HashSet::new, Set::addAll, Set::addAll);

    AtomicInteger totalData = new AtomicInteger();
    CommonsCollectionUtils.toMapPartitionList(
        entityIdsResult, PARTITION_SIZE_PAYLOAD, entityId->
          InsertCollectorsWebRequest.Event.builder()
              .collectorType(COLLECTOR_ROUTE)
              .collectorPriority(0).entityId(entityId).build()
        ).forEach((partitionNumber, events) -> {
          String toFile = FOLDER_CSV +"\\payload\\"+COLLECTOR_ROUTE.name()+"\\"+toFilename+"_" + partitionNumber + ".json";
          System.out.println(partitionNumber+ ". save to file: "+ toFile + " > " + events.size());
          CollectorUtils.saveEventsToFile(events, toFile);
          totalData.addAndGet(events.size());
        });
    System.out.println("===totalData=== " + totalData);
  }

  @SneakyThrows
  public List<String> prosesReport(int partitionNumber, JasperRestUtils restUtils, String param){
//    System.out.println("partitionNumber = " + partitionNumber+": " + param);
    String resourceUri = "/reports/KEMDIKBUD_COLLECTOR/GET_MERCHANT_ID_BY_ORDER_ID.csv";
    List<NameValuePair> params = new ArrayList<>();

    params.add(new BasicNameValuePair("P_ORDER_IDS", param));

    HttpResponse httpResponse = restUtils.sendRequest(
        new HttpGet(), ConstantJasperServer.BASE_REPORT + resourceUri, params, true);
    return IOUtils.readLines(httpResponse.getEntity().getContent(), "UTF-8");
  }

  private List<String> getFilenames(String file){
    return Arrays.stream(file.split(","))
        .filter(StringUtils::isNotEmpty)
        .map(String::trim)
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private  List<String> collectEntityIds(String filename){
    return CollectorUtils.getEntityIds(new File(FOLDER_CSV + "\\" + filename));
  }

}
