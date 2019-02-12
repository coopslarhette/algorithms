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
//        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(new Comparator<SearchTreeNode>() {
//            // may need to change Queue comparator
//            @Override
//            public int compare(SearchTreeNode o1, SearchTreeNode o2) {
//                o1.cost += o1.getCost();
//                o2.cost += o2.getCost();
//                return o1.cost - o2.cost;
//            }
//        });
//        ArrayList<String> result = new ArrayList<>();
//        Map<String, MazeState> transitions;
//        HashSet<MazeState> graveyard = new HashSet<>();
//        SearchTreeNode currentNode = null;
//        boolean hasKey = false;
//
//        /*
//         * need to clean code, maybe create generalized findValue() method?
//         */
//
//        //findKey
//        frontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null, 1));
//        while (!hasKey && !frontier.isEmpty()) {
//            currentNode = frontier.peek();
//            graveyard.add(currentNode.state);
//            if (problem.isKey(currentNode.state)) {
//                hasKey = true;
//                result = buildPath(currentNode);
//                System.out.println("key path: " + result.toString());
//            }
//            transitions = problem.getTransitions(currentNode.state);
//            for (Map.Entry<String, MazeState> action : transitions.entrySet()) {
//                if (!graveyard.contains(action.getValue()))
//                    frontier.add(new SearchTreeNode(action.getValue(), action.getKey(), currentNode));
//            }
//            frontier.remove(currentNode);
//        }
//
//        frontier.clear();
//        graveyard.clear();
//
//        //findGoal
//        frontier.add(new SearchTreeNode(currentNode.state, currentNode.action, null, 1));  //need to give null parent for buildPath()
//        while (!frontier.isEmpty()) {
//            currentNode = frontier.peek();
//            graveyard.add(currentNode.state);
//            if (problem.isGoal(currentNode.state)) {
//                result.addAll(buildPath(currentNode));
//                System.out.println("goal path: " + buildPath(currentNode));
//                return result;
//            }
//            transitions = problem.getTransitions(currentNode.state);
//            for (Map.Entry<String, MazeState> action : transitions.entrySet()) {
//                if (!graveyard.contains(action.getValue()))
//                    frontier.add(new SearchTreeNode(action.getValue(), action.getKey(), currentNode));
//            }
//            frontier.remove(currentNode);
//        }
        ArrayList<String> result = findPath(problem, problem.KEY_SET, problem.INITIAL_STATE);
        if (result != null) {
            ArrayList<String> result2 = findPath(problem, problem.GOAL_STATES, problem.KEY_STATE);
            result.addAll(result2);
            return result;
        }
        return null;
    }

    /**
     * Finds optimal path from an initial MazeState to goal MazeState.
     *
     * @param problem   A MazeProblem that specifies the maze, actions, transitions.
     * @param goal      Goal MazeState to find a path to.
     * @param initState Initial MazeState from which algorithm starts.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * * the goal state, of the format: ["R", "R", "L", ...]
     */
    public static ArrayList<String> findPath(MazeProblem problem, HashSet<MazeState> goal, MazeState initState) {
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(new Comparator<SearchTreeNode>() {
            // may need to change Queue comparator
            @Override
            public int compare(SearchTreeNode o1, SearchTreeNode o2) {

                return (o1.cost + manhatannDist(goal, o1.state)) - (o2.cost + manhatannDist(goal, o2.state));
            }
        });
        ArrayList<String> result = new ArrayList<>();
        Map<String, MazeState> transitions;
        HashSet<MazeState> graveyard = new HashSet<>();
        SearchTreeNode currentNode;

        frontier.add(new SearchTreeNode(initState, null, null, 1));
        while (!frontier.isEmpty()) {
            currentNode = frontier.peek();
            graveyard.add(currentNode.state);
            if (goal.contains(currentNode.state)) {
                return buildPath(currentNode);
            }
            transitions = problem.getTransitions(currentNode.state);
            for (Map.Entry<String, MazeState> action : transitions.entrySet()) {
                if (!graveyard.contains(action.getValue()))
                    frontier.add(new SearchTreeNode(action.getValue(), action.getKey(), currentNode));
            }
            frontier.remove(currentNode);
        }
        return null;
    }

    /**
     * Given a leaf node in the search tree (a goal), returns a solution by traversing
     * up the search tree, collecting actions along the way, until reaching the root
     *
     * @param root SearchTreeNode to start the upward traversal at (a goal node)
     * @return ArrayList sequence of actions; solution of format ["U", "R", "U", ...]
     */
    public static ArrayList<String> buildPath(SearchTreeNode root) {
        ArrayList<String> result = new ArrayList<>();
        for (SearchTreeNode current = root; current.parent != null; current = current.parent) {
            result.add(current.action);
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Calculates the Manhatann distance from the current MazeState to the MazeState of the nearest goal.
     *
     * @param goals   All possible goals in the Maze problem. Finds the closest one.
     * @param current Starting position to use in calculations
     * @return Distance to the goal it detmerines is closest.
     */
    public static int manhatannDist(HashSet<MazeState> goals, MazeState current) {
        int distance = Integer.MAX_VALUE;
        for (MazeState state : goals) {
            if (Math.abs(state.col - current.col) + Math.abs(state.row - current.row) < distance) {
                distance = Math.abs(state.col - current.col) + Math.abs(state.row - current.row);
            }
        }

        return distance;

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
    int cost;

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
        cost = parent.cost;
    }

    /**
     * Overloaded constructor that adds ability to set cost at initialization.
     *
     * @param state  The MazeState (col, row) that this node represents.
     * @param action The action that *led to* this state / node.
     * @param parent Reference to parent SearchTreeNode in the Search Tree.
     * @param cost Initial cost that this node should start at.
     */
    SearchTreeNode(MazeState state, String action, SearchTreeNode parent, int cost) {
        this.state = state;
        this.action = action;
        this.parent = parent;
        this.cost = cost;
    }

}