package edu.spbu.matrix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatrixRandomTest {
    @Test
    public void mulDenseSparse() {
        MatrixGenerator generator = new MatrixGenerator();
        generator.generateDense("src/test/java/edu/spbu/matrix/files/randomDense1", 15, 10);
        generator.generateSparse("src/test/java/edu/spbu/matrix/files/randomSparse1", 10, 4);
        Matrix m1 = new DenseMatrix("src/test/java/edu/spbu/matrix/files/randomDense1");
        Matrix m2 = new SparseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse1");
        Matrix m2Dense = new DenseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse1");
        Matrix result = m1.mul(m2);
        Matrix expected = m1.mul(m2Dense);
        assertEquals(expected, result);
    }

    @Test
    public void mulSparseDense() {
        MatrixGenerator generator = new MatrixGenerator();
        generator.generateSparse("src/test/java/edu/spbu/matrix/files/randomSparse1", 15, 10);
        generator.generateDense("src/test/java/edu/spbu/matrix/files/randomDense2", 10, 4);
        Matrix m1 = new SparseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse1");
        Matrix m2 = new DenseMatrix("src/test/java/edu/spbu/matrix/files/randomDense2");
        Matrix m1Dense = new DenseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse1");
        Matrix result = m1.mul(m2);
        Matrix expected = m1Dense.mul(m2);
        assertEquals(expected, result);
    }

    @Test
    public void mulSparseSparse() {
        MatrixGenerator generator = new MatrixGenerator();
        generator.generateSparse("src/test/java/edu/spbu/matrix/files/randomSparse1", 15, 10);
        generator.generateSparse("src/test/java/edu/spbu/matrix/files/randomSparse2", 10, 4);
        Matrix m1 = new SparseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse1");
        Matrix m2 = new SparseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse2");
        Matrix m1Dense = new DenseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse1");
        Matrix m2Dense = new DenseMatrix("src/test/java/edu/spbu/matrix/files/randomSparse2");
        Matrix result = m1.mul(m2);
        Matrix expected = m1Dense.mul(m2Dense);
        assertEquals(expected, result);
    }
}
