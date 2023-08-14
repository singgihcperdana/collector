package com.collector.utils;

import com.collector.enums.CollectorRoute;
import com.collector.model.InsertCollectorsWebRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class CollectorUtils {

  private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

  public static Map<String, CollectorRoute> MAP_COLLECTOR = new HashMap<>() {{
    put("TransactionOrderDeliveryUpdated", CollectorRoute.ORDER_DELIVERY_UPDATED);
    put("TransactionTestimonySubmitted", CollectorRoute.ORDER_TESTIMONY_SUBMITTED);
    put("TransactionPaymentComplained", CollectorRoute.PAYMENT_COMPLAINED);
    put("TransactionPaymentSettled", CollectorRoute.PAYMENT_SETTLED);
    put("TransactionPaymentConfirmed", CollectorRoute.PAYMENT_CONFIRMED);
    put("TransactionOrderProcessed", CollectorRoute.ORDER_PROCESSED);
    put("TransactionOrderShipped", CollectorRoute.ORDER_SHIPPED);
    put("TransactionOrderCancelled", CollectorRoute.ORDER_CANCELLED);
    put("TransactionBastGenerated", CollectorRoute.ORDER_BAST_GENERATED);
    put("TransactionOrderRejected", CollectorRoute.ORDER_REJECTED);
    put("TransactionOrderDelivered", CollectorRoute.ORDER_DELIVERED);
    put("TransactionCreated", CollectorRoute.ORDER_CREATED);
    put("TransactionOrderReturned", CollectorRoute.ORDER_RETURNED);
    put("TransactionCancellationApproved", CollectorRoute.ORDER_CANCELLATION_APPROVED);
    put("TransactionInfoUpdateApproved", CollectorRoute.ORDER_INFO_UPDATE_APPROVED);
    put("TransactionCancellationProposed", CollectorRoute.ORDER_CANCELLATION_PROPOSED);
    put("TransactionInfoUpdateProposed", CollectorRoute.ORDER_INFO_UPDATE_PROPOSED);
    put("TransactionOrderReceived", CollectorRoute.ORDER_RECEIVED);
    put("TransactionInfoUpdateRejected", CollectorRoute.ORDER_INFO_UPDATE_REJECTED);
    put("TransactionAgreementUpdated", CollectorRoute.AGREEMENT_UPDATED);
    put("TransactionInfoUpdated", CollectorRoute.ORDER_INFO_UPDATED);
    put("TransactionComplaintSubmitted", CollectorRoute.COMPLAINT_SUBMITTED);
    put("TransactionCancellationRejected", CollectorRoute.ORDER_CANCELLATION_REJECTED);
    put("TransactionComplaintFollowedUp", CollectorRoute.COMPLAINT_FOLLOWED_UP);
    put("TransactionComplaintResolved", CollectorRoute.COMPLAINT_RESOLVED);
    put("TransactionClosed", CollectorRoute.ORDER_CLOSED);
  }};


  public static void insertEvents(String payloadFolder, String cookie, int interval){

    File folder = new File(payloadFolder);

    CloseableHttpClient httpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD).build())
        .build();

    RequestConfig.Builder requestConfig = RequestConfig.custom();
    requestConfig.setConnectTimeout(30 * 1000);
    requestConfig.setConnectionRequestTimeout(30 * 1000);
    requestConfig.setSocketTimeout(30 * 1000);

    HttpPost request = new HttpPost("https://*.*.com/backend/operationals/collectors");
    request.setHeader("Content-type", "application/json");
    request.addHeader("Cookie", cookie);
    request.setConfig(requestConfig.build());

    turnOffLogHttpClient();
    List<String> fileNames = Arrays.stream(folder.listFiles())
        .map(File::getName)
        .collect(Collectors.toList());
    CommonsCollectionUtils.sortContainingNumber(fileNames);

    for (int i = 0; i < fileNames.size(); i++) {
      File file = new File(folder + "\\" + fileNames.get(i));
      try {
        String entityBodyJson = new String(Files.readAllBytes(file.toPath()));
        HttpEntity entityBody = new ByteArrayEntity(entityBodyJson.getBytes("UTF-8"));
        request.setEntity(entityBody);

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String result = entity == null ? null : EntityUtils.toString(entity);
        String now = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        System.out.println("["+now+"] " + file.getName() + " : " + result);
        sleep(interval);
      } catch (IOException e) {
        System.out.println("error at file: " + file.getAbsolutePath());
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }

  }

  @SneakyThrows
  private static void sleep(int second){
    Thread.sleep(second * 1000L);
  }

  public static void printSeparator(int count){
    StringBuilder separator = new StringBuilder();
    int i=0;
    while(i++ < count){
      separator.append("=");
    }
    System.out.println(separator);
  }

  public static void turnOffLogHttpClient(){
    java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.SEVERE);
    java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(Level.SEVERE);
    java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.SEVERE);
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
    System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
    System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
    System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
    System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
  }

  @SneakyThrows
  private static String getCookie(){
    ClassLoader classLoader = CollectorUtils.class.getClassLoader();
    File cookieFile = new File(URLDecoder.decode(classLoader.getResource("cookie.txt").getFile(), "UTF-8"));
    return new String(Files.readAllBytes(cookieFile.toPath()));
  }

  @SneakyThrows
  public static Map<Integer, List<InsertCollectorsWebRequest.Event>> getData(
      File file, CollectorRoute collectorRoute, int size) {
    return CommonsCollectionUtils.toMapPartitionList(getEntityIds(file), size,
        id->InsertCollectorsWebRequest.Event.builder()
            .collectorType(collectorRoute)
            .collectorPriority(0)
            .entityId(id)
            .build());
  }

  public static List<String> getEntityIds(File file) throws IOException {
    return FileUtils.readLines(file)
        .stream()
        .filter(StringUtils::isNotEmpty)
        .map(String::trim)
        .filter(line->!line.contains("id"))
        .filter(line->!line.startsWith("---"))
        .filter(line->!line.contains("rows"))
        .filter(line->!line.contains("("))
        .distinct()
        .collect(Collectors.toList());
  }

  @SneakyThrows
  public static void saveEventsToFile(
      List<InsertCollectorsWebRequest.Event> events, String toFile) {

    Path path = Paths.get(toFile);
    if(!path.getParent().toFile().isDirectory()){
        new File(path.getParent().toString()).mkdirs();
    }

    Files.write(path,
        new ObjectMapper().writeValueAsString(InsertCollectorsWebRequest.builder()
            .events(events).build()).getBytes());
  }


  @SneakyThrows
  private void saveToFile(String targetFolder, String targetFileName, String content){
    new File(targetFolder).mkdirs();
    FileUtils.writeStringToFile(
        new File(targetFolder + "\\" + targetFileName),content);
  }

  @SneakyThrows
  private void saveListToFile(List<String> data, String toFile){
    File file = new File(toFile);
    FileUtils.writeLines(file, data, false);
  }

}
