package com.collector.tool;

import com.collector.enums.CollectorRoute;
import com.collector.utils.CollectorUtils;
import com.collector.utils.CommonsCollectionUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class QueryGeneratorTest {

  private final int LIMIT = 6000;
//  private String FOLDER_CSV = "D:\\kemdikbuk collector\\juli 2023";
  private String FOLDER_CSV = "C:\\Users\\singgih.perdana\\Documents\\perbaikan data agustus 2023\\csv";
  private final CollectorRoute COLLECTOR_TYPE = CollectorRoute.MERCHANT_CREATED;
//  private final String QUERY_PATTERN = "select id from order_payments where order_id in("
//      + "select order_id from orders where payment_status='PAYMENT_CONFIRMED' and order_id in(%s));";

//  private final String QUERY_PATTERN = "select distinct on (order_id) id from order_payments "
//      + " where order_id in(%s) ORDER  BY order_id, updated_date DESC;";

  private final String QUERY_PATTERN = "select merchant_id from orders where order_id in (%s)";
//  private final String QUERY_PATTERN = "select distinct comparison_id from orders  where order_id in (%s)";
//  private final String QUERY_PATTERN = "SELECT DISTINCT ON (order_id) id FROM order_payments "
//    + " where receipt_status in ('DONE') and order_id in (%s) "
//    + " ORDER  BY order_id, updated_date DESC;";

  @SneakyThrows
  @Test
  public void createIn(){

//    String file = FOLDER_CSV;
    String file = "K.csv";

    System.out.println("\n====================================");
    getFilenames(file).forEach(this::createByFileName);
  }

  @SneakyThrows
  private void createByFileName(String filename){
    String baseFilename =  FilenameUtils.getBaseName(filename);

    File file = new File( FOLDER_CSV +"\\"+filename);

    String targetFolder = file.getParent() +"\\query\\"+COLLECTOR_TYPE.name();
    new File(targetFolder).mkdirs();

    List<String> entityIds = CollectorUtils.getEntityIds(file);
    if(entityIds.size() > LIMIT){
      createInWithLimit(entityIds, targetFolder, baseFilename);
    } else {
      createInNoLimit(entityIds, targetFolder, baseFilename);
    }
  }

  @SneakyThrows
  private void createInWithLimit(List<String> entityIds, String targetFolder, String baseFilename){

    AtomicInteger totalData = new AtomicInteger();

    CommonsCollectionUtils.toMapPartitionList(entityIds, LIMIT).forEach((key, value) -> {
      saveToFile(targetFolder, baseFilename + "_" + key, value);
      totalData.addAndGet(value.size());
      System.out.println("page: " + key + ", data = " + value.size());
    });

    System.out.println(baseFilename +".csv = " + totalData.get());
    System.out.println("====================================");
  }

  @SneakyThrows
  private void createInNoLimit(List<String> entityIds, String targetFolder, String baseFilename){
    saveToFile(targetFolder, baseFilename,  entityIds);
    System.out.println(baseFilename + ".csv = " + entityIds.size());
    System.out.println("====================================");
  }

  private String buildData(List<String> entityIds){
    return entityIds.stream().map(this::quote).collect(Collectors.joining(","));
  }

  @SneakyThrows
  private void saveToFile(String targetFolder, String targetFileName, List<String> entityIds){
    String data = buildData(entityIds);
    FileUtils.writeStringToFile(
        new File(targetFolder + "\\" + targetFileName + ".sql"),
        String.format(QUERY_PATTERN, data.substring(0, data.lastIndexOf(","))));
  }

  private String quote(String text){
    return "'" + text + "'";
  }

  private List<String> getFilenames(String file){
    File folder = new File(file);
    if(folder.isDirectory()) {
      return Arrays.stream(folder.listFiles())
          .filter(f-> !f.isDirectory())
          .map(File::getName)
          .collect(Collectors.toList());
    }
    return Arrays.stream(file.split(","))
        .filter(StringUtils::isNotEmpty)
        .map(String::trim)
        .collect(Collectors.toList());
  }
}
