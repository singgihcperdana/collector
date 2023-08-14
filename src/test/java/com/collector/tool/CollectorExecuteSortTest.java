package com.collector.tool;

import com.collector.enums.CollectorRoute;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * listing folder mana yg akan seharusnya diexecute terlebih dahulu
 * */
public class CollectorExecuteSortTest {

  @Test
  public void collectorExecuteSortTest(){

//    String folderPath = "D:\\kemdikbuk collector\\juli 2023\\entityId";
    String folderPath = "C:\\Users\\singgih.perdana\\Documents\\perbaikan data agustus 2023\\csv\\payload";

    List<CollectorRoute> collectorRoutes = Arrays.stream(new File(folderPath).listFiles())
        .map(file -> file.getName().replace(".json", ""))
        .map(CollectorRoute::valueOf)
        .collect(Collectors.toList());

    CollectorRoute.sort(collectorRoutes);

    collectorRoutes.forEach(collectorRoute->
        System.out.println(collectorRoute.getPriority() + "." + collectorRoute.name()));
    System.out.println("================================");
    System.out.println("TOTAL= " + collectorRoutes.size());

  }

}
