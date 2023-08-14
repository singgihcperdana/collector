package com.collector.tool;

import com.collector.enums.CollectorRoute;
import com.collector.model.InsertCollectorsWebRequest;
import com.collector.utils.CollectorUtils;
import com.collector.utils.CommonsCollectionUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectorPayloadByMappingTest {

  private String filename = "BB.CSV";
  private String FOLDER_CSV = "D:\\kemdikbuk collector\\juli 2023";

  @Test
  @SneakyThrows
  public void collectorPayloadByMappingTest(){

    Map<String, CollectorRoute> mapCollector = CollectorUtils.MAP_COLLECTOR;

    Map<CollectorRoute, List<String>> data = new HashMap<>();

    mapCollector.forEach((key1, value) -> data.put(value, new ArrayList<>()));

    CollectorUtils.getEntityIds(new File(FOLDER_CSV+"\\"+filename))
        .stream()
        .map(record-> record.split(","))
        .forEach(records -> data.get(mapCollector.get(records[1])).add(records[0]));

    AtomicInteger total = new AtomicInteger();
    data.entrySet()
        .stream()
        .filter(keyVal->!keyVal.getValue().isEmpty())
        .forEach(keyVal->{
            System.out.println(keyVal.getKey()+ " > " + keyVal.getValue().size());

            CollectorRoute collectorRoute = keyVal.getKey();

            Map<Integer, List<InsertCollectorsWebRequest.Event>> mapEvents =
                CommonsCollectionUtils.toMapPartitionList(keyVal.getValue(), 1000,
                    entityId -> new InsertCollectorsWebRequest.Event(entityId, collectorRoute, 0));

            File folder = new File(FOLDER_CSV + "\\payload\\" + collectorRoute.name());

            if(!folder.isDirectory()){
              folder.mkdirs();
            }

            int key = 0;
            for (Map.Entry<Integer,List<InsertCollectorsWebRequest.Event>> entry : mapEvents.entrySet()){
              CollectorUtils.saveEventsToFile(
                  entry.getValue(),
                  folder.getAbsolutePath() + "\\"+ FilenameUtils.getBaseName(filename)+"_" + (++key) + ".json");
      //        System.out.println(collectorType + " > page: " + key + ", data = " + entry.getValue().size());
              total.addAndGet(entry.getValue().size());
      }
    });
    System.out.println("===============================");
    System.out.println("total = " + total);
  }
}
