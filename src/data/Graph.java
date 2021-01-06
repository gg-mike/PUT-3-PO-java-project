package data;

import object.network.Junction;
import object.network.NetworkObject;
import object.network.Track;

import java.util.HashMap;
import java.util.HashSet;

class Graph {
    public static class Node {
        public String id;
        public double currWeight = Double.MAX_VALUE;
        public HashSet<String> connections = new HashSet<>();

        public Node(String id) {
            this.id = id;
        }
    }

    private final HashMap<String, Node> nodes = new HashMap<>();

    public Graph(HashSet<String> nodesId_a, HashSet<String> nodesId_j, char assignment) {
        if (assignment == 'A') {
            for (String nodeId : nodesId_a)
                nodes.put(nodeId, new Node(nodeId));
            for (String nodeId : nodesId_j)
                if (((NetworkObject) Database.getAppObjects().get(nodeId)).getAssignment() == 'A')
                    nodes.put(nodeId, new Node(nodeId));
        }
        else {
            for (String nodeId : nodesId_j)
                if (((NetworkObject) Database.getAppObjects().get(nodeId)).getAssignment() == 'W')
                    nodes.put(nodeId, new Node(nodeId));
        }
       createConnections();
    }

    private void createConnections() {
        for (String nodeId : nodes.keySet()) {
            for (String trackId : ((Junction) Database.getAppObjects().get(nodeId)).getTracks().values()) {
                String[] points = ((Track) Database.getAppObjects().get(trackId)).getPoints();
                if (points[0].equals(nodeId))
                    nodes.get(nodeId).connections.add(points[1]);
                else
                    nodes.get(nodeId).connections.add(points[0]);
            }
        }
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public void resetNodes() {
        for (String nodeId : nodes.keySet())
            nodes.get(nodeId).currWeight = Double.MAX_VALUE;
    }
}