enum Tokens {
   NAME,
   INTEGER,
   STRING,
   LESS_THAN,
   GREATER_THAN,
   LESS_THAN_OR_EQUAL,
   GREATER_THAN_OR_EQUAL,
   EQUAL,
   NOT_EQUAL,
   ASSIGN,
   PLUS,
   MINUS,
   TIMES,
   DIVIDE,
   MOD,
   LEFT_PAREN,
   RIGHT_PAREN,
   LEFT_BRACE,
   RIGHT_BRACE,
   PRINT,
   COLON,
   SEMICOLON,
   COMMA,
   PROGRAM,
   END,
   IF,
   WHILE,
   FOR,
   TO,
   SUB,
   RETURN,
   EOF,
   UNKNOWN
}

public class Token {

	Tokens type;

	// Some tokens have associated values
	Object value;

	int line;

	public Token(Tokens type, Object value, int line) {
		this.type = type;
		this.value = value;
		this.line = line;
	}

	public Token(Tokens type, int line) {
		this.type = type;
		this.value = null;
		this.line = line;
	}

    public String toString() {
        if (this.value != null) {
            return "<" + this.type + ", " + this.value + ", " + this.line + ">";
        } else {
            return "<" + this.type + ", " + this.line + ">";
        }
    }
}