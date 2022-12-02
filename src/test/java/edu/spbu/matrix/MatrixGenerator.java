package edu.spbu.matrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class MatrixGenerator {
    public void generateDense(String file, int n, int m) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    writer.write(String.valueOf(ThreadLocalRandom.current().nextDouble(-10000, 10000)));
                    writer.write(" ");
                }
                writer.write("\n");
            }
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void generateSparse(String file, int n, int m) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (ThreadLocalRandom.current().nextInt(0, 100) > 85)
                        writer.write(String.valueOf(ThreadLocalRandom.current().nextDouble(-10000, 10000)));
                    else
                        writer.write("0");
                    writer.write(" ");
                }
                writer.write("\n");
            }
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
