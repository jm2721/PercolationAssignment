/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] results;
    private int n;
    private int numTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be > 0");
        }

        results = new double[trials];
        this.n = n;
        numTrials = trials;

        for (int i = 0; i < trials; i++) {
            runATrial(n, i);
        }
    }

    private void runATrial(int n, int trialNumber) {
        Percolation p = new Percolation(n);

        double threshold = 0.0;
        while (!p.percolates()) {
            int[] nextRandomSite = randomSite(p);
            p.open(nextRandomSite[0], nextRandomSite[1]);
            threshold++;
        }
        results[trialNumber] = threshold / (n*n);
    }

    private int[] randomSite(Percolation p) {
        int row;
        int col;

        // Algorithm is fine for now.
        do {
            row = StdRandom.uniform(n);
            col = StdRandom.uniform(n);
        } while (p.isOpen(row + 1, col + 1));

        return new int[] {row + 1, col + 1};
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev())/Math.sqrt(numTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev())/Math.sqrt(numTrials);
    }

    public static void main(String[] args) {
        // Include a main() method that takes two command-line
        // arguments n and T, performs T independent computational experiments (discussed above)
        // on an n-by-n grid, and prints the sample mean, sample standard deviation, and the
        // 95% confidence interval for the percolation threshold. Use StdRandom to
        // generate random numbers; use StdStats to compute the sample mean and sample standard
        // deviation.

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, t);

        StdOut.println("mean " + stats.mean());
        StdOut.println("stdev " + stats.stddev());
        StdOut.println("95% confident interval [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
