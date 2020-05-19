/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int vTop, vBottom;
    private int[][] grid;
    private int n;
    private int numOpenSites = 0;
    private WeightedQuickUnionUF quickUnion;
    // QuickUnionTop is used to prevent backwash in isFull call
    private WeightedQuickUnionUF quickUnionTop;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }
        // 0 means blocked. 1 means free. By default all values are 0
        grid = new int[n][n];
        vTop = 0;
        vBottom = n*n + 1;

        // Allow first and last to be virtual top and bottom
        quickUnion = new WeightedQuickUnionUF(n*n + 2);
        quickUnionTop = new WeightedQuickUnionUF(n*n + 1);
        this.n = n;

        for (int i = 1; i <= n; i++) {
            quickUnion.union(getId(n, i), vBottom);
            quickUnion.union(getId(1, i), vTop);
            quickUnionTop.union(getId(1, i), vTop);
        }
    }

    private void visualizeGrid() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%d", grid[i][j]);
            }
            System.out.println();
        }
    }

    // opens the site (row, col) if it is not open already
    // row, col are 1-indexed
    public void open(int row, int col) {
        validRowCol(row, col);
        int prev = grid[row - 1][col - 1];

        // already open
        if (prev == 1) {
            return;
        }

        if (prev == 0) {
            numOpenSites++;
            grid[row - 1][col - 1] = 1;
        }

        // Add a union with each adjacent site
        int[] adjacentIds = getAdjacentIds(row, col);
        int currentId = getId(row, col);

        for (int i = 0; i < 4; i++) {
            int adjId = adjacentIds[i];
            if (adjId != -1) {
                int[] coords = coordsFromId(adjId);
                // Grid is zero indexed
                if (grid[coords[0] - 1][coords[1] - 1] == 1) {
                    quickUnion.union(currentId, adjId);
                    quickUnionTop.union(currentId, adjId);
                }
            }
        }
    }

    private void validRowCol(int row, int col) {
        if (row < 1 || row > n) {
            throw new IllegalArgumentException("row out of bounds");
        }

        if (col < 1 || col > n) {
            throw new IllegalArgumentException("col out of bounds");
        }
    }

    // Row/col are 1 indexed
    private int[] getAdjacentIds(int row, int col) {
        int adjRows[] = { row - 1, row, row, row + 1};
        int adjCols[] = { col, col - 1, col + 1, col};

        int adj[] = new int[4];

        for (int i = 0; i < 4; i++) {
            int adjRow = adjRows[i];
            int adjCol = adjCols[i];

            try {
                validRowCol(adjRow, adjCol);
                adj[i] = getId(adjRow, adjCol);
            } catch (IllegalArgumentException e) {
                adj[i] = -1;
            }
        }

        return adj;
    }

    private int getId(int row, int col) {
        return (row - 1)*n + (col - 1) + 1;
    }

    // returns coords 1 indexed
    private int[] coordsFromId(int id) {
        id = id - 1;

        int col = id % n;
        int row = id / n;

        return new int[] { row + 1, col + 1};
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validRowCol(row, col);
        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validRowCol(row, col);
        if (!isOpen(row, col)) {
            return false;
        }

        int currentId = getId(row, col);
        return connected(currentId, vTop, quickUnionTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (n == 1 && isOpen(1, 1)) {
            return true;
        }
        if (n == 1 && !isOpen(1, 1)) {
            return false;
        }

        return connected(vBottom, vTop, quickUnion);
    }

    // Rolling my own because for some reason the connected method in WeightedQuickUnionUF
    // is marked as deprecated
    private boolean connected(int id1, int id2, WeightedQuickUnionUF qu) {
        return qu.find(id1) == qu.find(id2);
    }

    public static void main(String[] args) {
        int n = 2;
        Percolation p = new Percolation(n);
        p.open(1, 1);
        p.open(2, 2);
        p.visualizeGrid();
        System.out.println(p.percolates());
    }
}
