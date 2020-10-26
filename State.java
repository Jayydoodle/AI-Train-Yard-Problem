import java.util.Arrays;
import java.util.Objects;

public class State implements Comparable<State>{

    private String[][] state;
    private String[][] parentState;
    private int hashcode;
    private int pathCost;
    private String stringForm;
    private String parentStringForm;
    private Action<Integer> actionTaken;

    public State(String [][] state){
        this.state = state;
        this.parentState = null;
        this.pathCost = 0;
        hashcode = Arrays.deepHashCode(state);
        stringForm = Arrays.deepToString(state);
    }
    public State(String [][] state, String[][] parentState, Action<Integer> actionTaken, int pathCost){
        this.state = state;
        this.parentState = parentState;
        this.actionTaken = actionTaken;
        this.pathCost = pathCost;
        hashcode = Arrays.deepHashCode(state);
        stringForm = Arrays.deepToString(state);
        parentStringForm = Arrays.deepToString(parentState);
    }
    public String[][] asArray(){
        return state;
    }
    public String[][] getParentState(){
        return parentState;
    }
    public String getParentStringForm(){
        return parentStringForm;
    }
    public int getPathCost(){
        return pathCost;
    }
    public int getHashCode(){
        return Objects.hash(hashcode, stringForm);
    }
    public int getHashCode(int hashcode, String stringForm){
        return Objects.hash(hashcode, stringForm);
    }
    public void setPathCost(int pathCost){
        this.pathCost = pathCost;
    }
    public int countWrong(String[][] finalState){
        int total = 0;
        for(int i = 0; i < state.length; i++){
            if(finalState[i] != null){  
                for(int j = 0; j < state[i].length; j++){
                    if(j < finalState[i].length){
                        if(state[i][j] != finalState[i][j])
                            total++;
                    }
                    else{
                        total++;
                    }
                }
            }
        }
        return total;
    }
    public Action<Integer> getActionTaken(){
        return this.actionTaken;
    }
    public String asString(){
        return stringForm;
    }
    
    @Override
    public int compareTo(State s2) { 
        if (getPathCost() > s2.getPathCost()) 
            return 1; 
        else if (getPathCost() < s2.getPathCost()) 
            return -1; 
        else
            return 0; 
    } 
    @Override
    public String toString(){
        return stringForm;
    }
}