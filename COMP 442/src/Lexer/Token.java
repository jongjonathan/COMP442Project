package Lexer;

public class Token {
    private TokenType tokenType;
    private String lexeme;
    private Position position;

    public void setTokenType(TokenType tokenType){
        this.tokenType = tokenType;
    }
    public TokenType getTokenType(){
        return this.tokenType;
    }

    public void setLexeme(String lexeme){
        this.lexeme = lexeme;
    }
    public String getLexeme(){
        return this.lexeme;
    }
    public void setPosition(int line){
        this.position = new Position(line);
    }
    public Position getPosition(){
        return this.position;
    }
    public Token (String lexeme, TokenType type, Position line){
        this.lexeme = lexeme;
        this.tokenType = type;
        this.position = line;

    }
    public String toString(){
        return "[" + this.getTokenType()+ ", " + this.getLexeme()+", " +this.position+"]";
    }

}
