import java.io.*;
import java.util.*;

public class Parser {
     HashMap<String, String[]> hm = new HashMap<String, String[]>();
     ArrayList<TokenType> Term = new ArrayList<>();
     ArrayList<String> nonTerm = new ArrayList<>();
     Map<String, String> hash = new HashMap();
     String output = "";
     boolean hasError = false;
    Stack<String> s1= new Stack<>();
    String prod ="";
    String[] terminals = {"id",
    "intnum",
     "floatnum",
    "eq",
     "gt",
    "lt",
    "noteq",
     "geq",
     "leq",
    "returntype",
    "plus",
    "minus",
    "mult",
    "div",
    "assign",
    "semi",
    "comma",
    "colon",
    "scopeop",
    "or",
    "and",
     "not",
    "openpar",
    "closepar",
     "opensqbr",
    "closesqbr",
    "opencubr",
    "closecubr",
     "dot",
     "integer",
    "float",
    "void",
    "class",
    "self",
    "isa",
    "while",
    "if",
    "then",
    "else",
    "read",
     "write",
    "return",
    "localvar",
     "constructor",
     "attribute",
     "FUNCTIONKEYWORD",
     "public",
     "private",
     "inlinecmt",
     "blockcmt"};


    public void Parser() {

        String fileInput = "example-bubblesort.outlextokens";

        String table = "/Users/jonathanjong/Developer/COMP 442 Project/COMP 442/parsingTable.csv";
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
            for(var token: Term){
                //System.out.println(token);
            }
            //System.out.println();
            for(var token: nonTerm){
                //System.out.println(token);
            }
            //System.out.println(nonTerm.indexOf("APARAMS"));
            //System.out.println(Term.indexOf("ID));
//            System.out.println(hash.get(nonTerm.indexOf("APARAMS") + "," + Term.indexOf("ID)));
            //System.out.println(hash);

        } catch (Exception e) {
            System.out.println("test");
            e.printStackTrace();
        }

    }

    public boolean parse()
    {
        try {
            //System.out.println("test");

            FileInputStream fin = new FileInputStream("COMP 442/inputOutput/example-bubblesort.src");
            PrintWriter pwError = new PrintWriter(new File("COMP 442/inputOutput/sampleParseError"));
            Lexer lex = new Lexer(fin, pwError);
            System.out.println("hash");
            System.out.println(hash);

            s1.push("$");//
            s1.push("START");
            //Read one token from input
            Token token = lex.getNextToken();
            String top;
            //System.out.println(token);

            var line = "START";
            while (!s1.peek().equals("$")) {
                //System.out.println(s1);
                while (token.getTokenType() == TokenType.BLOCKCOMMENT || token.getTokenType() == TokenType.INLINECOMMENT) {
                    token = lex.getNextToken();
                }

                top = s1.peek();

                if (top.equals("&epsilon")) {
                    s1.pop();
                    top = s1.peek();
                }

                if(top.equals("$")){
                    break;
                }

                var tempLookahead = hash.get(top +"," + token.getTokenType());
                //System.out.println(top + "," + token.getTokenType());
                String[] lookahead;

                if(tempLookahead != null){
                    lookahead = tempLookahead.split("→")[1].trim().split(" ");
                }else {
                    lookahead = new String[]{};
                }

                //System.out.println( lookahead.length > 0);

                if(Arrays.asList(terminals).contains(top)){
                    if(top.equals(token.getTokenType())){
                        s1.pop();
                        token = lex.getNextToken();

                        while (token.getTokenType() == TokenType.BLOCKCOMMENT || token.getTokenType() == TokenType.INLINECOMMENT) {
                            token = lex.getNextToken();
                        }
                    }else {
                        // handle error

                        hasError = true;
                        return false;
                    }

                }else if(lookahead.length > 0){

                    var nT = s1.pop();


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

                    line = line.replace(nT , tempLine);
                    output += "START => " + line + "\n";
                }else {
                    // handle error
                    System.out.println(top);
                    hasError = true;
                    return false;
                }

            }
        }
        catch(Exception e){
            System.out.println("test");
            System.out.println(e.getMessage());

        }

        return true;
    }

    public String convertTerminals(String value) {
        try {
            value = value.toUpperCase();
            switch (value) {
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
                case " " -> {
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

    public static void main(String[] args) {
        Parser p = new Parser();
        p.Parser();
        System.out.println(p.parse());
        System.out.println(p.output);
    }
}
