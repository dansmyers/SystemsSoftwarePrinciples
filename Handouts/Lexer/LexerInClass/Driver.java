import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Driver {
	
	//*** Simple error handling routine: reports line number and message ***//
	public static void error(String errorMessage, int lineNumber) {
		System.out.print("Error on line " + lineNumber + ": ");
		System.out.println(errorMessage);
		System.exit(1);
	}
	
	
	//*** Runtime errors -- no line number ***//
	public static void error(String errorMessage) {
		System.out.println(errorMessage);
		System.exit(1);
	}


    //*** Run a script: main driver of the interpreting process ***//
    public void run(String inputPath) throws IOException {
    	// Step 1: Open then file and read its characters into a buffer
    	byte[] bytes = Files.readAllBytes(Paths.get(inputPath));
    	String characters = new String(bytes);

    	// Step 2: Lexical analysis
    	// Returns an ArrayList<Token>
    	Lexer lex = new Lexer(characters);
    	ArrayList<Token> tokens = lex.analyze();

		for (Token t : tokens) {
			System.out.println(t);
		}
		
		//*** Parsing and execution will go here in future versions ***//
    }


	//*** Main: interpret the file name given as a command line argument ***//
	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("Usage: java Driver FILENAME");
			System.exit(0);
		}
		
		Driver Driver = new Driver();
		
		try {
		    Driver.run(args[0]);
		} catch(IOException e) {
		    Driver.error("Could not open file " + args[0] + ".", 0);	
		}
		
	    return;
    }
}