package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort {
  private static void merge (int[] firstArr, int[] secondArr, int firstStart, int secondStart, int[] result, int resultStart, int size) {
    int index1 = firstStart;
    int index2 = secondStart;

    int firstEnd = Math.min(firstStart + size, firstArr.length);
    int secondEnd = Math.min(secondStart + size, secondArr.length);

    int iterationCount = (firstEnd - firstStart) + (secondEnd - secondStart);

    for (int i = resultStart; i < resultStart + iterationCount; i++) {
      if (index1 < firstEnd && (index2 >= secondEnd || firstArr[index1] < secondArr[index2])) {
        result[i] = firstArr[index1];
        index1++;
      } else {
        result[i] = secondArr[index2];
        index2++;
      }
    }
  }

  public static void sort (int[] array) {
    int[] buffer = new int[array.length];

    int size = 1;
    while (size < array.length) {
      for (int i = 0; i < array.length; i += 2 * size) {
        merge(array, array, i, Math.min(i + size, array.length), buffer, i, size);
      }
      for (int i = 0; i < array.length; i++) {
        array[i] = array[i] + buffer[i] - (buffer[i] = array[i]);
      }
      size *= 2;
    }
  }

  public static void sort (List<Integer> list) {
    Collections.sort(list);
  }
}
