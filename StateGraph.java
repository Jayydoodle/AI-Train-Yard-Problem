import java.util.*;

class StateGraph<VertexType>{

    private Map<Integer, Set<State>> neighbors;
    private Map<Integer, Action<Integer>> actions;
    private Map<Integer, State> vertices;

    public StateGraph(){

        neighbors = new HashMap<>();
        actions = new HashMap<>();
        vertices = new HashMap<>();
    }
    public void addVertex(State vertex) {

        if(!neighbors.containsKey(vertex.getHashCode())) {
            neighbors.put(vertex.getHashCode(), new HashSet<>());
            //System.out.println("added new vertex to hashcode " + vertex.getHashCode() + Arrays.deepToString(vertex.asArray()));
            
            vertices.put(vertex.getHashCode(), vertex);
        }
    }
    public boolean containsVertex(int vertex){
        return vertices.containsKey(vertex);
    }
    public void addUndirectedEdge(State leftState, State rightState) {
        addVertex(leftState);
        addVertex(rightState);
        
        if(!(neighbors.get(leftState.getHashCode()).contains(rightState))){
            neighbors.get(leftState.getHashCode()).add(rightState);
        }
        if(!(neighbors.get(rightState.getHashCode()).contains(leftState))){
            neighbors.get(rightState.getHashCode()).add(leftState);
        }
    }
    public void processList(State currentState, ArrayList<Action<Integer>> list){
        
        for(int i = 0; i < list.size(); i++){
            
            if(!Arrays.deepEquals(currentState.asArray(), list.get(i).getState())){
                //System.out.println(list.get(i).toString() + " " + Arrays.deepToString(list.get(i).getState()));
                State childState = new State(list.get(i).getState(), currentState.asArray(), list.get(i), currentState.getPathCost() + 1);
                addUndirectedEdge(currentState, childState);
                //actions.put(list.get(i).getHashCode(), list.get(i));
            }
        }
    }
    public Set<State> getNeighbors(int origin) {
        return neighbors.get(origin);
    }
    public Action<Integer> getAction(int key){
        return actions.get(key);
    }
    public State getState(int key){
        return vertices.get(key);
    }
}