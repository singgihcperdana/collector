package com.collector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckDuplicateTest {

  @Test
  public void checkDuplicateInFolder(){

    Map<String, Integer> dataCounter = new HashMap<>();
    File[] files = new File("D:\\kemdikbuk collector\\juli 2023\\payload\\ORDER_BAST_GENERATED")
        .listFiles();

    for (File file : files) {
      checkDuplicate(file, dataCounter);
    }

    dataCounter.entrySet().stream()
        .filter(d->d.getValue()>1)
        .forEach(d-> System.out.println("d = " + d));
  }

  @Test
  public void checkDuplicateInFile(){
    Map<String, Integer> dataCounter = new HashMap<>();
    checkDuplicate(new File("D:\\kemdikbuk collector\\juli 2023\\payload\\ORDER_BAST_GENERATED\\AA_1.json"), dataCounter);
    dataCounter.entrySet().stream()
        .filter(d->d.getValue()>1)
        .forEach(d-> System.out.println("d = " + d));
  }

  @SneakyThrows
  public void checkDuplicate(File file, Map<String, Integer> dataCounter){
       TypeReference<Map<String, List<Map<String, Object>>>> typeRef = new TypeReference<Map<String, List<Map<String, Object>>>>() {};
    Map<String, List<Map<String, Object>>> eventsMap = new ObjectMapper().readValue(file, typeRef);
    List<Map<String, Object>> events = eventsMap.get("events");
    events.forEach(event-> dataCounter.put(event.get("entityId")+"",
        dataCounter.getOrDefault(dataCounter.get("entityId"), 0) + 1));
  }

}
