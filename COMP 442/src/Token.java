public class Token {
    private TokenType tokenType;
    private String lexeme;
    private Position line;

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
    public Token (String lexeme, TokenType type,  Position line){
        this.lexeme = lexeme;
        this.tokenType = type;
        this.line = line;

    }

}
