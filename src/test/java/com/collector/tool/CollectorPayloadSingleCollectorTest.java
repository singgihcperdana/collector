package com.collector.tool;

import com.collector.enums.CollectorRoute;
import com.collector.model.InsertCollectorsWebRequest;
import com.collector.utils.CollectorUtils;
import com.collector.utils.CommonsCollectionUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CollectorPayloadSingleCollectorTest {
  private final int PARTITION_LIMIT = 5000;
  private final String FOLDER_CSV = "C:\\Users\\singgih.perdana\\Documents\\perbaikan data agustus 2023\\csv";
  private final CollectorRoute COLLECTOR_ROUTE = CollectorRoute.ORDER_BAST_GENERATED;
  private final String FOLDER_PAYLOAD = FOLDER_CSV +"\\payload";

  @Test
  public void generatePayload(){

    //folder name or filename separated by comma
//    String file = FOLDER_CSV;
//    String file = "A.csv,D.csv,J.csv";
    String file = "B.csv,E.csv,F.csv,G.csv,H.csv,I.csv,J.csv";

    System.out.println("====================================");

    Set<String> entityIds = getFilenames(file)
        .stream()
        .map(this::collectEntityIds)
        .collect(HashSet::new, Set::addAll, Set::addAll);

    System.out.println("entityIds without duplicates: " + entityIds.size());

    if(entityIds.size() > PARTITION_LIMIT){
      generateWithLimit(entityIds);
    } else {
      generateNoLimit(entityIds);
    }
  }

  private List<String> getFilenames(String file){
    File folder = new File(file);
    if(folder.isDirectory()) {
      return Arrays.stream(folder.listFiles())
          .map(File::getName)
          .collect(Collectors.toList());
    }
    return Arrays.stream(file.split(","))
        .filter(StringUtils::isNotEmpty)
        .map(String::trim)
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private  List<String> collectEntityIds(String filename){
    return CollectorUtils.getEntityIds(new File(FOLDER_CSV + "\\" + filename));
  }

  @SneakyThrows
  private void generateNoLimit(Set<String> entityIds) {
    List<InsertCollectorsWebRequest.Event> events = entityIds
        .stream()
        .map(id-> InsertCollectorsWebRequest.Event.builder().collectorType(COLLECTOR_ROUTE)
            .collectorPriority(0).entityId(id).build())
        .collect(Collectors.toList());

    String toFile = FOLDER_PAYLOAD +"\\"+ COLLECTOR_ROUTE.name() + ".json";
    CollectorUtils.saveEventsToFile(events, toFile);
    System.out.println("saved to: " +toFile);
    System.out.println("total data: "+ events.size());
  }

  @SneakyThrows
  private void generateWithLimit(Set<String> entityIds) {
    Map<Integer, List<InsertCollectorsWebRequest.Event>> map =
        CommonsCollectionUtils.toMapPartitionList(entityIds, PARTITION_LIMIT,
            id->InsertCollectorsWebRequest.Event.builder()
                .collectorType(COLLECTOR_ROUTE)
                .collectorPriority(0).entityId(id).build()
        );

    AtomicInteger totalData = new AtomicInteger();
    map.forEach((partitionNumber, events) -> {
      String toFile = FOLDER_PAYLOAD +"\\"+COLLECTOR_ROUTE.name()+ "\\part_" + partitionNumber + ".json";
      System.out.println(FilenameUtils.getName(toFile) + " > " + events.size());
      CollectorUtils.saveEventsToFile(events, toFile);
      totalData.addAndGet(events.size());
    });

    System.out.println("total data " + COLLECTOR_ROUTE.name() + ": "+ totalData.get());
    System.out.println("====================================");
  }

}
