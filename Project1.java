
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.Year;
import java.util.*;

public class Project1 {
	
	public static void main(String[] args) {
		
		Integer[][] yard1_edges = {{1, 2}, {1, 3}, {3, 5}, {4, 5}, {2, 6}, {5, 6}};
		String[][] yard1_state = {{"*"},{"e"},{},{"b","c", "a"},{}, {"d"}};
		String[][] yard1_finalState = {{"*","a","b","c","d","e"},{},{},{},{},{}};

		Integer[][] yard2_edges = {{1, 2}, {1, 5}, {2, 3}, {2, 4}};
		String[][] yard2_state = {{"*"},{"d"},{"b"},{"a","e"},{"c"}};
		String[][] yard2_finalState = {{"*","a","b","c","d","e"},{},{},{},{}};

		Integer[][] yard3_edges = {{1, 2}, {1, 3}};
		String[][] yard3_state = {{"*"},{"a"},{"b"}};
		String[][] yard3_finalState = {{"*","a","b"},{},{}};

		Integer[][] yard4_edges = {{1, 2}, {1, 3}, {1, 4}};
		String[][] yard4_state = {{"*"},{"a"},{"b", "c"}, {"d"}};
		String[][] yard4_finalState = {{"*","a","b","c","d"},{},{},{}};

		Integer[][] yard5_edges = {{1, 2}, {1, 3}, {1, 4}};
		String[][] yard5_state = {{"*"},{"a"},{"c", "b"}, {"d"}};
		String[][] yard5_finalState = {{"*","a","b","c","d"},{},{},{}};

		runBFS(3, yard3_edges, yard3_state, yard3_finalState);
		runAStar(3, yard3_edges, yard3_state, yard3_finalState);
		runBFS(4, yard4_edges, yard4_state, yard4_finalState);
		runAStar(4, yard4_edges, yard4_state, yard4_finalState);
		runBFS(5, yard5_edges, yard5_state, yard5_finalState);
		runAStar(5, yard5_edges, yard5_state, yard5_finalState);
		runBFS(2, yard2_edges, yard2_state, yard2_finalState);
		runAStar(2, yard2_edges, yard2_state, yard2_finalState);
		//BFS on yard1 takes 10-20 minutes
		//runBFS(1, yard1_edges, yard1_state, yard1_finalState);
		runAStar(1, yard1_edges, yard1_state, yard1_finalState);
	}
	public static void runAStar(int yardNumber, Integer[][] yard_edges, String[][] initialState, String[][] finalState){
		
		System.out.println("yard " + yardNumber + " AStar");

		TrainGraph<Integer> yard = new TrainGraph<Integer>();;
		yard.createFromArray(yard_edges);

		Instant startTime = Instant.now();
		int yardSteps = AStar(yardNumber, yard, initialState, finalState);
		Instant stopTime = Instant.now();
		Duration interval = Duration.between(startTime, stopTime);

		if(yardNumber == 1)
			System.out.println("steps: " + yardSteps + " seconds: "+(interval.getSeconds())+"\n");
		else
			System.out.println("steps: " + yardSteps + " seconds: "+(interval.getNano()/1000000000.0)+"\n");
	}
	public static void runBFS(int yardNumber, Integer[][] yard_edges, String[][] initialState, String[][] finalState){
		
		System.out.println("yard " + yardNumber + " BFS");

		TrainGraph<Integer> yard = new TrainGraph<Integer>();;
		yard.createFromArray(yard_edges);

		Instant startTime = Instant.now();
		int yardSteps = BFS(yardNumber, yard, initialState, finalState);
		Instant stopTime = Instant.now();
		Duration interval = Duration.between(startTime, stopTime);
		
		if(yardNumber == 1)
			System.out.println("steps: " + yardSteps + " seconds: "+(interval.getSeconds())+"\n");
		else
			System.out.println("steps: " + yardSteps + " seconds: "+(interval.getNano()/1000000000.0)+"\n");
	}
	public static int AStar(int fileNumber, TrainGraph<Integer> yard, String[][] initialState, String[][] goalState){

		StateGraph<Action> stateGraph = new StateGraph<>();

		Map<Integer, MutableBoolean> visited = new HashMap<>();
		PriorityQueue<State> vertexPQueue = new PriorityQueue<State>();

		State firstState = new State(initialState);
		State finalState = new State(goalState);
		vertexPQueue.add(firstState);
		visited.put(firstState.getHashCode(), new MutableBoolean(false));

		while(!vertexPQueue.isEmpty()){
		
			State currentState = vertexPQueue.remove();

			vertexPQueue.removeIf(n -> (visited.get(n.getHashCode()).getValue() == true));
			if(visited.get(currentState.getHashCode()).getValue() == true){
				continue;
			}
			//System.out.println(Arrays.deepToString(currentState.asArray()) + " was visited " + "cost: " + currentState.getPathCost());
			if(Arrays.deepEquals(currentState.asArray(), finalState.asArray())){
				break;
			}
			ArrayList<Action<Integer>> adjacentNodes = Expand(yard, currentState.asArray());
			stateGraph.processList(currentState, adjacentNodes);
			
			visited.get(currentState.getHashCode()).setTrue();
			
			Set<State> neighbors = stateGraph.getNeighbors(currentState.getHashCode());
			
			//System.out.println();
			for(State vertex: neighbors){
				int neighborKey = vertex.getHashCode();
				if(visited.get(neighborKey) == null){
					//System.out.println("setting " + vertex.getHashCode() + " to false");
					if(!visited.containsKey(neighborKey)){
					visited.put(neighborKey, new MutableBoolean(false));
					}
				}
				if(visited.get(neighborKey).getValue() == false){
					
					int hValue = vertex.countWrong(goalState);
					vertex.setPathCost(vertex.getPathCost() + hValue);
					vertexPQueue.add(vertex);
					//System.out.println("adding " + neighborKey + " " + vertex.asString() + " to queue");
				}
			}
			//System.out.println();
		}
		int total = printToFile(fileNumber, "AStar", stateGraph, finalState);

		return total;
	}
	public static int BFS(int fileNumber, TrainGraph<Integer> yard, String[][] initialState, String[][] goalState){

		StateGraph<Action> stateGraph = new StateGraph<>();

		Map<Integer, MutableBoolean> visited = new HashMap<>();
		Queue<State> vertexQueue = new LinkedList<State>();

		State firstState = new State(initialState);
		State finalState = new State(goalState);
		vertexQueue.add(firstState);
		visited.put(firstState.getHashCode(), new MutableBoolean(false));
	
		while(!vertexQueue.isEmpty()){
			
			State currentState = vertexQueue.remove();

			vertexQueue.removeIf(n -> (visited.get(n.getHashCode()).getValue() == true));
			if(visited.get(currentState.getHashCode()).getValue() == true){
				continue;
			}
			//System.out.println(currentState.getHashCode()+Arrays.deepToString(currentState.asArray()) + " was visited\n" + "parent: " + Arrays.deepToString(currentState.getParentState()));
			if(Arrays.deepEquals(currentState.asArray(), finalState.asArray())){
				break;
			}
			ArrayList<Action<Integer>> adjacentNodes = Expand(yard, currentState.asArray());
			stateGraph.processList(currentState, adjacentNodes);
			
			visited.get(currentState.getHashCode()).setTrue();
			
			Set<State> neighbors = stateGraph.getNeighbors(currentState.getHashCode());
			
			//System.out.println();
			for(State vertex: neighbors){
				int neighborKey = vertex.getHashCode();
				if(visited.get(neighborKey) == null){
					//System.out.println("setting " + vertex.getHashCode() + " to false");
					if(!visited.containsKey(neighborKey)){
					visited.put(neighborKey, new MutableBoolean(false));
					}
				}
				if(visited.get(neighborKey).getValue() == false){
					vertexQueue.add(vertex);
					//System.out.println("adding " + neighborKey + " " + vertex.asString() + " to queue");
				}
			}
			//System.out.println();
		}
		int total = printToFile(fileNumber, "BFS", stateGraph, finalState);
		
		return total;
	}
	public static ArrayList<Action<Integer>> PossibleActions(TrainGraph yard, String[][] state){
		
		yard.addState(state);
		int engineVertex = yard.getEngineVertex();
		ArrayList<Action<Integer>> actions = new ArrayList<Action<Integer>>();
		Set<Integer> neighbors = yard.getNeighbors(engineVertex);
		//System.out.println("vertex of engine: " + engineVertex);
		for(Integer vertex: neighbors){
			
			Edge<Integer> edge = yard.getEdge(engineVertex, vertex);
			
			if(edge.getLeft() == engineVertex){
				actions.add(new Action<Integer>("left", vertex, engineVertex));
				actions.add(new Action<Integer>("right", engineVertex, vertex));
			}
			if(edge.getRight() == engineVertex){
				actions.add(new Action<Integer>("left", engineVertex, vertex));
				actions.add(new Action<Integer>("right", vertex, engineVertex));
			}
		}
		//System.out.println("Possible moves: " + actions.toString()+"\n"+"Vertex of engine: "+engineVertex);
		//System.out.println(yard.toString());
		return actions;
	}
	public static String[][] Result(TrainGraph yard, Action<Integer> action, String[][] state){

		yard.addState(state);
		if(action.getName() == "left"){
			Left(yard, action.getOrigin(), action.getDestination());
		}
		else if(action.getName() == "right"){
			Right(yard, action.getOrigin(), action.getDestination());
		}
		return yard.getState();
	}
	public static ArrayList<Action<Integer>> Expand(TrainGraph yard, String[][] state){

		ArrayList<Action<Integer>> actions = PossibleActions(yard, state);
		
		for(int i = 0; i < actions.size(); i++){

			String[][] newState = Result(yard, actions.get(i), state);
			actions.get(i).setState(newState);
		}
		//System.out.println(actions.toString());
		return actions;
	}
	public static void Right(TrainGraph yard, int x, int y){

		if(!yard.hasNeighbor(x, y))
			return;

		ArrayList<String> trackX = yard.getList(x);
		ArrayList<String> trackY = yard.getList(y);

		if(trackX.contains("*") || trackY.contains("*")){

			if(!trackX.isEmpty()){
				String car = trackX.remove(trackX.size() - 1);
				trackY.add(0, car);

				if(car == "*")
					yard.setEngineVertex(y);z
				//System.out.println("moved " + car + " right from track " + x + " to " + y);
			}
		}
	}
	public static void Left(TrainGraph yard, int y, int x){

		if(!yard.hasNeighbor(y, x))
			return;
		
		ArrayList<String> trackX = yard.getList(x);
		ArrayList<String> trackY = yard.getList(y);

		if(trackX.contains("*") || trackY.contains("*")){
			if(!trackY.isEmpty()){
				String car = trackY.remove(0);
				trackX.add(car);

				if(car == "*")
					yard.setEngineVertex(x);

				//System.out.println("moved " + car + " left from track " + y + " to " + x);
			}
		}
	}
	public static int printToFile(int fileNumber, String filePostfix, StateGraph<Action> stateGraph, State finalState){
		State currentState = stateGraph.getState(finalState.getHashCode());
		String[][] parentState = currentState.getParentState();
		Action<Integer> actionTaken = currentState.getActionTaken();
		System.out.println(currentState.asString());
		int total = 0;
		PrintWriter writer = null;
		String fileName = "yard" + fileNumber + filePostfix +".txt";
		try {
			writer = new PrintWriter(fileName);
		} catch (IOException ex) {
			// Report
		}
		while(parentState != null){
			writer.println(Arrays.deepToString(parentState) + " " + actionTaken.toString() + " Path cost: " + currentState.getPathCost());
			currentState = stateGraph.getState(currentState.getHashCode(Arrays.deepHashCode(parentState), currentState.getParentStringForm()));
			actionTaken = currentState.getActionTaken();
			parentState = currentState.getParentState();
			total++;
		}
		writer.close();
		return total;
	}
}