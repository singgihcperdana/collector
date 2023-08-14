package com.collector.tool;

import com.collector.enums.CollectorRoute;
import com.collector.model.InsertCollectorsWebRequest;
import com.collector.utils.CollectorUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CollectorPayloadMultipleCollectorTest {

  private final int size = 1000;

  private final CollectorRoute[] collectorRoutes = {
      CollectorRoute.ORDER_CREATED,
      CollectorRoute.ORDER_PROCESSED,
      CollectorRoute.ORDER_SHIPPED,
      CollectorRoute.ORDER_RECEIVED,
      CollectorRoute.ORDER_BAST_GENERATED,
      CollectorRoute.PAYMENT_CONFIRMED,
      CollectorRoute.PAYMENT_SETTLED
  };

  @Test
  @SneakyThrows
  public void generateJsonPayloadInCollectorType(){

    String filename = "AU.CSV";
    String folder1 = "D:\\kemdikbuk collector\\juli 2023\\";

    File file = new File(folder1 + filename);

    for (CollectorRoute collectorRoute : collectorRoutes) {
      Map<Integer, List<InsertCollectorsWebRequest.Event>> data =
          CollectorUtils.getData(file, collectorRoute, size);

      File folder = new File(file.getParent() + "\\payload\\" + collectorRoute.name());

      if(!folder.isDirectory()){
        folder.mkdirs();
      }

      int key = 0;
      for (Map.Entry<Integer,List<InsertCollectorsWebRequest.Event>> entry : data.entrySet()){
        CollectorUtils.saveEventsToFile(
            entry.getValue(),
            folder.getAbsolutePath() + "\\"+FilenameUtils.getBaseName(filename)+"_" + (++key) + ".json");
        System.out.println(collectorRoute + " > page: " + key + ", data = " + entry.getValue().size());
      }
    }
  }

}
