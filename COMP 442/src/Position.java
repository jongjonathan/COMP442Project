public class Position {
    public int lineNum;

    public Position(int lineNum){
        this.lineNum = lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getLineNum(){
        return this.lineNum;
    }
    public String toString(){
        return "" + lineNum;
    }
}
