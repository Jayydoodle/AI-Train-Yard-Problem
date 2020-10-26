import java.util.Arrays;

class Action<VertexType> {

    private String name;
    private VertexType left;
    private VertexType right;
    public String[][] state;
    public String[][] currentState;
    private int hashCode;

    public Action(String name, VertexType left, VertexType right) {
        this.name = name;
        this.left = left;
        this.right = right;
        this.state = null;
        this.hashCode = -1;
    }
    public String[][] getState(){
        return state;
    }
    public String getName() {
        return name;
    }
    public VertexType getOrigin() {
        return left;
    }
    public VertexType getDestination() {
        return right;
    }
    public int getHashCode(){
        return this.hashCode;
    }
    public void setState(String[][] state){
        this.state = state;
        this.hashCode = Arrays.deepHashCode(state);
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(name + " " + left + " " + right);
        return sb.toString();
    }
    @Override
    public int hashCode() {
        return Objects.hash(state);
    }
}