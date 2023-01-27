import java.io.FileInputStream;
import java.util.ArrayList;

public class Lexer {

    private ArrayList<String> tokenSequence;

    public Lexer(FileInputStream Fin ){
        try {

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
                    nextCharPoint = Fin.read();

                    if((char)nextCharPoint == '='){
                        System.out.println("Double equals");

                    }
                    else if((char)charPoint == '>'){
                        System.out.println("lambda");
                    }
                    else if((char)nextCharPoint == ' '){
                        //validate if prev token is good and create the normal equals
                        System.out.println("Normal equals");
                    }
                    else{
                        System.out.println("Normal equals no space");
                    }



                }
                //---------- COLON  -----------
                else if((char)charPoint == ':'){
                    nextCharPoint = Fin.read();
                    if((char)nextCharPoint == ':' ){
                        System.out.println("Double colon");

                    }
                    else if((char)nextCharPoint == ' '){
                        System.out.println("Single colon");

                    }
                    else{
                        System.out.println("Single colon no space");
                    }
                }
                //---------- LESS THAN  -----------
                else if((char)charPoint == '<'){
                    nextCharPoint = Fin.read();
                    if((char)nextCharPoint == '>'){
                        System.out.println("Double arrows");
                    }
                    else if((char)nextCharPoint == '='){
                        System.out.println("less than equal");
                    }
                    else if ((char)nextCharPoint == ' '){
                        System.out.println("less than");

                    }
                    else{
                        System.out.println("less than no space");

                    }

                }
                //---------- GREATER THAN -----------
                else if((char)charPoint == '>'){
                    nextCharPoint = Fin.read();
                    if((char)nextCharPoint == '='){
                        System.out.println("greater than equal");

                    }
                    else if((char)nextCharPoint == ' '){
                        System.out.println("normal greater than");

                    }
                    else{
                        System.out.println("greater than no space");
                    }

                }
                //---------- FORWARD SLASH -----------
                else if((char)charPoint == '/'){
                    nextCharPoint = Fin.read();
                    if((char) nextCharPoint == '/'){
                        prevCharPoint = "//";
                       //while loop for the comment section

                        while((nextCharPoint = Fin.read())!= '\n' && (nextCharPoint)!= '\r'){
                            prevCharPoint +=(char)nextCharPoint;
                        }
                        System.out.println("inline comment " + prevCharPoint);
                        //create token for inline comment to add to arraylist

                    }
                    //block comment
                    else if((char)nextCharPoint == '*'){
                        prevCharPoint = "/*";
                        boolean endComment = false;
                        while(endComment == false){

                            nextCharPoint = Fin.read();

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
                        System.out.println("Block comment " + prevCharPoint);
                    }
                    else if((char)nextCharPoint == ' '){
                        System.out.println("normal forward slash");
                    }
                    else{
                        System.out.println("normal forward slash no space");
                    }
                }
                //---------- PERIOD -----------
                else if((char)charPoint == '.'){
                    boolean isID = isTokenID(prevCharPoint);

                    if(isID == true){
                        //create token for ID alphanum
                    }
                    else{
                        prevCharPoint += '.';
                        while(((nextCharPoint = Fin.read() )>= 48 && (nextCharPoint <= 57)) || (char)nextCharPoint == 'e'|| (char)nextCharPoint == '+' ||(char)nextCharPoint == '-'){
                            prevCharPoint += (char)nextCharPoint;
                        }
                        boolean isFloat = isTokenFloat(prevCharPoint); //checks for multiple e, +, -
                        if(isFloat){
                            //create token with prevCharPoint as its now the full float ex: 123.123
                            //reset prevCharPoint
                            prevCharPoint = ""+nextCharPoint; //if 123.123; then prevCharPoint = ';'
                        }

                    }



                }
                else if((char)charPoint =='*'){

                    //validate token before, if good then create token for before, create token for multiply
                    System.out.println("multiply");
                }
                else if((char)charPoint =='+'){
                    //validate token before, if good then create token for before, create token for add
                    System.out.println("add");
                }
                else if((char)charPoint =='-'){
                    //validate token before, if good then create token for before, create token for subtract
                    System.out.println("subtract");
                }
                else if((char)charPoint ==','){
                    //validate token before, if good then create token for before, create token for comma
                    System.out.println("comma");
                }
                else if((char)charPoint ==';'){
                    //validate token before, if good then create token for before, create token for semicolon
                    System.out.println("semicolon");
                }
                else if((char)charPoint =='('){
                    //validate token before, if good then create token for before, create token for openbracket
                    System.out.println("open bracket");
                }
                else if((char)charPoint ==')'){
                    //validate token before, if good then create token for before, create token for closed bracket
                    System.out.println("close bracket");
                }
                else if((char)charPoint =='{'){
                    //validate token before, if good then create token for before, create token for curly open bracket
                    System.out.println("curly open bracket");
                }
                else if((char)charPoint =='}'){
                    //validate token before, if good then create token for before, create token for curly open bracket
                    System.out.println("curly close bracket");
                }
                else if((char)charPoint =='['){
                    //validate token before, if good then create token for before, create token for curly open bracket
                    System.out.println("open square bracket");
                }
                else if((char)charPoint ==']'){
                    //validate token before, if good then create token for before, create token for curly open bracket
                    System.out.println("close square bracket");
                }

                //for last remaining closing character
                if(prevCharPoint.equals(';') || prevCharPoint.equals(')') || prevCharPoint.equals('}')|| prevCharPoint.equals(']')){
                    //make token for it
                    prevCharPoint = "";
                }




            }

            Fin.close();

        } catch (Exception e) {
            System.out.println(e);
        }



    }
    public boolean isTokenID(String ID){
        //alphanum
        Boolean b = false;
        return b;
    }
    public boolean isTokenFloat(String fl){
        Boolean b = false;
        return b;
    }
    public boolean isInt(String i){
        Boolean b = false;
        return b;
    }
    public boolean isResWord(String res){
        Boolean b = false;
        return b;
    }





}
