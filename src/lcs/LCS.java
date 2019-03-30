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

    private static Set<String> collectSolution(String rStr, int r, String cStr, int c, int[][] memo) {
        Set<String> tempSet;
        Set<String> result = new HashSet<>();
        if (r == 0 || c == 0) {
            return new HashSet<>(Collections.singletonList(""));
        } else if (rStr.charAt(r - 1) == cStr.charAt(c - 1)) {
            tempSet = collectSolution(rStr, r - 1, cStr, c - 1, memo);
            for (String str : tempSet) {
                result.add(str + rStr.charAt(r - 1));
            }
            return result;
        } else {
            if (memo[r][c - 1] >= memo[r - 1][c]) {
                result.addAll(collectSolution(rStr, r, cStr, c - 1, memo));
            }
            if (memo[r - 1][c] >= memo[r][c - 1]) {
                result.addAll(collectSolution(rStr, r - 1, cStr, c, memo));
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
    public static Set<String> bottomUpLCS(String rStr, String cStr) {
        int[][] table = new int[rStr.length() + 1][cStr.length() + 1];
        for (int r = 1; r < rStr.length() + 1; r++) {
            for (int c = 1; c < cStr.length() + 1; c++) {
                if (rStr.charAt(r - 1) == cStr.charAt(c - 1)) {
                    table[r][c] = 1 + table[r - 1][c - 1];
                } else {
                    table[r][c] = Math.max(table[r - 1][c], table[r][c - 1]);
                }
            }
        }
        memoCheck = table;
        return collectSolution(rStr, rStr.length(), cStr, cStr.length(), table);
    }

    // [!] TODO: Add any bottom-up specific helpers here!

//    /**
//     * Takes a bottom-up table and fills first row and column with zeroes to make the programming a little
//     * easier
//     *
//     * @param table Table to fill. Note: assumes a single row length for all rows and signle column length
//     *              for all columns
//     */
//    private static void fillZeroes(int[][] table) {
//        for (int[] arr : table) {
//            for (int i : arr) {
//                i = 0;
//            }
//        }
//    }


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
        int[][] table = new int[rStr.length() + 1][cStr.length() + 1];
        table[rStr.length()][cStr.length()] = lcsRecursiveHelper(rStr, rStr.length(), cStr, cStr.length(), table);
        memoCheck = table;
        System.out.println(Arrays.deepToString(table));
        return collectSolution(rStr, rStr.length(), cStr, cStr.length(), table);
    }

    private static int lcsRecursiveHelper(String rStr, int r, String cStr, int c, int[][] memo) {
        Map<Integer, Map<Integer, Integer>> cache = new HashMap<>();
        int temp;
        if (r == 0 || c == 0) {
            return 0;
        } else if (rStr.charAt(r - 1) == cStr.charAt(c - 1)) {
            memo[r][c] = 1 + lcsRecursiveHelper(rStr, r - 1, cStr, c - 1, memo);
            return memo[r][c];
        } else {
            memo[r][c - 1] = lcsRecursiveHelper(rStr, r, cStr, c - 1, memo);
            memo[r - 1][c] = lcsRecursiveHelper(rStr, r - 1, cStr, c, memo);
            memo[r][c] = Math.max(memo[r - 1][c], memo[r][c - 1]);
            return memo[r][c];
        }
    }

    // [!] TODO: Add any top-down specific helpers here!

}
