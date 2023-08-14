package com.collector.tool;

import com.collector.enums.CollectorRoute;
import com.collector.utils.CollectorUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectorEntityIdByMappingTest {

  private final String filename = "BB.CSV";
  private final String FOLDER_CSV = "D:\\kemdikbuk collector\\juli 2023";

  @Test
  @SneakyThrows
  public void collectorPayloadByMappingTest(){

    Map<String, CollectorRoute> mapCollector = CollectorUtils.MAP_COLLECTOR;

    Map<CollectorRoute, List<String>> data = new HashMap<>();

    mapCollector.forEach((key, value) -> data.put(value, new ArrayList<>()));

    CollectorUtils.getEntityIds(new File(FOLDER_CSV+"\\"+filename))
        .stream()
        .map(csvRecord-> csvRecord.split(","))
        .forEach(csvRecords -> data.get(mapCollector.get(csvRecords[1])).add(csvRecords[0] + System.lineSeparator()));

    data.entrySet()
        .stream()
        .filter(keyVal->!keyVal.getValue().isEmpty())
        .forEach(keyVal->{
          System.out.println(keyVal.getKey()+ " > " + keyVal.getValue().size());

          CollectorRoute collectorRoute = keyVal.getKey();

          File folder = new File(FOLDER_CSV + "\\entityId\\" + collectorRoute.name());

          if(!folder.isDirectory()){
            folder.mkdirs();
          }

          String toFile = folder.getAbsolutePath() + "\\" + filename;

          writeListToFile(keyVal.getValue(), toFile);

        });
    System.out.println("===============================");
  }

  @SneakyThrows
  private void writeListToFile(List<String> entityIds, String toFile){
    FileWriter writer = new FileWriter(toFile);
    entityIds.forEach(s -> {
      try {
        writer.write(s);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    writer.close();
  }

}
