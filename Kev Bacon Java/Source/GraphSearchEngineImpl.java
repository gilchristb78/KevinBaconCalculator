import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine{

    private class frontierNodes{
        public Node data;
        public frontierNodes parent; //'Parent' of a node I.E. the node who has a neighbor with .data

        /**
         * create a Node with data and Parents
         * @param n data in the form of a node
         * @param p a 'parent Node' with type of frontiernodes
         */
        public frontierNodes(Node n, frontierNodes p){
            data = n;
            parent = p;
        }
    }



    private HashSet<String> explored = new HashSet<>();
    private ArrayList<frontierNodes> queueFrontier = new ArrayList<>();

    /**
     * find the shortest path between two Nodes, using BFS
     * @param s the start node.
     * @param t the target node.
     * @return List of Nodes consisting of the path or null
     */
    public List<Node> findShortestPath (Node s, Node t){
        queueFrontier.add(new frontierNodes(s,null));
        while(queueFrontier.size() > 0) {
            frontierNodes node = queueFrontier.remove(0);
            explored.add(node.data.getName());
            if(node.data.equals(t)){ // found it yay!
                ArrayList<Node> temp = new ArrayList<>();
                while(node != null){  //add nodes in reverse order to a temp array
                    temp.add(node.data);
                    node = node.parent;
                }
                ArrayList<Node> answer = new ArrayList<>();
                for(int i = temp.size()-1; i >= 0; i--){ //reverse the order to match start and end
                    answer.add(temp.remove(i));
                }
                return answer;
            }
            for(Node n: node.data.getNeighbors()){ //for all the neighbors
                if(!explored.contains(n.getName())){ //if we haven't searched them yet, or are going to search them
                    frontierNodes temp = new frontierNodes(n,node);
                    explored.add(temp.data.getName());
                    queueFrontier.add(temp); //add them to stuff we will search
                }
            }
        }
        return null; //no path
    };
}
