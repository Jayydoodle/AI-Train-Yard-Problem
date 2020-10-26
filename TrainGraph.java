import java.util.*;

public class TrainGraph<VertexType> {
    private Set<VertexType> vertices;
    private Map<Edge<VertexType>, Edge<VertexType>> edges;
    private Map<VertexType, ArrayList<String>> cars;
    private Map<VertexType, Set<VertexType>> neighbors;
    private ArrayList<Edge<VertexType>> edgeList;
    public List<VertexType> vertexArray;
    private int engineVertex;

    public TrainGraph(){
        vertices = new HashSet<>();
        edges = new HashMap<>();
        cars = new HashMap<>();
        neighbors = new HashMap<>();
        vertexArray = new ArrayList<>();
        edgeList = new ArrayList<>();
    }
    public void createFromArray(VertexType[][] array, String[][] states){

        createFromArray(array);
        addStates(states);
    }
    public void createFromArray(VertexType[][] array){

        for(int i = 0; i < array.length; i++){
            addUndirectedEdge(array[i][0], array[i][1]);
        }
    }
    private void addStates(String[][] array){

        for(int i = 0; i < array.length; i++){
            ArrayList<String> list = cars.get(i+1);
            for(int j = 0; j < array[i].length; j++){
                list.add(array[i][j]);
                if(array[i][j] == "*")
                    engineVertex = (i + 1);
            }
        }
    }
    public void addState(String[][] array){
        for(int i = 0; i < array.length; i++){
            ArrayList<String> list = cars.get(i+1);
            list.clear();
            for(int j = 0; j < array[i].length; j++){
                list.add(array[i][j]);
                if(array[i][j] == "*")
                    engineVertex = (i + 1);
            }
        }
    }
    public String[][] getState(){

        String[][] state = new String[cars.size()][];

        for(int i = 0; i < state.length; i++){
            String[] carArray = new String[cars.get(i + 1).size()];

            for(int j = 0; j < carArray.length; j++){
                carArray[j] = cars.get(i+1).get(j);
            }
            state[i] = carArray;
        }
        return state;
    }
    /**
     * Adds the vertex if it is new (otherwise does nothing). Note that this
     * vertex will not be connected to anything.
     * 
     * @param vertex The vertex to be added.
     */
    public void addVertex(VertexType vertex) {
        if(!neighbors.containsKey(vertex)) {
            neighbors.put(vertex, new HashSet<>());
            vertexArray.add(vertex);
        }
        vertices.add(vertex);
        ArrayList<String> carArray = new ArrayList<String>();
        cars.put(vertex, carArray);
    }
    /**
     * Adds a new directed edge to the graph
     * 
     * @param left The origin vertex.
     * @param right The destination vertex.
     */
    public void addUndirectedEdge(VertexType left, VertexType right) {
        // Keep track of both vertices, in case they're new
        addVertex(left);
        addVertex(right);
        // Actually build the neighbor connection
        neighbors.get(left).add(right);
        neighbors.get(right).add(left);
        // Keep track of edges
        Edge<VertexType> edge = new Edge<VertexType>(left, right);
        edges.put(edge, edge);
        edgeList.add(edge);
    }
    public int getEngineVertex(){
        return engineVertex;
    }
    public ArrayList<String> getList(VertexType vertex) {
        return cars.get(vertex);
    }
    /**
     * Provides the entire set of vertices for the graph.
     * 
     * @return All the vertices in the graph.
     */
    public Set<VertexType> getVertices() {
        return vertices;
    }
    /**
     * Get a vertex at the specified index
     * @param index the index of the vertex
     * @return vertex at index
     */
    public VertexType getVertex(int index) {
        return vertexArray.get(index);
    }
    /**
     * Provides all vertices that are connected to the given vertex.
     * 
     * @param origin The vertex to find the neighbors of.
     * @return Any vertices that are connected to the given vertex.
     */
    public Set<VertexType> getNeighbors(VertexType origin) {
        return neighbors.get(origin);
    }
    public boolean hasNeighbor(VertexType vertex, VertexType neighbor){

        if(neighbors.get(vertex).contains(neighbor))
            return true;
        else
            return false;
    }
    public ArrayList<Edge<VertexType>> getEdges(){
        return edgeList;
    }
    /**
     * Utility function for getEdge() below to get
     * the requested edge from it's position in the
     * map
     * 
     * @param the edge in key form
     * @return the requested edge
     */
    public Edge<VertexType> getEdge(Edge<VertexType> edge) {
        Edge<VertexType> newEdge = edges.get(edge);

        if(newEdge == null){ // if null, see if edge is stored the opposite way
            return getEdge(edge.getRight(), edge.getLeft());
        }
        return newEdge;
    }
    /**
     * Get the edge that connects two verticies.  This version of the
     * function is for the user.  The overloaded function above is
     * used by the graph to return the requested edge from the graph
     * 
     * @param left the origin vertex
     * @param right the neighbor vertex
     * @return recursively calls getEdge() above
     */
    public Edge<VertexType> getEdge(VertexType left, VertexType right){
        Edge<VertexType> e = new Edge<VertexType>(left, right);
        return getEdge(e);
    }
    /**
     * set the global engineVertex to keep track of the 
     * track the engine is on at all times
     * 
     * @param vertex integer representation of a vertex
     */
    public void setEngineVertex(int vertex){
        engineVertex = vertex;
    }
    /**
     * Create a simple representation of the graph as an adjacency list:
     *   [Vertex 1] -> (Neighbor 1, Neighbor 2, ...)
     *   [Vertex 2] -> (Neighbor 1, ...)
     *   ...
     * 
     * @return A string representation of the graph.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (VertexType v: vertices) {
            sb.append("["+v.toString()+"]");
            List<String> neighborStrings = new ArrayList<String>();
            for (VertexType n: neighbors.get(v)) {
                neighborStrings.add(n.toString());
            }
            sb.append(" -> (");
            for(int i = 0; i < neighborStrings.size(); i++) {
            	
            	sb.append(neighborStrings.get(i).toString());
            	if((i+1) != neighborStrings.size())
            	sb.append(", ");
            }
            sb.append(") ");
            sb.append(getList(v).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    /** Print all the verticies in the graph
     * 
     * @return a string vertices separated by newline
     */
    public String printVertices() {
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < vertexArray.size(); i++) {
    		
    		sb.append(vertexArray.get(i).toString());
    		sb.append("\n");
    	}
    	return sb.toString();
    }
}
