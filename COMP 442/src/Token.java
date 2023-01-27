public class Token {
    private TokenType tokenType;
    private String lexeme;
    private int location;

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

}
