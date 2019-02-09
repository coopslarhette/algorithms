package pathfinder.informed;

import java.util.*;

/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first tree search.
 */
public class Pathfinder {

    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     *
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
     */
    public static ArrayList<String> solve(MazeProblem problem) {
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(new Comparator<SearchTreeNode>() {  // need to define getCost(),
            // may need to change Queue comparator
            @Override
            public int compare(SearchTreeNode o1, SearchTreeNode o2) {
                return problem.getCost(o1.state) - problem.getCost(o2.state);  //not sure this is right may need to switch
            }
        });
        ArrayList<String> result = new ArrayList<>();
        Map<String, MazeState> transitions;
        SearchTreeNode currentNode;
        boolean hasKey = false;

        frontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null));
        while (!frontier.isEmpty()) {
            currentNode = frontier.peek();
            if (problem.isKey(currentNode.state))
                hasKey = true;
            if (hasKey && problem.isGoal(currentNode.state)) {
                return buildPath(currentNode);
            }
            transitions = problem.getTransitions(currentNode.state);
            for (Map.Entry<String, MazeState> action : transitions.entrySet()) {
                frontier.add(new SearchTreeNode(action.getValue(), action.getKey(), currentNode));
            }

            frontier.remove();
        }

        return null;
    }

    public static ArrayList<String> buildPath(SearchTreeNode root) {
        Stack<String> temp = new Stack<>();
        ArrayList<String> result = new ArrayList<>();
        while (root.parent != null) {
            temp.add(root.action);
            root = root.parent;
        }
        while (!temp.isEmpty()) {
            result.add(temp.pop());
        }
        return result;
    }
}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {

    MazeState state;
    String action;
    SearchTreeNode parent;

    /**
     * Constructs a new SearchTreeNode to be used in the Search Tree.
     *
     * @param state  The MazeState (col, row) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     */
    SearchTreeNode(MazeState state, String action, SearchTreeNode parent) {
        this.state = state;
        this.action = action;
        this.parent = parent;
    }

}