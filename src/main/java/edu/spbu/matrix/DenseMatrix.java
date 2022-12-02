package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  private List<List<Double>> matrixList = new ArrayList<>();
  public int hashCode = 0;

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
        List<Double> buffer =
                Arrays.stream(line.split(" "))
                .mapToDouble(Double::parseDouble)
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
      hashCode = this.matrixList.hashCode();
    }
    catch (IOException ex)
    {
      System.out.println(ex.getMessage());
    }
  }
  public DenseMatrix(List<List<Double>> list)
  {
    try
    {
      if (!list.isEmpty())
      {
        int w = list.get(0).size();
        for (List<Double> doubles: list)
          if (w != doubles.size()) throw new IOException("Incorrect format of the matrix in the list");
      }
    }
    catch (IOException ex)
    {
      System.out.println(ex.getMessage());
    }
    this.matrixList = list;
    hashCode = this.matrixList.hashCode();
  }

  public DenseMatrix(int h, int w)
  {
    List<Double> temp = new ArrayList<>();
    for (int i = 0; i < w; i++)
    {
      temp.add((double) 0);
    }
    for (int i = 0; i < w; i++)
    {
      matrixList.add(temp);
    }
    hashCode = this.matrixList.hashCode();
  }

  public List<List<Double>> getMatrixList()
  {
    return this.matrixList;
  }

  public int getH()
  {
    return this.matrixList.size();
  }

  public int getW()
  {
    if (this.getH() == 0) return 0;
    return this.matrixList.get(0).size();
  }

  public double getElement(int i, int j)
  {
    return this.matrixList.get(i).get(j);
  }

  /**
   * однопоточное умножение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o - второй множитель
   * @return - результат умножения
   */
  @Override public Matrix mul(Matrix o) {
    if (o instanceof DenseMatrix) {
      List<List<Double>> result = new ArrayList<>();
      try {
        List<List<Double>> m1 = this.matrixList;
        List<List<Double>> m2 = ((DenseMatrix) o).getMatrixList();

        if (m1.isEmpty()) return o;
        if (m2.isEmpty()) return this;
        if (m2.size() != m1.get(0).size()) throw new IllegalArgumentException("Incorrect input matrix sizes");

        for (List<Double> integers : m1) {
          List<Double> raw = new ArrayList<>();
          for (int j = 0; j < m2.get(0).size(); j++) {
            double cell = 0;
            for (int k = 0; k < m2.size(); k++) {
              cell += integers.get(k) * m2.get(k).get(j);
            }
            raw.add(cell);
          }
          result.add(raw);
        }
      } catch (IllegalArgumentException ex) {
        System.out.println(ex.getMessage());
        return new DenseMatrix(0, 0);
      }
      return new DenseMatrix(result);
    }
    else
    {
      if (this.matrixList.isEmpty()) return o;
      if (((SparseMatrix)o).getMatrixHashMap().isEmpty()) return this;

      try {
        if (((SparseMatrix) o).getH() != this.getW())
          throw new IllegalArgumentException("Incorrect input matrix sizes");
      } catch (IllegalArgumentException ex)
      {
        System.out.println(ex.getMessage());
        return new SparseMatrix(0, 0);
      }

      DenseMatrix m1 = this;
      SparseMatrix m2 = ((SparseMatrix) o).matrixTransposition();
      SparseMatrix result = new SparseMatrix(m1.getH(), m2.getH());
      for (int i = 0; i < m1.getH(); i++) {
        for (Map.Entry<Integer, Map<Integer, Double>> lineMatrix2 : m2.getMatrixHashMap().entrySet()) {
          for (Map.Entry<Integer, Double> currentElemMatrix2 : lineMatrix2.getValue().entrySet()) {
            int j = lineMatrix2.getKey();
            int k = currentElemMatrix2.getKey();
            double value = result.getElement(i, j) + m1.getElement(i, k) * m2.getElement(j, k);
            if (value == 0) continue;
            result.putElement(i, j, value);
            }
          }
        }
      result.hashCode = result.getMatrixHashMap().hashCode();
      return result;
    }
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
    if (o instanceof DenseMatrix)
    {
      if (this == o) return true;
      if (this.hashCode() != o.hashCode()) return false;
      return this.matrixList.equals(((DenseMatrix) o).getMatrixList());
    }
    else if (o instanceof SparseMatrix)
    {

      if (((SparseMatrix) o).getH() != this.getH() || ((SparseMatrix) o).getW() != this.getW())
        return false;
      for (int i = 0; i < this.getH(); i++)
      {
        for (int j = 0; j < this.getW(); j++)
        {
          if (this.getElement(i, j) != ((SparseMatrix) o).getElement(i, j))
            return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override public String toString()
  {
    if (this.matrixList == null) return "";
    StringBuilder result = new StringBuilder();
    for (List<Double> str: this.matrixList)
    {
      for (double number: str)
      {
        result.append(number).append(" ");
      }
      result.append("\n");
    }
    return result.toString();
  }

  private void hashCodeCalculate()
  {
    this.hashCode = this.matrixList.hashCode();
  }
  @Override public int hashCode()
  {
    return this.hashCode;
  }
}
