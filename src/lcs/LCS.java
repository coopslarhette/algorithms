package lcs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
 * Cooper LaRhette
 */

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

    /**
     * Helper method that traces back through memoization table to find any and all LCS subsequences.
     * @param rStr 1st string to compare
     * @param r Index of 1st string. Also represents row in the table.
     * @param cStr 2nd string to compare
     * @param c Index of 2nd string. Also represents column in the table.
     * @param memo Memoization table
     * @return Set of all LCS sequences.
     */
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
        memoCheck = fillBottomUp(rStr, cStr);
        return collectSolution(rStr, rStr.length(), cStr, cStr.length(), memoCheck);
    }

    /**
     * Helper method that fills the memoization table with LCS values
     * @param rStr 1st string to compare
     * @param cStr 2nd string to compare
     * @return Memoization table filled out according to bottom-up paradigm.
     */
    private static int[][] fillBottomUp(String rStr, String cStr) {
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
        return table;
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
        int[][] table = new int[rStr.length() + 1][cStr.length() + 1];
        table[rStr.length()][cStr.length()] = lcsRecursiveHelper(rStr, rStr.length(), cStr, cStr.length(), table);
        memoCheck = table;
        return collectSolution(rStr, rStr.length(), cStr, cStr.length(), table);
    }

    /**
     * Recursive helper which determines the length of the LCS and fills out the table according to top-down paradigm.
     * @param rStr 1st string to compare.
     * @param r Index of 1st string. Also represents row in the table.
     * @param cStr 2nd string to compare.
     * @param c Index of the 2nd string. Also represents column in the table.
     * @param memo Memoization table to store values of a LCS of certain substrings of cStr and rStr
     * @return The final value for the LCS.
     */
    private static int lcsRecursiveHelper(String rStr, int r, String cStr, int c, int[][] memo) {
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

}
