import java.io.*;
import java.util.*;

public class Parser {
     HashMap<String, String[]> hm = new HashMap<String, String[]>();
    private Map<String, ArrayList<TokenType>> followSet = new HashMap<>();
    private Map<String, ArrayList<TokenType>> firstSet = new HashMap<>();
     ArrayList<TokenType> Term = new ArrayList<>();
     ArrayList<String> nonTerm = new ArrayList<>();
     Map<String, String> hash = new HashMap();
     String output = "";
     boolean hasError = false;
    Stack<String> s1= new Stack<>();
    private ArrayList<String> nullable = new ArrayList<>();
    private ArrayList<String> endable = new ArrayList<>();
    String filename="example-polynomial";
//    String filename="parsetest";
    PrintWriter pwError;

    Lexer lex;
    String[] terminals = {
            "ID",
            "INTEGER",
            "FLOAT",
            "DOUBLEEQUAL",
            "ANGLEBRACKETS",
            "LESSTHAN",
            "GREATERTHAN",
            "LESSTHANEQUAL",
            "GREATERTHANEQUAL",
            "ADD",
            "SUBTRACT",
            "MULTIPLY",
            "DIVIDE",
            "EQUAL",
            "OR",
            "AND",
            "NOT",
            "OPENBRACKET",
            "CLOSEDBRACKET",
            "CURLYOPENBRACKET",
            "CURLYCLOSEDBRACKET",
            "SQUAREOPENBRACKET",
            "SQUARECLOSEDBRACKET",
            "SEMICOLON",
            "COMMA",
            "PERIOD",
            "COLON",
            "LAMDAEXPRESSION",
            "DOUBLECOLON",
            "INTEGERKEYWORD",
            "FLOATKEYWORD",
            "VOIDKEYWORD",
            "CLASSKEYWORD",
            "SELFKEYWORD",
            "ISAKEYWORD",
            "WHILEKEYWORD",
            "IFKEYWORD",
            "THENKEYWORD",
            "ELSEKEYWORD",
            "READKEYWORD",
            "WRITEKEYWORD",
            "RETURNKEYWORD",
            "LOCALVARKEYWORD",
            "CONSTRUCTORKEYWORD",
            "ATTRIBUTEKEYWORD",
            "FUNCTIONKEYWORD",
            "PUBLICKEYWORD",
            "PRIVATEKEYWORD",
            "INLINECOMMENT",
            "BLOCKCOMMENT",
          };


    public void Parser() {

        addFirstFollow();

        String table = "COMP 442/parsingTable.csv";
        String line = "";
        String commaDel = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(table))) {
            int row = 0;
            var headers = br.readLine().split(",");
            while ((line = br.readLine()) != null) {
                String[] values = line.split(commaDel);
                //nonvalues[0]);
                for (int col = 0; col < values.length; col++) {
                    hash.put(convertTerminals(values[0]) + "," + convertTerminals(headers[col]), values[col]);
                }
                row++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean parse()
    {
        try {
            //System.out.println("test");

            FileInputStream fin = new FileInputStream("COMP 442/inputOutput/example-polynomial.src");
            pwError = new PrintWriter(new File("COMP 442/inputOutput/" + filename+ ".outsyntaxerrors"));
            PrintWriter pwDerivations = new PrintWriter(new File("COMP 442/inputOutput/" + filename+ ".outderivation"));
            lex = new Lexer(fin, pwError);

            s1.push("$");//
            s1.push("START");

            Token token = lex.getNextToken();
            String top;
            String[] lookahead;
            //System.out.println(token);

            var line = "START";
            while (!s1.peek().equals("$")) {
                //System.out.println(s1);
                while (token.getTokenType() == TokenType.BLOCKCOMMENT || token.getTokenType() == TokenType.INLINECOMMENT) {
                    token = lex.getNextToken();
                }

                top = s1.peek();

                if (top.equals("EPSILON")) {
                    s1.pop();
                    top = s1.peek();
                }

                if(top.equals("$")){
                    System.out.println("end of file");
                    break;
                }
//gets value of the key in the parsing table and stores it in templookahead for the not terminal
                var tempLookahead = hash.get(top +"," + token.getTokenType());


                if(tempLookahead != null){
                    lookahead = tempLookahead.split("→")[1].trim().split(" ");

                }else {

                    lookahead = new String[]{};
                }

                //System.out.println( lookahead.length > 0);

                if(Arrays.asList(terminals).contains(top)){
                    if(top.equals(token.getTokenType().name())){

                        s1.pop();

                        token = lex.getNextToken();

                        while (token.getTokenType() == TokenType.BLOCKCOMMENT || token.getTokenType() == TokenType.INLINECOMMENT) {
                            token = lex.getNextToken();
                        }
                    }else {

                        // handle error
                        skipError(token);
                        hasError = true;
                        return false;

                    }

                }else if(lookahead.length > 0){

                    var nT = s1.pop(); //pop nonterminal


                    Collections.reverse(Arrays.asList(lookahead));
                    //System.out.println(lookahead.length);
                    for(var i : lookahead){
                        s1.push(convertTerminals(i));

                    }

                    Collections.reverse(Arrays.asList(lookahead));

                    if(nT == null){
                        nT = "!";
                    }

                    var tempLine = "";

                    for(var i: lookahead){
                        tempLine += " " + i;

                    }

                    line = line.replace(nT , tempLine).replace("&epsilon","");
                    //write to file

                    output += "START => " + line + "\n";
                    pwDerivations.write(output);
                }else {
                    // handle error
//                    System.out.println(token.getTokenType().name());
                    System.out.println("Error:" + top);
                    skipError(token);
                    hasError = true;
                    return false;

                }

            }
            pwError.close();
            pwDerivations.close();
        }
        catch(Exception e){
        }

        return true;
    }

    public String convertTerminals(String value) {
        try {
            value = value.toUpperCase();
            switch (value) {
                case "INTEGER" -> {
                    return "INTEGERKEYWORD";
                }
                case "FLOAT" -> {
                    return "FLOATKEYWORD";
                }
                case "RPAR" -> {
                    return "CLOSEDBRACKET";
                }
                case "LPAR" -> {
                    return "OPENBRACKET";
                }
                case "RCURBR" -> {
                    return "CURLYCLOSEDBRACKET";
                }
                case "LCURBR" -> {
                    return "CURLYOPENBRACKET";
                }
                case "NEQ" -> {
                    return "NOT";
                }
                case "RSQBR" -> {
                    return "SQUARECLOSEDBRACKET";
                }
                case "LSQBR" -> {
                    return "SQUAREOPENBRACKET";
                }
                case "FLOATLIT" -> {
                    return "FLOAT";
                }
                case "INTLIT" -> {
                    return "INTEGER";
                }
                case "ARROW" -> {
                    return "LAMDAEXPRESSION";
                }
                case "EQUAL" -> {
                    return "EQUAL";
                }
                case "SR" -> {
                    return "DOUBLECOLON";
                }
                case "PUBLIC" -> {
                    return "PUBLICKEYWORD";
                }
                case "DOT" -> {
                    return "PERIOD";
                }
                case "SEMI" -> {
                    return "SEMICOLON";
                }
                case "RETURN" -> {
                    return "RETURNKEYWORD";
                }
                case "WRITE" -> {
                    return "WRITEKEYWORD";
                }
                case "READ" -> {
                    return "READKEYWORD";
                }
                case "WHILE" -> {
                    return "WHILEKEYWORD";
                }
                case "ELSE" -> {
                    return "ELSEKEYWORD";
                }
                case "THEN" -> {
                    return "THENKEYWORD";
                }
                case "IF" -> {
                    return "IFKEYWORD";
                }
                case "MINUS" -> {
                    return "SUBTRACT";
                }
                case "PLUS" -> {
                    return "ADD";
                }
                case "VOID" -> {
                    return "VOIDKEYWORD";
                }
                case "GEQ" -> {
                    return "GREATERTHANEQUAL";
                }
                case "LEQ" -> {
                    return "LESSTHANEQUAL";
                }
                case "GT" -> {
                    return "GREATERTHAN";
                }
                case "LT" -> {
                    return "LESSTHAN";
                }
                case "EQ" -> {
                    return "EQUAL";
                }
                case "ISA" -> {
                    return "ISAKEYWORD";
                }
                case "DIV" -> {
                    return "DIVIDE";
                }
                case "MULT" -> {
                    return "MULTIPLY";
                }
                case "ATTRIBUTE" -> {
                    return "ATTRIBUTEKEYWORD";
                }
                case "CONSTRUCTOR" -> {
                    return "CONSTRUCTORKEYWORD";
                }
                case "FUNCTION" -> {
                    return "FUNCTIONKEYWORD";
                }
                case "LOCALVAR" -> {
                    return "LOCALVARKEYWORD";
                }
                case "CLASS" -> {
                    return "CLASSKEYWORD";
                }
                case "PRIVATE" -> {
                    return "PRIVATEKEYWORD";
                }
                case "&EPSILON" -> {
                    return "EPSILON";
                }
                default -> {
                    return value;
                }
            }
        } catch (Exception e) {

        }

        return "";
    }
    private Token skipError(Token lookahead) {
        System.out.println("syntax error at " + lookahead.getPosition() + "\n");
        pwError.write("syntax error at " + lookahead.getPosition() + "\n");
        String top = s1.peek();
        Token token = lookahead;

        if (token.getTokenType() == TokenType.ENDOFFILE || followSet.get(top).contains(token.getTokenType())) {
            s1.pop();
        } else {
            while (!firstSet.get(top).contains(token.getTokenType())
                    || (!(firstSet.get(top).contains("&epsilon")
                    && followSet.get(top).contains(token.getTokenType()))
                    && token.getTokenType() != TokenType.ENDOFFILE)) {
                token = lex.getNextToken();
            }
        }

        return token;
    }
    private void addFirstFollow() {
        String firstSetFile = "COMP 442/firstFollow.csv";
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(firstSetFile))) {
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] initialSplit = line.split(",");
                String key = initialSplit[0];
                if (initialSplit[3].contains("yes")) {
                    nullable.add(key.toUpperCase());
                }
                if (initialSplit[3].contains("no")) {
                    endable.add(key.toUpperCase());
                }
                String[] firstSplit = initialSplit[1].split(" ");
                String[] followSplit = initialSplit[2].split(" ");

                ArrayList<TokenType> firstVal = new ArrayList<>();
                for (int i = 0; i < firstSplit.length; i++) {
                    if (firstSplit[i].contains("∅")) {
                        firstVal.add(TokenType.EPSILON);
                    } else {
                        String valueToAdd = firstSplit[i].toUpperCase();
                        switch (valueToAdd) {
                            case "INTEGER" -> firstVal.add(TokenType.INTEGERKEYWORD);

                            case "FLOAT" -> firstVal.add(TokenType.FLOATKEYWORD);

                            case "RPAR" -> firstVal.add(TokenType.CLOSEDBRACKET);

                            case "LPAR" -> firstVal.add(TokenType.OPENBRACKET);

                            case "RCURBR" -> firstVal.add(TokenType.CURLYCLOSEDBRACKET);

                            case "LCURBR" -> firstVal.add(TokenType.CURLYOPENBRACKET);

                            case "NEQ" -> firstVal.add(TokenType.NOT);

                            case "RSQBR" -> firstVal.add(TokenType.SQUARECLOSEDBRACKET);

                            case "LSQBR" -> firstVal.add(TokenType.SQUAREOPENBRACKET);

                            case "FLOATLIT" -> firstVal.add(TokenType.FLOAT);

                            case "INTLIT" -> firstVal.add(TokenType.INTEGER);

                            case "ARROW" -> firstVal.add(TokenType.LAMDAEXPRESSION);

                            case "EQUAL" -> firstVal.add(TokenType.EQUAL);

                            case "SR" -> firstVal.add(TokenType.DOUBLECOLON);

                            case "PUBLIC" -> firstVal.add(TokenType.PUBLICKEYWORD);

                            case "DOT" -> firstVal.add(TokenType.PERIOD);

                            case "SEMI" -> firstVal.add(TokenType.SEMICOLON);

                            case "RETURN" -> firstVal.add(TokenType.RETURNKEYWORD);

                            case "WRITE" -> firstVal.add(TokenType.WRITEKEYWORD);

                            case "READ" -> firstVal.add(TokenType.READKEYWORD);

                            case "WHILE" -> firstVal.add(TokenType.WHILEKEYWORD);

                            case "ELSE" -> firstVal.add(TokenType.ELSEKEYWORD);

                            case "THEN" -> firstVal.add(TokenType.THENKEYWORD);

                            case "IF" -> firstVal.add(TokenType.IFKEYWORD);

                            case "MINUS" -> firstVal.add(TokenType.SUBTRACT);

                            case "PLUS" -> firstVal.add(TokenType.ADD);

                            case "VOID" -> firstVal.add(TokenType.VOIDKEYWORD);

                            case "GEQ" -> firstVal.add(TokenType.GREATERTHANEQUAL);

                            case "LEQ" -> firstVal.add(TokenType.LESSTHANEQUAL);

                            case "GT" -> firstVal.add(TokenType.GREATERTHAN);

                            case "LT" -> firstVal.add(TokenType.LESSTHAN);

                            case "EQ" -> firstVal.add(TokenType.EQUAL);

                            case "ISA" -> firstVal.add(TokenType.ISAKEYWORD);

                            case "DIV" -> firstVal.add(TokenType.DIVIDE);

                            case "MULT" -> firstVal.add(TokenType.MULTIPLY);

                            case "ATTRIBUTE" -> firstVal.add(TokenType.ATTRIBUTEKEYWORD);

                            case "CONSTRUCTOR" -> firstVal.add(TokenType.CONSTRUCTORKEYWORD);

                            case "FUNCTION" -> firstVal.add(TokenType.FUNCTIONKEYWORD);

                            case "LOCALVAR" -> firstVal.add(TokenType.LOCALVARKEYWORD);

                            case "CLASS" -> firstVal.add(TokenType.CLASSKEYWORD);

                            case "PRIVATE" -> firstVal.add(TokenType.PRIVATEKEYWORD);

                            case "&epsilon" -> firstVal.add(TokenType.EPSILON);

                            default -> firstVal.add(TokenType.valueOf(valueToAdd.toUpperCase()));

                        }
                    }
                }
                firstSet.put(key, firstVal);


                ArrayList<TokenType> followVal = new ArrayList<>();
                for (int i = 0; i < followSplit.length; i++) {
                    if (followSplit[i].contains("∅")) {
                        followVal.add(TokenType.EPSILON);
                    } else {
                        String valueToAdd = followSplit[i].toUpperCase();
                        switch (valueToAdd) {
                            case "INTEGER" -> followVal.add(TokenType.INTEGERKEYWORD);

                            case "FLOAT" -> followVal.add(TokenType.FLOATKEYWORD);

                            case "RPAR" -> followVal.add(TokenType.CLOSEDBRACKET);

                            case "LPAR" -> followVal.add(TokenType.OPENBRACKET);

                            case "RCURBR" -> followVal.add(TokenType.CURLYCLOSEDBRACKET);

                            case "LCURBR" -> followVal.add(TokenType.CURLYOPENBRACKET);

                            case "NEQ" -> followVal.add(TokenType.NOT);

                            case "RSQBR" -> followVal.add(TokenType.SQUARECLOSEDBRACKET);

                            case "LSQBR" -> followVal.add(TokenType.SQUAREOPENBRACKET);

                            case "FLOATLIT" -> followVal.add(TokenType.FLOAT);

                            case "INTLIT" -> followVal.add(TokenType.INTEGER);

                            case "ARROW" -> followVal.add(TokenType.LAMDAEXPRESSION);

                            case "EQUAL" -> followVal.add(TokenType.EQUAL);

                            case "SR" -> followVal.add(TokenType.DOUBLECOLON);

                            case "PUBLIC" -> followVal.add(TokenType.PUBLICKEYWORD);

                            case "DOT" -> followVal.add(TokenType.PERIOD);

                            case "SEMI" -> followVal.add(TokenType.SEMICOLON);

                            case "RETURN" -> followVal.add(TokenType.RETURNKEYWORD);

                            case "WRITE" -> followVal.add(TokenType.WRITEKEYWORD);

                            case "READ" -> followVal.add(TokenType.READKEYWORD);

                            case "WHILE" -> followVal.add(TokenType.WHILEKEYWORD);

                            case "ELSE" -> followVal.add(TokenType.ELSEKEYWORD);

                            case "THEN" -> followVal.add(TokenType.THENKEYWORD);

                            case "IF" -> followVal.add(TokenType.IFKEYWORD);

                            case "MINUS" -> followVal.add(TokenType.SUBTRACT);

                            case "PLUS" -> followVal.add(TokenType.ADD);

                            case "VOID" -> followVal.add(TokenType.VOIDKEYWORD);

                            case "GEQ" -> followVal.add(TokenType.GREATERTHANEQUAL);

                            case "LEQ" -> followVal.add(TokenType.LESSTHANEQUAL);

                            case "GT" -> followVal.add(TokenType.GREATERTHAN);

                            case "LT" -> followVal.add(TokenType.LESSTHAN);

                            case "EQ" -> followVal.add(TokenType.EQUAL);

                            case "ISA" -> followVal.add(TokenType.ISAKEYWORD);

                            case "DIV" -> followVal.add(TokenType.DIVIDE);

                            case "MULT" -> followVal.add(TokenType.MULTIPLY);

                            case "ATTRIBUTE" -> followVal.add(TokenType.ATTRIBUTEKEYWORD);

                            case "CONSTRUCTOR" -> followVal.add(TokenType.CONSTRUCTORKEYWORD);

                            case "FUNCTION" -> followVal.add(TokenType.FUNCTIONKEYWORD);

                            case "LOCALVAR" -> followVal.add(TokenType.LOCALVARKEYWORD);

                            case "CLASS" -> followVal.add(TokenType.CLASSKEYWORD);

                            case "PRIVATE" -> followVal.add(TokenType.PRIVATEKEYWORD);

                            case " " -> followVal.add(TokenType.EPSILON);

                            default -> followVal.add(TokenType.valueOf(valueToAdd.toUpperCase()));
                    }
                }
                followSet.put(key, followVal);
            }
        }
        }
        catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        Parser p = new Parser();
        p.Parser();
        p.parse();
        System.out.println(p.output);



    }
}
