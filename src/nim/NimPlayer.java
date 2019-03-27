package nim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Artificial Intelligence responsible for playing the game of Nim!
 * Implements the alpha-beta-pruning mini-max search algorithm
 */
public class NimPlayer {

    private final int MAX_REMOVAL;

    NimPlayer(int MAX_REMOVAL) {
        this.MAX_REMOVAL = MAX_REMOVAL;
    }

    /**
     * @param remaining Integer representing the amount of stones left in the pile
     * @return An int action representing the number of stones to remove in the range
     * of [1, MAX_REMOVAL]
     */
    public int choose(int remaining) {
        int resultantAction = 0;
        int tempScore = 0;
        GameTreeNode root = new GameTreeNode(remaining, 0, true);  //unsure if action of 0 is correct
        Map<GameTreeNode, Integer> visited = new HashMap<>();

        alphaBetaMinimax(root, Integer.MIN_VALUE, Integer.MAX_VALUE, true, visited);
        for (GameTreeNode node : root.children) {
            System.out.println("node score: " + node.score + ", node action: " + node.action);
            if (node.score > tempScore) {
                tempScore = node.score;
                resultantAction = node.action;
                System.out.println("resultant score updated");
            }
        }
        return resultantAction;
    }

    /**
     * Constructs the minimax game tree by the tenets of alpha-beta pruning with
     * memoization for repeated states.
     *
     * @param node    The root of the current game sub-tree
     * @param alpha   Smallest minimax score possible
     * @param beta    Largest minimax score possible
     * @param isMax   Boolean representing whether the given node is a max (true) or min (false) node
     * @param visited Map of GameTreeNodes to their minimax scores to avoid repeating large subtrees
     * @return Minimax score of the given node + [Side effect] constructs the game tree originating
     * from the given node
     */
    private int alphaBetaMinimax(GameTreeNode node, int alpha, int beta, boolean isMax, Map<GameTreeNode, Integer> visited) {
        int tempAlpha = Integer.MIN_VALUE;

        if (node.remaining == 0) {
            tempAlpha = (node.isMax) ? 0 : 1;
            node.score = tempAlpha;
            visited.put(node, tempAlpha);
            return tempAlpha;
        }
        if (visited.containsKey(node)) {
            return visited.get(node);
        }
        genChildren(node);

        //new computation
        if (isMax) {
            for (GameTreeNode child : node.children) {
                tempAlpha = Math.max(tempAlpha, alphaBetaMinimax(child, alpha, beta, false, visited));
                alpha = Math.max(alpha, tempAlpha);
                if (beta <= alpha) {
                    break;
                }
            }
            node.score = tempAlpha;
            visited.put(node, tempAlpha);
            return tempAlpha;
        } else {
            tempAlpha = Integer.MAX_VALUE;
            for (GameTreeNode child : node.children) {
                tempAlpha = Math.min(tempAlpha, alphaBetaMinimax(child, alpha, beta, true, visited));
                beta = Math.min(tempAlpha, beta);
                if (beta <= alpha) {
                    break;
                }
            }
            node.score = tempAlpha;
            visited.put(node, tempAlpha);
            return tempAlpha;
        }
    }

    /**
     * Generates children from a parent node based off of how many different possible number of stones a
     * player can remove.
     *
     * @param parent Node children are generated from.
     */
    private void genChildren(GameTreeNode parent) {
        int action = Math.min(MAX_REMOVAL, parent.remaining);
        for (int i = action; i > 0; i--) {
            parent.children.add(new GameTreeNode(parent.remaining - i, i, !parent.isMax));
        }
    }

}

/**
 * GameTreeNode to manage the Nim game tree.
 */
class GameTreeNode {

    int remaining, action, score;
    boolean isMax;
    ArrayList<GameTreeNode> children;

    /**
     * Constructs a new GameTreeNode with the given number of stones
     * remaining in the pile, and the action that led to it. We also
     * initialize an empty ArrayList of children that can be added-to
     * during search, and a placeholder score of -1 to be updated during
     * search.
     *
     * @param remaining The Nim game state represented by this node: the #
     *                  of stones remaining in the pile
     * @param action    The action (# of stones removed) that led to this node
     * @param isMax     Boolean as to whether or not this is a maxnode
     */
    GameTreeNode(int remaining, int action, boolean isMax) {
        this.remaining = remaining;
        this.action = action;
        this.isMax = isMax;
        children = new ArrayList<>();
        score = -1;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GameTreeNode
                ? remaining == ((GameTreeNode) other).remaining &&
                isMax == ((GameTreeNode) other).isMax &&
                action == ((GameTreeNode) other).action
                : false;
    }

    @Override
    public int hashCode() {
        return remaining + ((isMax) ? 1 : 0);
    }

}
