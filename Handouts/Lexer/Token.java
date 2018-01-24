enum Tokens {
   NAME,
   INTEGER,
   PLUS,
   MINUS,
   TIMES,
   DIVIDE,
   LESS_THAN,
   GREATER_THAN,
   LESS_THAN_OR_EQUAL,
   GREATER_THAN_OR_EQUAL,
   EQUAL,
   NOT_EQUAL,
   EOF,
   UNKNOWN
}

public class Token {
	
	Tokens type;
	
	// Some tokens have associated values:
	// NAME tokens have a string that is the name of the variable
	// STRING tokens have the string
	// NUMBER tokens have the value of the number
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
        return "<" + this.type + ", value = " + this.value + ", line = " + this.line + ">";
    }
}