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


	//*** Build an identifier: returns a NAME or keyword Token ***//
	public Token analyzeIdentifier() {
        StringBuilder identifier = new StringBuilder();

        while (true) {
            char c = nextCharacter();

            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_') {
                unread();
                break;
            } else {
                identifier.append((char) c);
            }
        }

        String identString = identifier.toString();

        if (identString.equalsIgnoreCase("print")) {
        	return new Token(Tokens.PRINT, this.line);
        } else if (identString.equalsIgnoreCase("program")) {
        	return new Token(Tokens.PROGRAM, this.line);
        } else if (identString.equalsIgnoreCase("if")) {
        	return new Token(Tokens.IF, this.line);
        } else if (identString.equalsIgnoreCase("while")) {
        	return new Token(Tokens.WHILE, this.line);
        } else if (identString.equalsIgnoreCase("sub")) {
        	return new Token(Tokens.SUB, this.line);
        } else if (identString.equalsIgnoreCase("for")) {
        	return new Token(Tokens.FOR, this.line);
        } else if (identString.equalsIgnoreCase("to")) {
        	return new Token(Tokens.TO, this.line);
        } else if (identString.equalsIgnoreCase("end")) {
        	return new Token(Tokens.END, this.line);
        } else if (identString.equalsIgnoreCase("return")) {
        	return new Token(Tokens.RETURN, this.line);
        }

        else {
        	return new Token(Tokens.NAME, identString, this.line);
        }
	}


	//*** Integer number literals ***//
	public Token analyzeNumber() {
		int value = 0;

		while (true) {
			char c = nextCharacter();

			if (Character.isDigit(c)) {
                value = value * 10;
                value += Character.getNumericValue(c);

			} else {
				unread();
				break;
			}
        }

        return new Token(Tokens.INTEGER, value, this.line);
	}



	//*** Build a string: returns a STRING token with a String value ***//
	public Token analyzeString() {
	    StringBuilder text = new StringBuilder();

        while (true) {
            char c = nextCharacter();

            if (c == '"') {
                break;
            } else if (c == 0) {
                Driver.error("EOF reached in string.", this.line);
            } else {
                text.append((char) c);
            }
        }

		return new Token(Tokens.STRING, text.toString(), this.line);
	}


	//*** Find and return the next Token ***//
	public Token nextToken() {

        while (true) {
            char c = nextCharacter();

            if (c == 0) {
            	return new Token(Tokens.EOF, this.line);
            }

    		// NAME
            if (Character.isLetter(c)) {
                unread();
                return analyzeIdentifier();
            }

            // NUMBER
            else if (Character.isDigit(c) || c == '.') {
                unread();
                return analyzeNumber();
            }

            else if (c == '"') {
                return analyzeString();
            }

            // Math symbols
            else if (c == '+') {
                return new Token(Tokens.PLUS, this.line);
            } else if (c == '*') {
                return new Token(Tokens.TIMES, this.line);
            } else if (c == '/') {
                return new Token(Tokens.DIVIDE, this.line);
            } else if (c == '-') {
                return new Token(Tokens.MINUS, this.line);
            } else if (c == '(') {
                return new Token(Tokens.LEFT_PAREN, this.line);
            } else if (c == ')') {
                return new Token(Tokens.RIGHT_PAREN, this.line);
            } else if (c == '%') {
                return new Token(Tokens.MOD, this.line);
            }

            // ASSIGN and COLON
            else if (c == ':') {

                int next = nextCharacter();

                if (next == '=') {
                    return new Token(Tokens.ASSIGN, this.line);
                } else {
                	unread();
                    return new Token(Tokens.COLON, this.line);
                }
            }

            // Relational operators
            else if (c == '=') {
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
                int next = nextCharacter();

                if (next == '=') {
                    return new Token(Tokens.LESS_THAN_OR_EQUAL, this.line);
                } else if (next == '>') {
                    return new Token(Tokens.NOT_EQUAL, this.line);
                } else {
                    unread();
                    return new Token(Tokens.LESS_THAN, this.line);
                }
            }

            // Newline
            else if (c == '\n') {
                this.line++;
            }

            // COMMA
            else if (c == ',') {
                return new Token(Tokens.COMMA, this.line);
            }

            // SEMICOLON
            else if (c == ';') {
                return new Token(Tokens.SEMICOLON, this.line);
            }

            // Comment
            else if (c == '{') {

                // Skip everything to the next right brace
                while (c != '}' && c != 0) {
                    c = nextCharacter();
                }

                // Reaching the end of the file in a comment is an error
                if (c == 0) {
                    Driver.error("End of file in comment.", this.line);
                } else {
                    c = nextCharacter();  // Consume '}'
                }

                // Unread the comment-terminating newline
                unread();
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