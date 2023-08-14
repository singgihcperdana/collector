package com.collector;

import com.collector.enums.CollectorRoute;
import com.collector.model.InsertCollectorsWebRequest;
import com.collector.utils.CollectorUtils;
import com.collector.utils.CommonsCollectionUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class A {

  private final Long ONE_HOUR = 3600000L;
  Integer countData = 0;

  @Test
  public void countDaysBetweenDates(){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    String fromDate = "27/01/2023";
    String toDate = "30/06/2023";

    //convert String to LocalDate
    LocalDate startDate = LocalDate.parse(fromDate, formatter);
    LocalDate endDate = LocalDate.parse(toDate, formatter);
    long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

    System.out.println("numOfDaysBetween = " + numOfDaysBetween);
  }

  @Test
  @SneakyThrows
  public void generateEpoch(){

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    String fromDate = "27/01/2023";
    String toDate = "27/06/2023";

    //convert String to LocalDate
    LocalDate startDate = LocalDate.parse(fromDate, formatter);
    LocalDate endDate = LocalDate.parse(toDate, formatter);

    int everyHours = 24;
    FileWriter fileWriter = new FileWriter("C:\\Users\\singgih.perdana\\Documents\\date_range.txt");
    for (LocalDate localDate : getDatesBetween(startDate, endDate)) {
       generateEveryNHours(fileWriter, localDate, everyHours);
    }
    fileWriter.close();
    System.out.println("countData = " + countData);
  }

  private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
    long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
    System.out.println("numOfDaysBetween = " + numOfDaysBetween);
    return IntStream.iterate(0, i -> i + 1)
        .limit(numOfDaysBetween)
        .mapToObj(startDate::plusDays)
        .collect(Collectors.toList());
  }

  private List<String> generateListEveryNHours(LocalDate localDate, int everyHours){
    Long epochHour = ONE_HOUR * everyHours;
    Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Long startEpoch = instant.toEpochMilli();

    List<String> epochs = new ArrayList<>();

    for(int i=1; i<= (24/everyHours); i++){
      epochs.add((startEpoch + (epochHour* (i-1))) + "|" + (startEpoch + (epochHour*i) - 1000));
    }

    return epochs;
  }

  @SneakyThrows
  private void generateEveryNHours(FileWriter fileWriter, LocalDate localDate, int everyHours){
    Long epochHour = ONE_HOUR * everyHours;
    Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    Long startEpoch = instant.toEpochMilli();

    for(int i=1; i<= (24/everyHours); i++){
      countData++;
      fileWriter.write((startEpoch + (epochHour* (i-1))) + "|" + (startEpoch + (epochHour*i) - 1000) + System.lineSeparator());
    }
  }

  private  List<InsertCollectorsWebRequest.Event> populateEvents(int size){
    List<InsertCollectorsWebRequest.Event> events = new ArrayList<>();

    for(int i=0; i<size; i++){
      events.add(new InsertCollectorsWebRequest.Event(
          i+"", CollectorRoute.PRODUCT_VERIFIED, 1));
    }
    return events;
  }

  @Test
  public void bbb(){
    List<InsertCollectorsWebRequest.Event> events = populateEvents(37);

    Map<Integer, List<InsertCollectorsWebRequest.Event>> map =
        CommonsCollectionUtils.toMapPartitionList(events, 10);

    map.forEach((key, value) -> System.out.println("data = " + key + ":::" + value.size()));
  }

  @Test
  @SneakyThrows
  public void splitFileExecuted() {

    AtomicInteger index = new AtomicInteger(0);
        CollectorUtils
        .getEntityIds(new File("C:\\Users\\singgih.perdana\\Desktop\\log insert event.logioio"))
        .stream()
        .filter(line->line.contains(".json"))
        .filter(line->!line.contains("error"))
        .filter(line->!line.contains("AB"))
        .filter(line->!line.contains("AC_1"))
        .map(line-> {
          String lines[] = line.split(".json");
          String fileAs[] = lines[0].split(" ");
          return fileAs[fileAs.length-1] + ".json";
        })
        .forEach(fileName->{
          index.getAndIncrement();
          if(index.get()==2){
            return;
          }
          File file = new File("D:\\kemdikbuk collector\\juli 2023\\payload\\ORDER_CREATED\\" + fileName);

          try {
            Files.move(file.toPath(),
                new File("D:\\kemdikbuk collector\\juli 2023\\payload\\ORDER_CREATED_EXECUTED\\" + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
          } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }
        });
//        .collect(Collectors.toList());

//    System.out.println("fileJsons = " + fileJsons);
//    System.out.println("fileJsons = " + fileJsons.size());
  }


  @Test
  @SneakyThrows
  public void sortContainingNumberTest() {

    List<String> fileNames = Arrays.stream(new File("D:\\kemdikbuk collector\\juli 2023\\payload\\ORDER_CREATED").listFiles())
        .map(File::getName)
        .collect(Collectors.toList());
    System.out.println("before sort fileNames = " + fileNames);
    CommonsCollectionUtils.sortContainingNumber(fileNames);
    System.out.println("after sort fileNames = " + fileNames);

  }

  @Test
  @SneakyThrows
  public void processTime() {
//    Instant start = Instant.now();
//
//    Instant end = Instant.now();
//    System.out.println(Duration.between(start, end).get(ChronoUnit.NANOS)/1000000);

    StopWatch timer = new StopWatch();
timer.start();
    Thread.sleep(2*100);

    timer.stop();
    long durationInMs = TimeUnit.MILLISECONDS.convert(timer.getNanoTime(), TimeUnit.NANOSECONDS);
    System.out.println(durationInMs);
  }

  @Test
  @SneakyThrows
  public void a(){
      String file = "D:\\kemdikbuk collector\\juli 2023\\S.csv";

    System.out.println("file = " + CollectorUtils.getEntityIds(new File(file)).size());
  }


  @Test
  public void collectRemoveDuplicate() throws IOException {

    String FOLDER_CSV = "D:\\kemdikbuk collector\\juli 2023\\data_from_db\\merchant-result\\payload\\MERCHANT_CREATED";

    Set<String> set = new HashSet<>();

    List<Map<String, Object>> temp = new ArrayList<>();
    for (File file : new File(FOLDER_CSV).listFiles()) {
      System.out.println("file = " + file.getAbsolutePath());
      TypeReference<Map<String, List<Map<String, Object>>>> typeRef = new TypeReference<Map<String, List<Map<String, Object>>>>() {};
      Map<String, List<Map<String, Object>>> eventsMap = new ObjectMapper().readValue(file, typeRef);
      List<Map<String, Object>> events = eventsMap.get("events");

      events.forEach(event->{
        String entityId = event.get("entityId")+"";
        if(set.add(entityId)){
          temp.add(event);
        }
      });
    }

    System.out.println("temp = " + temp.size());

  }

}
