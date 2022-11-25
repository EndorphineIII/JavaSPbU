package edu.spbu.matrix;

import org.junit.Test;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    Matrix expected = new SparseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulIncorrectDense()
  {
    Matrix m1 = new DenseMatrix("m.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("empty.txt");
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
    Matrix m2 = new SparseMatrix("empty.txt");
    Matrix expected = new DenseMatrix("empty.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void readDenseMatrix()
  {
    Matrix m = new DenseMatrix("m.txt");

    double[][] arr = {{13.7, 15.2, 17.1, 0, 0.2}, {14, -4.1, -16, 0, 3}, {2, 0, 2.3, 5, 9}, {0, 0, 0, 0, 0}};
    List<List<Double>> list = new ArrayList<>();
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
  public void readSparseMatrix()
  {
    Matrix m = new SparseMatrix("m.txt");
    double[][] arr = {{13.7, 15.2, 17.1, 0, 0.2}, {14, -4.1, -16, 0, 3}, {2, 0, 2.3, 5, 9}, {0, 0, 0, 0, 0}};
    SparseMatrix expected = new SparseMatrix(arr);
    assertEquals(expected, m);
  }

  @Test
  public void readEmptyDenseMatrix()
  {
    Matrix m = new DenseMatrix("empty.txt");
    List<List<Double>> list = new ArrayList<>();
    Matrix expected = new DenseMatrix(list);
    assertEquals(expected, m);
  }

  @Test
  public void mulIncorrectSparse()
  {
    Matrix m1 = new SparseMatrix("m.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    Matrix expected = new SparseMatrix("empty.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulSS()
  {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulSD()
  {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new DenseMatrix("m2.txt");
    Matrix expected = new DenseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulDS()
  {
    Matrix m1 = new DenseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    Matrix expected = new SparseMatrix("result.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulSE()
  {
    Matrix m1 = new SparseMatrix("m1.txt");
    Matrix m2 = new SparseMatrix("empty.txt");
    Matrix expected = new SparseMatrix("m1.txt");
    assertEquals(expected, m1.mul(m2));
  }

  @Test
  public void mulES()
  {
    Matrix m1 = new SparseMatrix("empty.txt");
    Matrix m2 = new SparseMatrix("m2.txt");
    Matrix expected = new SparseMatrix("m2.txt");
    assertEquals(expected, m1.mul(m2));
  }

}
