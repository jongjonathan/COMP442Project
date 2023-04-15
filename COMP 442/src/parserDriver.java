import java.io.FileInputStream;

public class parserDriver {

    public static void main(String[] args) {
        try {
            FileInputStream fin = new FileInputStream("COMP 442/projectInput/" + args[0]);
            String readingFile = args[0];


            Parser p = new Parser();
            p.Parser(readingFile);
            p.parse(readingFile);

            System.out.println(p.output);
        } catch (Exception e) {

        }


    }
}
