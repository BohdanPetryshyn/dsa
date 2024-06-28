import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be 1 or more.");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("Trials must be 1 or more.");
        }

        this.trials = trials;
        this.results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            int[] randomIndices = StdRandom.permutation(n * n);
            int percolationIndex = 0;

            for (int j = 0; j < n * n; j++) {
                int randomIndex = randomIndices[j];
                int[] expandedRandomIndex = this.expand(randomIndex, n);
                percolation.open(expandedRandomIndex[0] + 1, expandedRandomIndex[1] + 1);

                if (percolation.percolates()) {
                    percolationIndex = j;
                    break;
                }
            }

            this.results[i] = (double) (percolationIndex + 1) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + CONFIDENCE_95 * this.stddev() / Math.sqrt(this.trials);
    }

    private int[] expand(int flatIndex, int n) {
        int row = flatIndex / n;
        int col = flatIndex % n;

        return new int[] { row, col };
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");

    }
}