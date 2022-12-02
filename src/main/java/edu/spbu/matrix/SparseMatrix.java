package edu.spbu.matrix;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix
{

  private Map<Integer, Map<Integer, Double>> matrixHashMap = new HashMap<>();
  private final int[] matrixSize = {0, 0};
  public int hashCode = 0;

  public int getH()
  {
    return matrixSize[0];
  }

  public int getW()
  {
    return matrixSize[1];
  }
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName)
  {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
    {
      String line = reader.readLine();
      int w = 0;
      if (line != null)
      {
        w = line.split(" ").length;
        matrixSize[1] = w;
      }
      else
      {
        matrixHashMap = new HashMap<>();
        matrixSize[0] = 0;
        matrixSize[1] = 0;
      }

      int h = 0;
      while (line != null)
      {
        List<Double> temp =
                Arrays.stream(line.split(" "))
                        .mapToDouble(Double::parseDouble)
                        .boxed()
                        .collect(Collectors.toList());
        if (w != temp.size())
        {
          matrixHashMap = null;
          throw new IOException("Incorrect format of the matrix in the file");
        }
        HashMap<Integer, Double> buffer = new HashMap<>();
        for (int i = 0; i < temp.size(); i++)
        {
          double number = temp.get(i);
          if (number != 0)
          {
            buffer.put(i, number);
          }
        }
        if (!buffer.isEmpty()) matrixHashMap.put(h, buffer);
        line = reader.readLine();
        h++;
      }
      hashCode = this.matrixHashMap.hashCode();
      matrixSize[0] = h;
    }
    catch (Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }

  public SparseMatrix(int h, int w)
  {
    matrixHashMap = new HashMap<>();
    if (h <= 0 || w <= 0)
      return;
    matrixSize[0] = h;
    matrixSize[1] = w;
    hashCode = this.matrixHashMap.hashCode();
  }

  public SparseMatrix(double[][] arr)
  {
    matrixHashMap = new HashMap<>();
    matrixSize[0] = arr.length;
    matrixSize[1] = arr[0].length;
    for (int i = 0; i < arr.length; i++)
    {
      for (int j = 0; j < arr[0].length; j++)
      {
        if (arr[i][j] != 0)
        {
          if (matrixHashMap.containsKey(i))
            matrixHashMap.get(i).put(j, arr[i][j]);
          else
          {
            HashMap<Integer, Double> temp = new HashMap<>();
            temp.put(j, arr[i][j]);
            matrixHashMap.put(i, temp);
          }
        }
      }
    }
    hashCode = this.matrixHashMap.hashCode();
  }

  public Map<Integer, Map<Integer, Double>> getMatrixHashMap()
  {
    return this.matrixHashMap;
  }

  public double getElement(int i, int j)
  {
    if (matrixHashMap.containsKey(i)){
      Map<Integer, Double> raw = matrixHashMap.get(i);
      return raw.getOrDefault(j, (double) 0);
    }
    return 0;
  }

  public void putElement(int i, int j, double value) {
    Map<Integer, Double> line = matrixHashMap.get(i);
    if (line != null)
      line.put(j, value);
    else {
      HashMap<Integer, Double> temp = new HashMap<>();
      temp.put(j, value);
      matrixHashMap.put(i, temp);
    }
  }

  public SparseMatrix matrixTransposition()
  {
    SparseMatrix result = new SparseMatrix(this.getW(), this.getH());
    for (int i = 0; i < this.getW(); i++)
    {
      for (int j = 0; j < this.getH(); j++)
      {
        double current = this.getElement(j,i);
        if (current != 0)
        {
          if (result.matrixHashMap.containsKey(i))
            result.matrixHashMap.get(i).put(j, current);
          else{
            HashMap<Integer, Double> temp = new HashMap<>();
            temp.put(j, current);
            result.matrixHashMap.put(i, temp);
          }
        }
      }
    }
    return result;
  }

  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o) {
    if (o instanceof SparseMatrix) {
      if (this.matrixHashMap.isEmpty()) return o;
      if (((SparseMatrix) o).matrixHashMap.isEmpty()) return this;

      try {
        if (((SparseMatrix) o).getH() != this.getW())
          throw new IllegalArgumentException("Incorrect input matrix sizes");
      } catch (IllegalArgumentException ex) {
        System.out.println(ex.getMessage());
        return new SparseMatrix(0, 0);
      }

      SparseMatrix m1 = this;
      SparseMatrix m2 = ((SparseMatrix) o).matrixTransposition();
      SparseMatrix result = new SparseMatrix(m1.getH(), ((SparseMatrix) o).getW());
      for (Map.Entry<Integer, Map<Integer, Double>> lineMatrix1 : m1.getMatrixHashMap().entrySet()) {
        for (Map.Entry<Integer, Map<Integer, Double>> lineMatrix2 : m2.getMatrixHashMap().entrySet()) {
          for (Map.Entry<Integer, Double> currentElemMatrix1 : lineMatrix1.getValue().entrySet()) {
            if (lineMatrix2.getValue().containsKey(currentElemMatrix1.getKey())) {
              int i = lineMatrix1.getKey();
              int j = lineMatrix2.getKey();
              int k = currentElemMatrix1.getKey();
              double value = result.getElement(i, j) + currentElemMatrix1.getValue() * lineMatrix2.getValue().get(k);
              if (value == 0) continue;
              result.putElement(i, j, value);
            }
          }
        }
      }
      result.hashCode = result.matrixHashMap.hashCode();
      return result;
    }
    else
    {
      if (this.matrixHashMap.isEmpty()) return o;
      if (((DenseMatrix)o).getMatrixList().isEmpty()) return this;

      try {if (((DenseMatrix) o).getH() != this.getW()) throw new IllegalArgumentException("Incorrect input matrix sizes");}
      catch (IllegalArgumentException ex)
      {
        System.out.println(ex.getMessage());
        return new SparseMatrix(0, 0);
      }

      SparseMatrix m1 = this;
      DenseMatrix m2 = (DenseMatrix) o;
      SparseMatrix result = new SparseMatrix(m1.getH(), m2.getW());
      for (Map.Entry<Integer, Map<Integer, Double>> lineMatrix1 : m1.getMatrixHashMap().entrySet()) {
        for (int j = 0; j < m2.getW(); j++) {
          for (Map.Entry<Integer, Double> currentElemMatrix1 : lineMatrix1.getValue().entrySet()) {
            int i = lineMatrix1.getKey();
            int k = currentElemMatrix1.getKey();
            double value = result.getElement(i, j) + currentElemMatrix1.getValue() * m2.getElement(k, j);
            if (value == 0) continue;
            result.putElement(i, j, value);
            }
          }
        }
      result.hashCode = result.matrixHashMap.hashCode();
      return result;
    }
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o)
  {
    if (o instanceof SparseMatrix)
    {
      if (((SparseMatrix) o).getH() != this.getH() || ((SparseMatrix) o).getW() != this.getW())
        return false;
      if (!matrixHashMap.keySet().equals(((SparseMatrix) o).getMatrixHashMap().keySet()))
        return false;
      if (this.hashCode() != o.hashCode()) return false;
      for (Integer i: matrixHashMap.keySet())
      {
        if (!matrixHashMap.get(i).keySet().equals(((SparseMatrix) o).getMatrixHashMap().get(i).keySet()))
          return false;
        for (Integer j: matrixHashMap.get(i).keySet())
        {
          if (this.getElement(i, j) != ((SparseMatrix) o).getElement(i, j))
            return false;
//        Или так:
//        if (abs(this.getElement(i, j) - ((SparseMatrix) o).getElement(i, j)) > 0.001)
//          return false;
        }
      }
      return true;
    }
    else if (o instanceof DenseMatrix)
    {
      if (((DenseMatrix) o).getH() != this.getH() || ((DenseMatrix) o).getW() != this.getW())
        return false;
      for (int i = 0; i < this.getH(); i++)
      {
        for (int j = 0; j < this.getW(); j++)
        {
          if (this.getElement(i, j) != ((DenseMatrix) o).getElement(i, j))
            return false;
        }
      }
      return true;
    }
    return false;
  }

  public void hashCodeCalculate()
  {
    this.hashCode = this.matrixHashMap.hashCode();
  }

  @Override public int hashCode()
  {
    return this.hashCode;
  }

  @Override public String toString()
  {
    if (this.matrixHashMap == null) return "";
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < this.getH(); i++)
    {
      for (int j = 0; j < this.getW(); j++)
      {
        if (this.matrixHashMap.containsKey(i) && this.matrixHashMap.get(i).containsKey(j))
            result.append(this.matrixHashMap.get(i).get(j)).append(" ");
        else
          result.append("0 ");
      }
      result.append("\n");
    }
    return result.toString();
  }
}
