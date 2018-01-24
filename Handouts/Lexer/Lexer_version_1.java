import java.util.ArrayList;

public class Lexer {
	
	// String containing the program's text
	String program;
	
	// Index of the current character being analyzed
	int index;
	
	// Line counter
	// Used for error reporting and incremented on each newline
	int line;
	
	
	//*** Constructor ***//
	public Lexer(String program) {
		this.program = program;
		this.index = 0;
		this.line = 1;
	}
	

	//*** Back-up one character ***//
	public void unread() {
		this.index--;
	}
	
	
	//*** Return the next character and advance the index pointer ***//
	public char nextCharacter() {
		if (this.index >= this.program.length()) {
			this.index++;
			return (char) 0;  // EOF
		}
		
		char c = this.program.charAt(this.index);
		this.index++;
		return c;
	}

	
	//*** Find and return the next Token ***//
	public Token nextToken() {
		
        while (true) {
            char c = nextCharacter();
            
            if (c == 0) {
            	return new Token(Tokens.EOF, this.line);
            }
            
            // Relational operators
            if (c == '=') {
                return new Token(Tokens.EQUAL, this.line); 
            } else if (c == '>') {
                int next = nextCharacter();
        
                if (next == '=') {
                    return new Token(Tokens.GREATER_THAN_OR_EQUAL, this.line); 
                } else {
                    unread();
                    return new Token(Tokens.GREATER_THAN, this.line);
                }
            } else if (c == '<') {
 
 
            } 
            
            // Default: ignore whitespace
            else if (!Character.isWhitespace(c)) {
                Driver.error("Unexpected character " + (char) c, this.line);
            }
        }
	}
	
	
	//*** Main lexical analysis routine ***//
    //	
    // Scans the input program and returns an ArrayList containing all of its
    // Tokens.
	public ArrayList<Token> analyze() {
		
		ArrayList<Token> tokens = new ArrayList<Token>();
		
		Token t;
		do {
            t = nextToken();
            tokens.add(t);
        } while (t.type != Tokens.EOF);

		return tokens;
	}
}