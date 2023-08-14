package com.collector;

import com.collector.utils.CommonsCollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommonsCollectionUtilsTest {

  @ParameterizedTest
  @CsvSource({
      "10, 20, 2, 10",
      "10, 32, 4, 2",
      "10, 59, 6, 9",
      "5, 22, 5, 2",
      "5, 20, 4, 5",
  })
  public void partitionList(int partitionSize, int countData, int partitionCount, int lastPartitionSize) {

    List<Person> people = populatePerson(countData);

    Map<Integer, List<Person>> mapPartitioned =
        CommonsCollectionUtils.toMapPartitionList(people, partitionSize);

    Assertions.assertEquals(partitionCount, mapPartitioned.size());

    Map.Entry<Integer, List<Person>> firstEntry = mapPartitioned.entrySet().stream().findFirst().get();
    Assertions.assertEquals(partitionSize, firstEntry.getValue().size());

    Map.Entry<Integer, List<Person>> lastEntry =
        mapPartitioned.entrySet().stream().reduce((first,second) -> second).get();
    Assertions.assertEquals(lastPartitionSize, lastEntry.getValue().size());
  }

  @ParameterizedTest
  @CsvSource({
      "10, 20, 2, 10",
      "10, 32, 4, 2",
      "10, 59, 6, 9"
  })
  public void partitionListWithMapper(int partitionSize, int countData, int partitionCount, int lastPartitionSize){
    List<Integer> collectionId = IntStream.range(1, countData+1)
        .boxed()
        .collect(Collectors.toList());

    Map<Integer, List<Person>> mapPartitioned = CommonsCollectionUtils.toMapPartitionList(
        collectionId, partitionSize, id->new Person(id, "person-"+id));

    Assertions.assertEquals(partitionCount, mapPartitioned.size());

    Map.Entry<Integer,  List<Person>> firstEntry = mapPartitioned.entrySet().stream().findFirst().get();
    Assertions.assertEquals(partitionSize, firstEntry.getValue().size());

    Map.Entry<Integer, List<Person>> lastEntry = mapPartitioned.entrySet().stream()
        .reduce((first,second) -> second).get();
    Assertions.assertEquals(lastPartitionSize, lastEntry.getValue().size());
  }

  @Getter
  @Setter
  @AllArgsConstructor
  private class Person {
    private int id;
    private String name;
  }

  List<Person> populatePerson(int countData){
    return IntStream.range(0, countData)
        .mapToObj(value ->  new Person(value , "person-" + value))
        .collect(Collectors.toList());
  }

}
