package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  private List<List<Integer>> matrixList = new ArrayList<>();
  private int hashCode;

  /**
   * загружает матрицу из файла
   * @param fileName - имя файла
   */
  public DenseMatrix(String fileName)
  {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
    {
      String line = reader.readLine();
      int w = 0;
      if (line != null)
      {
        w = line.split(" ").length;
      }
      while (line != null)
      {
        List<Integer> buffer =
                Arrays.stream(line.split(" "))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
        if (w != buffer.size())
        {
          this.matrixList = null;
          throw new IOException("Incorrect format of the matrix in the file");
        }
        this.matrixList.add(buffer);
        line = reader.readLine();
      }
      this.hashCode = this.hashCodeCalculate();
    }
    catch (IOException ex)
    {
      System.out.println(ex.getMessage());
    }
  }
  public DenseMatrix(List<List<Integer>> list)
  {
    try
    {
      if (!list.isEmpty())
      {
        int w = list.get(0).size();
        for (List<Integer> integers: list)
          if (w != integers.size()) throw new IOException("Incorrect format of the matrix in the list");
      }
    }
    catch (IOException ex)
    {
      System.out.println(ex.getMessage());
    }
    this.matrixList = list;
    this.hashCode = this.hashCodeCalculate();
  }
  private List<List<Integer>> getMatrixList()
  {
    return this.matrixList;
  }

  /**
   * однопоточное умножение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o - второй множитель
   * @return - результат умножения
   */
  @Override public Matrix mul(Matrix o)
  {
    List<List<Integer>> result = new ArrayList<>();
    try {
      List<List<Integer>> m1 = this.matrixList;
      List<List<Integer>> m2 = ((DenseMatrix)o).getMatrixList();

      if (m1.isEmpty()) return o;
      if (m2.isEmpty()) return this;
      if (m1.size() != m2.get(0).size()) throw new IllegalArgumentException("Incorrect input matrix sizes");

      for (List<Integer> integers : m1) {
        List<Integer> raw = new ArrayList<>();
        for (int j = 0; j < m2.get(0).size(); j++) {
          int cell = 0;
          for (int k = 0; k < m2.size(); k++) {
            cell += integers.get(k) * m2.get(k).get(j);
          }
          raw.add(cell);
        }
        result.add(raw);
      }
    }
    catch (IllegalArgumentException ex)
    {
      System.out.println(ex.getMessage());
    }
    return new DenseMatrix(result);
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o - второй множитель
   * @return - результат умножения
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  /**
   * сравнивает с обоими вариантами
   * @param o - с чем сравинвается
   * @return - результат сравнения
   */
  @Override public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o != null && o.getClass() == this.getClass())) return false;
    return this.matrixList.equals(((DenseMatrix) o).getMatrixList());
  }

  @Override public String toString()
  {
    if (this.matrixList == null) return "";
    StringBuilder result = new StringBuilder();
    for (List<Integer> str: this.matrixList)
    {
      for (int number: str)
      {
        result.append(number).append(" ");
      }
      result.append("\n");
    }
    return result.toString();
  }

  private int hashCodeCalculate()
  {
    int hashCode = 1;
    for (List<Integer> integers: this.matrixList)
    {
      hashCode = 31 * hashCode + (integers==null ? 0 : integers.hashCode());
    }
    return hashCode;
  }
  @Override public int hashCode()
  {
    return this.hashCode;
  }
}
