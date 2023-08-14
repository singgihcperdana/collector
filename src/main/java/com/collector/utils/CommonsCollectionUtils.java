package com.collector.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonsCollectionUtils {

  public static <E> Map<Integer, List<E>> toMapPartitionList(Collection<E> collection, int size) {
    return toMapPartitionList(collection, size, Function.identity());
  }

  public static <E, R> Map<Integer, List<R>> toMapPartitionList(
      Collection<E> collection, int size, Function<E, R> mapper) {

    int totalPartition = collection.size() / size;
    int remain = collection.size() % size;
    if(remain > 0){
      totalPartition++;
    }

    int partitionNumber=0;
    int idx=0;
    int idxMax=0;

    Map<Integer, List<R>> result = new HashMap<>();

    List<E> data = new ArrayList<>(collection);

    while (partitionNumber < totalPartition){
      idxMax = partitionNumber++ == totalPartition-1 && remain>0 ? idxMax+remain : idxMax+size;
      List<R> partitionedList = new ArrayList<>();
      while (idx<idxMax){
        partitionedList.add(mapper.apply(data.get(idx++)));
      }
      result.put(partitionNumber, partitionedList);
    }
    return result;
  }

  public static void sortContainingNumber(List<String> records){
    Collections.sort(records, new Comparator<String>() {
      public int compare(String o1, String o2) {
        String o1StringPart = o1.replaceAll("\\d", "");
        String o2StringPart = o2.replaceAll("\\d", "");
        if(o1StringPart.equalsIgnoreCase(o2StringPart)){
          return extractInt(o1) - extractInt(o2);
        }
        return o1.compareTo(o2);
      }

      int extractInt(String s) {
        String num = s.replaceAll("\\D", "");
        // return 0 if no digits found
        return num.isEmpty() ? 0 : Integer.parseInt(num);
      }
    });
  }

}
