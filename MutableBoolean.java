class MutableBoolean{

    private Boolean bool;

    public MutableBoolean(boolean bool){
        this.bool = bool;
    }
    public void setTrue(){
        this.bool = true;
    }
    public void setFalse(){
        this.bool = false;
    }
    public boolean getValue(){
        return this.bool;
    }
}