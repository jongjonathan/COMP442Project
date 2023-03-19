package Lexer;

public class ErrorToken extends Token {

    private String errorPrint;

    public ErrorToken(String lexeme, TokenType tokenType, Position position) {
        super(lexeme, tokenType, position);
        if (tokenType == TokenType.INVALIDCHAR ) {
            this.errorPrint = "invalidchar";
        } else if (tokenType == TokenType.INVALIDNUM) {
            this.errorPrint = "invalidnum";
        } else if (tokenType == TokenType.INVALIDID) {
            this.errorPrint = "invalidid";
        }
    }
    public String toString(){
        return "["+this.errorPrint+", "+super.getLexeme()+", "+ super.getPosition() +"]";
    }

}
