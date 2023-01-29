import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private ArrayList<Token> tokenSequence;
    private int tokenCount;
    public static ArrayList<String> resWords = new ArrayList<>(Arrays.asList("integer", "float", "void", "class", "self", "isa", "while", "if", "then", "else", "read", "write", "return", "localvar", "constructor",
            "attribute", "function", "public", "private"));
    public static ArrayList<String> opWords = new ArrayList<>(Arrays.asList("and", "not", "or"));

    public Lexer(FileInputStream Fin ){
        try {
            tokenCount = 0;
            int countLinePos = 1;
            int charPoint;
            int nextCharPoint;
            String prevCharPoint = "";
            //reading character of the file
            while ((charPoint = Fin.read()) != -1) {

//                System.out.println((char) charPoint);
//                evalChar +=(char)(charPoint);
//                System.out.print(evalChar);
                //for operator/punctuation



                //---------- EQUALS -----------

                if((char)charPoint == '='){
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before =
                    }
                    prevCharPoint =""; //reset
                    nextCharPoint = Fin.read(); //READ NEXT CHAR

                    if((char)nextCharPoint == '='){
                        tokenSequence.add(new Token("==", TokenType.DOUBLEEQUAL, new Position(countLinePos)));
                        System.out.println("Double equals");

                    }
                    else if((char)charPoint == '>'){
                        tokenSequence.add(new Token("=>", TokenType.LAMDAEXPRESSION, new Position(countLinePos)));
                        System.out.println("lambda");
                    }
                    else if((char)nextCharPoint == ' '){
                        //validate if prev token is good and create the normal equals
                        tokenSequence.add(new Token("=", TokenType.EQUAL, new Position(countLinePos)));
                        System.out.println("Normal equals");
                    }
                    else{
                        tokenSequence.add(new Token("=", TokenType.EQUAL, new Position(countLinePos)));
                        prevCharPoint +=(char)nextCharPoint; //reset ex: x=5, nextCharPoint is 5, save into prevCharPoint to be read next iteration
                        System.out.println("Normal equals no space");
                    }



                }
                //---------- COLON  -----------
                else if((char)charPoint == ':'){
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before =
                    }
                    prevCharPoint =""; //reset
                    nextCharPoint = Fin.read();
                    if((char)nextCharPoint == ':' ){
                        tokenSequence.add(new Token("::", TokenType.DOUBLECOLON, new Position(countLinePos)));
                        System.out.println("Double colon");

                    }
                    else if((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token(":", TokenType.COLON, new Position(countLinePos)));
                        System.out.println("Single colon");

                    }
                    else{
                        tokenSequence.add(new Token(":", TokenType.COLON, new Position(countLinePos)));
                        prevCharPoint +=(char)nextCharPoint; //reset
                        System.out.println("Single colon no space");
                    }
                }
                //---------- LESS THAN  -----------
                else if((char)charPoint == '<'){
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before =
                    }
                    prevCharPoint =""; //reset
                    nextCharPoint = Fin.read();
                    if((char)nextCharPoint == '>'){
                        tokenSequence.add(new Token("<>", TokenType.ANGLEBRACKETS, new Position(countLinePos)));
                        System.out.println("Angle brackets");
                    }
                    else if((char)nextCharPoint == '='){
                        tokenSequence.add(new Token("<=", TokenType.LESSTHANEQUAL, new Position(countLinePos)));
                        System.out.println("less than equal");
                    }
                    else if ((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token("<", TokenType.LESSTHAN, new Position(countLinePos)));
                        System.out.println("less than");

                    }
                    else{
                        tokenSequence.add(new Token("<", TokenType.LESSTHAN, new Position(countLinePos)));
                        prevCharPoint +=(char)nextCharPoint;
                        System.out.println("less than no space");

                    }

                }
                //---------- GREATER THAN -----------
                else if((char)charPoint == '>'){
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before =
                    }
                    prevCharPoint =""; //reset
                    nextCharPoint = Fin.read();
                    if((char)nextCharPoint == '='){
                        tokenSequence.add(new Token("=>", TokenType.GREATERTHANEQUAL, new Position(countLinePos)));
                        System.out.println("greater than equal");

                    }
                    else if((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token(">", TokenType.GREATERTHAN, new Position(countLinePos)));
                        System.out.println("normal greater than");

                    }
                    else{
                        tokenSequence.add(new Token(">", TokenType.GREATERTHAN, new Position(countLinePos)));
                        prevCharPoint +=(char)nextCharPoint;
                        System.out.println("greater than no space");
                    }

                }
                //---------- FORWARD SLASH -----------
                else if((char)charPoint == '/'){
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before =
                    }
                    prevCharPoint =""; //reset
                    nextCharPoint = Fin.read();
                    if((char) nextCharPoint == '/'){
                        prevCharPoint = "//";
                       //while loop for the comment section

                        while((char)(nextCharPoint = Fin.read())!= '\n' && (char)(nextCharPoint)!= '\r'){
                            prevCharPoint +=(char)nextCharPoint;
                        }
                        tokenSequence.add(new Token(prevCharPoint, TokenType.INLINECOMMENT, new Position(countLinePos)));

                        System.out.println("inline comment " + prevCharPoint);
                        prevCharPoint =""; //reset

                        //create token for inline comment to add to arraylist

                    }
                    //block comment
                    else if((char)nextCharPoint == '*'){
                        prevCharPoint = "/*";
                        boolean endComment = false;
                        while(endComment == false){

                            nextCharPoint = Fin.read();
                            if((char)nextCharPoint == '\n' || (char)nextCharPoint == '\r'){
                                countLinePos ++;
                            }

                            if((char)nextCharPoint == '*'){
                                prevCharPoint += '*';
                                nextCharPoint = Fin.read();

                                //end of comment
                                if((char)nextCharPoint == '/'){
                                    endComment = true;

                                }
                            }
                            //append characters in comment
                            prevCharPoint+=(char)nextCharPoint;

                        }
                        tokenSequence.add(new Token(prevCharPoint, TokenType.BLOCKCOMMENT, new Position(countLinePos)));
                        System.out.println("Block comment " + prevCharPoint);
                        prevCharPoint = "";
                    }
                    else if((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token("/", TokenType.DIVIDE, new Position(countLinePos)));
                        System.out.println("normal forward slash");
                    }
                    else{
                        tokenSequence.add(new Token("/", TokenType.DIVIDE, new Position(countLinePos)));
                        prevCharPoint +=(char)nextCharPoint;
                        System.out.println("normal forward slash no space");
                    }
                }
                //---------- PERIOD -----------
                else if((char)charPoint == '.'){
                    boolean isID = isTokenID(prevCharPoint);

                    if(isID == true){
                        if(prevCharPoint!=""){
                            //create token for ID alphanum
                            addALEToken(prevCharPoint, countLinePos); //token before
                        }
                        prevCharPoint =""; //reset
                        tokenSequence.add(new Token(".", TokenType.PERIOD, new Position(countLinePos)));

                    }
                    else{
                        prevCharPoint += '.';
                        while(((nextCharPoint = Fin.read() )>= 48 && (nextCharPoint <= 57)) || (char)nextCharPoint == 'e'|| (char)nextCharPoint == '+' ||(char)nextCharPoint == '-'){
                            prevCharPoint += (char)nextCharPoint;
                        }
                        boolean isFloat = isTokenFloat(prevCharPoint); //checks for multiple e, +, -
                        if(isFloat == true){
                            tokenSequence.add(new Token(prevCharPoint, TokenType.FLOAT, new Position(countLinePos)));
                            if((char)nextCharPoint!= ' '|| (char)nextCharPoint!= '\n' || (char)nextCharPoint!= '\r'){
                                prevCharPoint = ""+(char)nextCharPoint; //if 123.123; then prevCharPoint = ';'
                                //create token with prevCharPoint as it's now the full float ex: 123.123
                                //reset prevCharPoint
                            }
                            else if((char)nextCharPoint == '\n' || (char)nextCharPoint == '\r'){
                                countLinePos ++;
                            }
                        }
                        else{
                            System.out.println("this is not valid float or id "+prevCharPoint);
                            prevCharPoint = ""+(char)nextCharPoint; //ex: 123.0;, nextCharPoint is ;
                        }
                    }
                }
                else if((char)charPoint =='*'){

                    //validate token before, if good then create token for before, create token for multiply
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the MULTIPLY
                    }
                    tokenSequence.add(new Token("*", TokenType.MULTIPLY, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("multiply");
                }
                else if((char)charPoint =='+'){
                    //validate token before, if good then create token for before, create token for add
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the add
                    }
                    tokenSequence.add(new Token("+", TokenType.ADD, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("add");
                }
                else if((char)charPoint =='-'){
                    //validate token before, if good then create token for before, create token for subtract
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the subtract
                    }
                    tokenSequence.add(new Token("-", TokenType.SUBTRACT, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("subtract");
                }
                else if((char)charPoint ==','){
                    //validate token before, if good then create token for before, create token for comma
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the comma
                    }
                    tokenSequence.add(new Token(",", TokenType.COMMA, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("comma");
                }
                else if((char)charPoint ==';'){
                    //validate token before, if good then create token for before, create token for semicolon
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the semi colon
                    }
                    tokenSequence.add(new Token(";", TokenType.SEMICOLON, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("semicolon");
                }
                else if((char)charPoint =='('){
                    //validate token before, if good then create token for before, create token for openbracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the openbracket
                    }
                    tokenSequence.add(new Token("(", TokenType.OPENBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("open bracket");
                }
                else if((char)charPoint ==')'){
                    //validate token before, if good then create token for before, create token for closed bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the CLOSE BRACKET
                    }
                    tokenSequence.add(new Token(")", TokenType.CLOSEDBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("close bracket");
                }
                else if((char)charPoint =='{'){
                    //validate token before, if good then create token for before, create token for curly open bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the CURLYOPENBRACKET
                    }
                    tokenSequence.add(new Token("{", TokenType.CURLYOPENBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("curly open bracket");
                }
                else if((char)charPoint =='}'){
                    //validate token before, if good then create token for before, create token for curly CLOSE bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the CURLYCLOSEBRACKET
                    }
                    tokenSequence.add(new Token("}", TokenType.CURLYCLOSEDBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("curly close bracket");
                }
                else if((char)charPoint =='['){
                    //validate token before, if good then create token for before, create token for SQUARE open bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the SQUARE OPEN BRACKET
                    }
                    tokenSequence.add(new Token("[", TokenType.SQUAREOPENBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("open square bracket");
                }
                else if((char)charPoint ==']'){
                    //validate token before, if good then create token for before, create token for SQUARE CLOSE bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the SQUARE CLOSE BRACKET
                    }
                    tokenSequence.add(new Token("]", TokenType.SQUARECLOSEDBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
                    System.out.println("close square bracket");
                }
                //invalid character
                else if((char)charPoint =='$' || (char)charPoint == '\\' || (char)charPoint =='~' ||
                        (char)charPoint =='!' || (char)charPoint =='@'|| (char)charPoint =='#' || (char)charPoint =='\''){
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos);
                    }
                    prevCharPoint =""; //reset
                    System.out.println("Invalid Char: " + charPoint+ " at line " + countLinePos);
                    //NO CREATION OF INVALID TOKEN
                    // call method to print into hello$hello
                }
                //alphanum checker for ID, float, integer (this is for the 3 valid tokens)
                else if((charPoint >= 65 && charPoint <= 90) || (charPoint >= 97 && charPoint <=122) ||
                        (charPoint >= 48 && charPoint <=57) || (char)charPoint == '_'){

                    prevCharPoint += (char)charPoint; //append the alphanum string

                }
                else if((char)charPoint == ' ' || (char)charPoint == '\n' || (char)charPoint == '\r'){
                    if((char)charPoint == '\n' || (char)charPoint == '\r'){
                        countLinePos ++;
                    }
                    if(prevCharPoint!= ""){
                        addALEToken(prevCharPoint, countLinePos);
                        prevCharPoint = "";
                    }
                }
                //for last remaining closing character
                if(prevCharPoint.equals(';') || prevCharPoint.equals(')') || prevCharPoint.equals('}')|| prevCharPoint.equals(']') || prevCharPoint.equals(',')){
                    if(prevCharPoint.equals(";")){
                        tokenSequence.add(new Token(";", TokenType.SEMICOLON, new Position(countLinePos)));
                    }
                    else if(prevCharPoint.equals(")")){
                        tokenSequence.add(new Token(")", TokenType.CLOSEDBRACKET, new Position(countLinePos)));
                    }
                    else if(prevCharPoint.equals("}")){
                        tokenSequence.add(new Token("}", TokenType.CURLYCLOSEDBRACKET, new Position(countLinePos)));
                    }
                    else if(prevCharPoint.equals("]")){
                        tokenSequence.add(new Token("]", TokenType.SQUARECLOSEDBRACKET, new Position(countLinePos)));
                    }
                    else if(prevCharPoint.equals(",")){
                        tokenSequence.add(new Token(",", TokenType.COMMA, new Position(countLinePos)));
                    }
                    //make token for it
                    prevCharPoint = "";
                }
            }

            //Fin.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
    public void addALEToken(String prevCharPoint, int position){
        TokenType tok = isValidToken(prevCharPoint);
        if(tok!= TokenType.ERRORTOKEN){
            tokenSequence.add(new Token(prevCharPoint, tok, new Position(position)));
        }
        else{
            System.out.println("Invalid char " + prevCharPoint);
        }



    }
    public boolean isTokenID(String ID){
        //alphanum
        try{
            //if starts with digit or underscore
            if(Character.isDigit(ID.charAt(0)) || (char)(ID.charAt(0)) == '_'){
                return false;
            }
            else{
                return true;
            }

        }
        catch(Exception e){
            return false;
        }
    }
    public boolean isTokenFloat(String fl){
        float validFloat;
        try{
            //invalid number assignment here
            if(fl.startsWith("0") || fl.endsWith("0") || fl.contains("e0")){ //return false if leading or trailing zeros or eo
                return false;
            }
            else{
                validFloat = Float.parseFloat(fl+"f"); //check if valid float (one e, +, -)
                return true; //if valid float then return true
            }

        }
        catch(Exception e){
            return false;

        }
    }
    public boolean isTokenInt(String i){

        int validInt;
        try{
            //invalid number assignment here
            if(i.startsWith("0")){ //return false if leading zeros
                return false;
            }
            else{
                validInt = Integer.parseInt(i); //check if valid int
                return true; //if valid int then return true
            }

        }
        catch(Exception e){
            return false;

        }
    }
    public boolean isResWord(String res){

        if(resWords.contains(res)){
            return true;
        }
        return false;
    }
    public boolean isOpWord(String op){

        if(opWords.contains(op)){
            return true;
        }
        return false;
    }
    public TokenType isValidToken(String prevCharPoint){
        if(isResWord(prevCharPoint) == true){
            return TokenType.valueOf(prevCharPoint.toUpperCase()+"KEYWORD");
        }
        if(isOpWord(prevCharPoint) == true){
            return TokenType.valueOf(prevCharPoint.toUpperCase());
        }
        if(isTokenInt(prevCharPoint) == true){
            return TokenType.INTEGER;
        }
        if(isTokenFloat(prevCharPoint) == true){
            return TokenType.FLOAT;
        }
        if(isTokenID(prevCharPoint) == true){
            return TokenType.ID;
        }
        else{
            return TokenType.ERRORTOKEN;
        }

    }
    public Token getNextToken(){
        Token tok = null;
        if(tokenCount!= tokenSequence.size()){
            tok = tokenSequence.get(tokenCount);
            tokenCount++;
            return tok;
        }
        else{
            return tok;
        }


    }





}
