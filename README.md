My solution to https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php

Score: 94/100 from Autograder

Improvements:
- My Percolation implementation uses too much space. Haven't optimized for that.
- PercolationStats calls mean() and stdev() too many times (3 instead of 1). That's just becuase I don't store the value. Haven't bothered to fix it.

