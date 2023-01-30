import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private ArrayList<Token> tokenSequence;
    private int tokenCount;
    public static ArrayList<String> resWords = new ArrayList<>(Arrays.asList("integer", "float", "void", "class", "self", "isa", "while", "if", "then", "else", "read", "write", "return", "localvar", "constructor",
            "attribute", "function", "public", "private"));
    public static ArrayList<String> opWords = new ArrayList<>(Arrays.asList("and", "not", "or"));
    private PrintWriter pwErrors;

    public Lexer(FileInputStream Fin, PrintWriter pwErrors){
        try {
            tokenSequence = new ArrayList<>(10);
            this.pwErrors = pwErrors;
            tokenCount = 0;
            int countLinePos = 1;
            int charPoint;
            int nextCharPoint;
            boolean alreadyCount = false;
            boolean lastCharNL = false;
            int counted = 0;
            String prevCharPoint = "";
            //reading character of the file
            while ((charPoint = Fin.read()) != -1) {
                //---------- EQUALS -----------
                if((char)charPoint == '='){
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before =
                        prevCharPoint =""; //reset
                    }

                    nextCharPoint = Fin.read(); //READ NEXT CHAR

                    if((char)nextCharPoint == '='){
                        tokenSequence.add(new Token("==", TokenType.DOUBLEEQUAL, new Position(countLinePos)));
//                        System.out.println("Double equals");

                    }
                    else if((char)nextCharPoint == '>'){
                        tokenSequence.add(new Token("=>", TokenType.LAMDAEXPRESSION, new Position(countLinePos)));
//                        System.out.println("lambda");
                    }
                    else if((char)nextCharPoint == ' '){
                        //validate if prev token is good and create the normal equals
                        tokenSequence.add(new Token("=", TokenType.EQUAL, new Position(countLinePos)));
//                        System.out.println("Normal equals");
                    }
                    else{
                        tokenSequence.add(new Token("=", TokenType.EQUAL, new Position(countLinePos)));
                        prevCharPoint = ""+(char)nextCharPoint; //reset ex: x=5, nextCharPoint is 5, save into prevCharPoint to be read next iteration
//                        System.out.println("Normal equals no space");
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
//                        System.out.println("Double colon");

                    }
                    else if((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token(":", TokenType.COLON, new Position(countLinePos)));
//                        System.out.println("Single colon");

                    }
                    else{
                        tokenSequence.add(new Token(":", TokenType.COLON, new Position(countLinePos)));
                        prevCharPoint = "" + (char)nextCharPoint; //reset
//                        System.out.println("Single colon no space");
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
//                        System.out.println("Angle brackets");
                    }
                    else if((char)nextCharPoint == '='){
                        tokenSequence.add(new Token("<=", TokenType.LESSTHANEQUAL, new Position(countLinePos)));
//                        System.out.println("less than equal");
                    }
                    else if ((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token("<", TokenType.LESSTHAN, new Position(countLinePos)));
//                        System.out.println("less than");

                    }
                    else{
                        tokenSequence.add(new Token("<", TokenType.LESSTHAN, new Position(countLinePos)));
                        prevCharPoint = ""+ (char)nextCharPoint;
//                        System.out.println("less than no space");
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
//                        System.out.println("greater than equal");

                    }
                    else if((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token(">", TokenType.GREATERTHAN, new Position(countLinePos)));
//                        System.out.println("normal greater than");

                    }
                    else{
                        tokenSequence.add(new Token(">", TokenType.GREATERTHAN, new Position(countLinePos)));
                        prevCharPoint = "" + (char)nextCharPoint;
//                        System.out.println("greater than no space");
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

                        while((char)(nextCharPoint = Fin.read())!= '\n'){// && nextCharPoint != 10 ){
                            if((char)(nextCharPoint)== '\r'){
                                continue;
                            }
                            prevCharPoint +=(char)nextCharPoint;
                        }

                        tokenSequence.add(new Token(prevCharPoint, TokenType.INLINECOMMENT, new Position(countLinePos)));
                        countLinePos++;
//                        System.out.println("inline comment " + prevCharPoint);
                        prevCharPoint =""; //reset
                    }
                    //block comment
                    else if((char)nextCharPoint == '*'){
                        prevCharPoint = "/*";
                        boolean endComment = false;
                        //initial comment line
                        int firstRowCom = countLinePos;
                        while(endComment == false){

                            nextCharPoint = Fin.read();
                            if((char)(nextCharPoint)== '\r'){
                                continue;
                            }
                            if((char)nextCharPoint == '\n'){// || (char)nextCharPoint == '\r' || nextCharPoint == 10 ){
//                            if((char)nextCharPoint == '\r'){
                                countLinePos ++;
                                prevCharPoint+="\\n";
                                continue;
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
                        tokenSequence.add(new Token(prevCharPoint, TokenType.BLOCKCOMMENT, new Position(firstRowCom)));
//                        System.out.println("Block comment " + prevCharPoint);
                        prevCharPoint = "";
                    }
                    else if((char)nextCharPoint == ' '){
                        tokenSequence.add(new Token("/", TokenType.DIVIDE, new Position(countLinePos)));
//                        System.out.println("normal forward slash");
                    }
                    else{
                        tokenSequence.add(new Token("/", TokenType.DIVIDE, new Position(countLinePos)));
                        prevCharPoint = ""+ (char)nextCharPoint;
//                        System.out.println("normal forward slash no space");
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
                            if((char)nextCharPoint!= ' '&& (char)nextCharPoint!= '\n' &&(char)nextCharPoint!= '\r'){//&& (char)nextCharPoint!= '\r'&& nextCharPoint!= 10 ){
                                prevCharPoint = ""+(char)nextCharPoint; //if 123.123; then prevCharPoint = ';'
                                //create token with prevCharPoint as it's now the full float ex: 123.123
                                //reset prevCharPoint
                            }
                            else{
                                prevCharPoint = "";
                                if((char)nextCharPoint == '\n'){// || (char)nextCharPoint == '\r' || nextCharPoint == 10 ){
//                            else if((char)nextCharPoint == '\r'){
                                    countLinePos ++;
                                }
                            }

                        }
                        else{
                            printErrorToken(prevCharPoint, "number", countLinePos); //for errors
//                            System.out.println("this is not valid float or id "+prevCharPoint);
                            if((char)nextCharPoint!= ' '&& (char)nextCharPoint!= '\n' &&(char)nextCharPoint!= '\r'){//&& (char)nextCharPoint!= '\r'&& nextCharPoint!= 10 ){
                                prevCharPoint = ""+(char)nextCharPoint; //if 123.123; then prevCharPoint = ';'
                                //create token with prevCharPoint as it's now the full float ex: 123.123
                                //reset prevCharPoint
                            }
                            else{
                                prevCharPoint = "";
                                if((char)nextCharPoint == '\n'){// || (char)nextCharPoint == '\r' || nextCharPoint == 10 ){
//                            else if((char)nextCharPoint == '\r'){
                                    countLinePos ++;
                                }
                            }

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
//                    System.out.println("multiply");
                }
                else if((char)charPoint =='+'){
                    //validate token before, if good then create token for before, create token for add
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the add
                    }
                    tokenSequence.add(new Token("+", TokenType.ADD, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("add");
                }
                else if((char)charPoint =='-'){
                    //validate token before, if good then create token for before, create token for subtract
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the subtract
                    }
                    tokenSequence.add(new Token("-", TokenType.SUBTRACT, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("subtract");
                }
                else if((char)charPoint ==','){
                    //validate token before, if good then create token for before, create token for comma
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the comma
                    }
                    tokenSequence.add(new Token(",", TokenType.COMMA, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("comma");
                }
                else if((char)charPoint ==';'){
                    //validate token before, if good then create token for before, create token for semicolon
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the semi colon
                    }
                    tokenSequence.add(new Token(";", TokenType.SEMICOLON, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("semicolon");
                }
                else if((char)charPoint =='('){
                    //validate token before, if good then create token for before, create token for openbracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the openbracket
                    }
                    tokenSequence.add(new Token("(", TokenType.OPENBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("open bracket");
                }
                else if((char)charPoint ==')'){
                    //validate token before, if good then create token for before, create token for closed bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the CLOSE BRACKET
                    }
                    tokenSequence.add(new Token(")", TokenType.CLOSEDBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("close bracket");
                }
                else if((char)charPoint =='{'){
                    //validate token before, if good then create token for before, create token for curly open bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the CURLYOPENBRACKET
                    }
                    tokenSequence.add(new Token("{", TokenType.CURLYOPENBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("curly open bracket");
                }
                else if((char)charPoint =='}'){
                    //validate token before, if good then create token for before, create token for curly CLOSE bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the CURLYCLOSEBRACKET
                    }
                    tokenSequence.add(new Token("}", TokenType.CURLYCLOSEDBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("curly close bracket");
                }
                else if((char)charPoint =='['){
                    //validate token before, if good then create token for before, create token for SQUARE open bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the SQUARE OPEN BRACKET
                    }
                    tokenSequence.add(new Token("[", TokenType.SQUAREOPENBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("open square bracket");
                }
                else if((char)charPoint ==']'){
                    //validate token before, if good then create token for before, create token for SQUARE CLOSE bracket
                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos); //token before the SQUARE CLOSE BRACKET
                    }
                    tokenSequence.add(new Token("]", TokenType.SQUARECLOSEDBRACKET, new Position(countLinePos)));
                    prevCharPoint =""; //reset
//                    System.out.println("close square bracket");
                }
                //invalid character
                else if((char)charPoint =='$' || (char)charPoint == '\\' || (char)charPoint =='~' ||
                        (char)charPoint =='!' || (char)charPoint =='@'|| (char)charPoint =='#' || (char)charPoint =='\''){

                    if(prevCharPoint!=""){
                        addALEToken(prevCharPoint, countLinePos);
                    }
                    prevCharPoint =""; //reset
//                    System.out.println("Invalid Char: " + charPoint+ " at line " + countLinePos);
                    //NO CREATION OF INVALID TOKEN
                    // call method to print into hello$hello
                    printErrorToken(""+(char)charPoint, "character", countLinePos); //for errors of invalid char
                }
                //alphanum checker for ID, float, integer (this is for the 3 valid tokens)
                else if((charPoint >= 65 && charPoint <= 90) || (charPoint >= 97 && charPoint <=122) ||
                        (charPoint >= 48 && charPoint <=57) || (char)charPoint == '_'){

                    prevCharPoint += (char)charPoint; //append the alphanum string

                }
                else if((char)charPoint == ' ' || (char)charPoint == '\n'){// || (char)charPoint == '\r'|| charPoint == 10 ){
                    if(prevCharPoint!= ""){
                        addALEToken(prevCharPoint, countLinePos);
                        prevCharPoint = "";
                    }
                    if((char)charPoint == '\n'){ //|| (char)charPoint == '\r' || charPoint == 10){
                        countLinePos ++;
                    }
                }
                //for last remaining closing character
                if(prevCharPoint.equals(";") || prevCharPoint.equals(")") || prevCharPoint.equals("}")|| prevCharPoint.equals("]") || prevCharPoint.equals(",")){
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
            if(prevCharPoint!=""){
                addALEToken(prevCharPoint,countLinePos);
            }


            //Fin.close();
            pwErrors.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
    public void addALEToken(String prevCharPoint, int position){
        TokenType tok = isValidToken(prevCharPoint);
        if(tok!= TokenType.INVALIDCHAR && tok!= TokenType.INVALIDNUM && tok!= TokenType.INVALIDID){
            tokenSequence.add(new Token(prevCharPoint, tok, new Position(position)));
        }
        else{
            if(tok== TokenType.INVALIDCHAR){
                printErrorToken(prevCharPoint, "character", position);
            }
            if(tok== TokenType.INVALIDNUM){
                printErrorToken(prevCharPoint, "number", position);
            }
            if(tok== TokenType.INVALIDID){
                printErrorToken(prevCharPoint, "identifier", position);
            }

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
            if(fl.contains("_")){
                return false;
            }
            if((fl.startsWith("0") && fl.charAt(1)!= '.')|| fl.endsWith("0") && fl.charAt(fl.length()-2) !='.' && !fl.contains("e")){ //return false if leading or trailing zeros or eo
                return false;
            }
            else if(fl.contains("e")){

                if(fl.charAt((fl.indexOf('e')+1)) == ('+')|| (fl.charAt(fl.indexOf('e')+1)) == ('-')){
                    //l side
                    for(int i =0; i<fl.indexOf('e'); i++){
                        if(Character.isDigit(fl.charAt(i)) || fl.charAt(i) == '.')
                        {
                            continue;
                        }
                        return false;
                    }
                    //r side
                    for(int i =fl.indexOf('e')+2; i< fl.length()-fl.indexOf('e'); i++){
                        if(Character.isDigit(fl.charAt(i)))
                        {
                            continue;
                        }
                        return false;
                    }
                    return true;
                }
                if(fl.length() == fl.indexOf('e') + 1){
                    return false; //if e last
                }
                if (fl.charAt((fl.indexOf('e') + 1)) == '+' || fl.charAt((fl.indexOf('e') + 1)) == '-' || Character.isDigit(fl.charAt(fl.indexOf('e')+1))) {
                    // bad case of e+ or e-
                    if (fl.charAt((fl.indexOf('e') + 1)) == '+' || fl.charAt((fl.indexOf('e') + 1)) == '-') {
                        if (fl.length() == fl.indexOf('e') + 2 ) {
                            return false;
                        }
                    }
                    // double check trailing zero before e
                    if (fl.charAt((fl.indexOf('e') - 1)) == '0' && fl.charAt((fl.indexOf('e') - 2)) != '.') {
                        return false;
                    }
                    // double check leading zero
                    if (fl.charAt((fl.indexOf('e') + 1)) == '0' && fl.length() != (fl.indexOf('e') + 2)) {
                        return false;
                    }
                    return true;

                }
                else{
                    return true;
                }

            } //botom of e
            //
            for(int i =0; i<fl.length(); i++){
                if(Character.isDigit(fl.charAt(i)) || fl.charAt(i) == '.')
                {
                    continue;
                }
                return false;
            }
           return true;

        }
        catch(Exception e){
            return false;

        }
    }
    public boolean isTokenInt(String it){

        try{
            //invalid number assignment here
            if((it.startsWith("0") && it.length()!=1)){ //return false if leading zeros
                return false;
            }
            else{
                for(int i = 0; i<it.length() ;i++){
                    if(Character.isDigit(it.charAt(i)))
                    {
                        continue;
                    }
                    return false;
                }
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
        if(isTokenID(prevCharPoint) == true){
            return TokenType.ID;
        }
        if(isTokenInt(prevCharPoint) == true){
            return TokenType.INTEGER;
        }
        if(isTokenFloat(prevCharPoint) == true){
            return TokenType.FLOAT;
        }

        else{
            return IDorNum(prevCharPoint); //check if ID or NUM error
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
    public void printErrorToken(String invLexeme, String type, int lineNum){
        if(type.equals("character")){
            this.pwErrors.write("Lexical error: Invalid character: \"" + invLexeme+"\": line " + lineNum +".\n");
        }
        else if(type.equals("number")){
            this.pwErrors.write("Lexical error: Invalid number: \"" + invLexeme+"\": line " + lineNum+".\n");
        }
        else if(type.equals("identifier")){
            this.pwErrors.write("Lexical error: Invalid identifier: \"" + invLexeme+"\": line " + lineNum+".\n");

        }
    }
    public TokenType IDorNum (String prevCharPoint){
        if (prevCharPoint.charAt(0) == '_') {
            return TokenType.INVALIDID;
        }
        for(int i=0;i<prevCharPoint.length();i++){
            if((int)prevCharPoint.charAt(i) >=65 && (int)prevCharPoint.charAt(i) <=90 || (int)prevCharPoint.charAt(i) >=97 && (int)prevCharPoint.charAt(i) <122){
                return TokenType.INVALIDID;
            }
        }
        return TokenType.INVALIDNUM;
    }






}
