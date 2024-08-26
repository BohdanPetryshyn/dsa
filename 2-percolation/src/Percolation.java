import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF percolationUF;
    private final WeightedQuickUnionUF fullnessUF;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N can't be less than 1.");
        }

        this.n = n;
        this.grid = new boolean[n][n];

        this.percolationUF = new WeightedQuickUnionUF(n * n + 2);
        this.fullnessUF = new WeightedQuickUnionUF(n * n + 1);

        this.numberOfOpenSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        row = this.assertCorrectIndex(row);
        col = this.assertCorrectIndex(col);

        // boolean oldValue = this.grid.get(row).set(col, true);
        boolean oldValue = this.grid[row][col];
        if (oldValue) {
            return;
        }

        this.grid[row][col] = true;
        this.numberOfOpenSites++;

        int flatCoordinate = this.flatten(row, col);
        if (row > 0 && this.grid[row - 1][col]) {
            this.percolationUF.union(flatCoordinate, this.flatten(row - 1, col));
            this.fullnessUF.union(flatCoordinate, this.flatten(row - 1, col));
        }
        if (row < n - 1 && this.grid[row + 1][col]) {
            this.percolationUF.union(flatCoordinate, this.flatten(row + 1, col));
            this.fullnessUF.union(flatCoordinate, this.flatten(row + 1, col));
        }
        if (col > 0 && this.grid[row][col - 1]) {
            this.percolationUF.union(flatCoordinate, this.flatten(row, col - 1));
            this.fullnessUF.union(flatCoordinate, this.flatten(row, col - 1));
        }
        if (col < n - 1 && this.grid[row][col + 1]) {
            this.percolationUF.union(flatCoordinate, this.flatten(row, col + 1));
            this.fullnessUF.union(flatCoordinate, this.flatten(row, col + 1));
        }

        if (row == 0) {
            this.percolationUF.union(flatCoordinate, this.n * this.n);
            this.fullnessUF.union(flatCoordinate, this.n * this.n);
        }
        if (row == this.n - 1) {
            this.percolationUF.union(flatCoordinate, this.n * this.n + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        row = this.assertCorrectIndex(row);
        col = this.assertCorrectIndex(col);

        return this.grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        row = this.assertCorrectIndex(row);
        col = this.assertCorrectIndex(col);

        int flatCoordinate = this.flatten(row, col);

        return this.fullnessUF.find(flatCoordinate) == this.fullnessUF.find(this.n * this.n);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.percolationUF.find(this.n * this.n) == this.percolationUF.find(this.n * this.n + 1);
    }

    private int flatten(int row, int col) {
        return this.n * row + col;
    }

    private int assertCorrectIndex(int index) {
        if (index < 1 || index > this.n) {
            throw new IllegalArgumentException("Index out of bounds.");
        }

        return index - 1;
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}