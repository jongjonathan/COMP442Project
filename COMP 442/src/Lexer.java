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

                        while((nextCharPoint = Fin.read())!= '\n' && (nextCharPoint = Fin.read())!= '\r'){
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

            }

            Fin.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }



}
