package lcs;

import java.util.*;

public class LCS {

    /**
     * memoCheck is used to verify the state of your tabulation after
     * performing bottom-up and top-down DP. Make sure to set it after
     * calling either one of topDownLCS or bottomUpLCS to pass the tests!
     */
    public static int[][] memoCheck;

    // -----------------------------------------------
    // Shared Helper Methods
    // -----------------------------------------------

    // [!] TODO: Add your shared helper methods here!

    private static Set<String> collectSolution(String rStr, int r, String cStr, int c, int[][] memo) { //needs debug
        Set<String> tempSet;
        Set<String> result = new HashSet<>();
        if (r == 0 || c == 0) {
            return new HashSet<>(Collections.singletonList(""));
        } else if (rStr.charAt(r) == cStr.charAt(c)) {
            tempSet = collectSolution(rStr, r - 1, cStr, c - 1, memo);
            for (String str : tempSet) {
                result.add(str + rStr.charAt(r));
            }
            return result;
        } else {
            if (memo[r][c-1] >= memo[r-1][c]) {
                result.addAll(collectSolution(rStr, r, cStr, c-1, memo));
            }
            if (memo[r - 1][c] >= memo[r][c-1]) {
                result.addAll(collectSolution(rStr, r-1, cStr, c, memo));
            }
            return result;
        }

    }


    // -----------------------------------------------
    // Bottom-Up LCS
    // -----------------------------------------------

    /**
     * Bottom-up dynamic programming approach to the LCS problem, which
     * solves larger and larger subproblems iterative using a tabular
     * memoization structure.
     *
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     * [Side Effect] sets memoCheck to refer to table
     */
    public static Set<String> bottomUpLCS(String rStr, String cStr) {  //works
        int[][] table = new int[rStr.length() + 1][cStr.length() + 1];
        fillFirstZeros(table);
        for (int r = 1; r < rStr.length() + 1; r++) {
            for (int c = 1; c < cStr.length() + 1; c++) {
                if (rStr.charAt(r-1) == cStr.charAt(c-1)) {
                    table[r][c] = 1 + table[r - 1][c - 1];
                } else {
                    table[r][c] = Math.max(table[r - 1][c], table[r][c - 1]);
                }
            }
        }
        memoCheck = table;
        return collectSolution(rStr, rStr.length() - 1, cStr, cStr.length() - 1, table);  //should it be length - 1?
    }

    // [!] TODO: Add any bottom-up specific helpers here!

    /**
     * Takes a bottom-up table and fills first row and column with zeroes to make the programming a little
     * easier
     *
     * @param table Table to fill. Note: assumes a single row length for all rows and signle column length
     *              for all columns
     */
    private static void fillFirstZeros(int[][] table) {
        for (int r = 0; r < table.length; r++) {
            table[r][0] = 0;
        }
        for (int c = 0; c < table[0].length; c++) {
            table[0][c] = 0;
        }
    }


    // -----------------------------------------------
    // Top-Down LCS
    // -----------------------------------------------

    /**
     * Top-down dynamic programming approach to the LCS problem, which
     * solves smaller and smaller subproblems recursively using a tabular
     * memoization structure.
     *
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     * [Side Effect] sets memoCheck to refer to table
     */
    public static Set<String> topDownLCS(String rStr, String cStr) {

        throw new UnsupportedOperationException();
    }

    // [!] TODO: Add any top-down specific helpers here!


}
