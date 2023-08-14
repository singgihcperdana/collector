package com.collector.model;

import com.collector.enums.CollectorRoute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertCollectorsWebRequest {

  private List<Event> events;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Event {
    private String entityId;
    private CollectorRoute collectorType;
    private int collectorPriority;
  }

}

