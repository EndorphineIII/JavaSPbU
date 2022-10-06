package edu.spbu.matrix;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MatrixTest
{
  /**
   * ожидается 4 таких теста
   */
  @Test
  public void mulDD()
  {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDE()
  {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("empty.txt");
    Matrix expected = new DenseMatrix("m1.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulED()
  {
    Matrix m1 = new DenseMatrix("empty.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("m2.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public  void mulEE()
  {
    Matrix m1 = new DenseMatrix("empty.txt");
    Matrix m2 = new DenseMatrix("empty.txt");
    Matrix expected = new DenseMatrix("empty.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void readDenseMatrix()
  {
    Matrix m = new DenseMatrix("m.txt");

    int[][] arr = {{13, 15, 17, 0, 0}, {14, -4, -16, 0, 3}, {2, 2, 2, 5, 9}};
    List<List<Integer>> list = new ArrayList<>();
    for (int i = 0; i < arr.length; i++)
    {
      list.add(new ArrayList<>());
      for (int j = 0; j < arr[0].length; j++)
      {
        list.get(i).add(j, arr[i][j]);
      }
    }
    Matrix expected = new DenseMatrix(list);

    assertEquals(expected, m);
  }

  @Test
  public void readEmptyDenseMatrix()
  {
    Matrix m = new DenseMatrix("empty.txt");
    List<List<Integer>> list = new ArrayList<>();
    Matrix expected = new DenseMatrix(list);
    assertEquals(expected, m);
  }
}
