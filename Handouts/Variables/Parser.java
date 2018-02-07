import java.util.ArrayList;

public class Parser {

	// List of all program tokens
	ArrayList<Token> tokens;

	// Index of the next token
	int index;

	public Parser(ArrayList<Token> tokens) {
		this.tokens = tokens;
		this.index = 0;
	}

	//*** Return true if the current token matches the expected type ***//
	//
	// Note that this does not consume the token.
	public boolean check(Tokens expectedType) {
		Token t = this.tokens.get(this.index);

		if (t.type == expectedType) {
			return true;
		} else {
			return false;
		}
	}

	//*** Check the type of the current Token ***//
	//
	// If it doesn't match the expected type, generate an error
	public void consume(Tokens expectedType) {
		Token t = tokens.get(index);

		if (t.type != expectedType) {
			Driver.error("Expected " + expectedType + ", found " +
			                        t.type + ".", t.line);
		} else {
			index++;
		}
	}


	//*** Return the current token ***//
	public Token currentToken() {
		return tokens.get(index);
	}


	public Expr.IntegerValue integer() {
		Integer value = (Integer) currentToken().value;
		consume(Tokens.INTEGER);
		return new Expr.IntegerValue(value);
	}


	//*** An atom is a single value ***//
	public Expr atom() {

		// Integer literal
        if (check(Tokens.INTEGER)) {
			return integer();
		}

		// Nested expression
		else if (check(Tokens.LEFT_PAREN)) {
			consume(Tokens.LEFT_PAREN);
			Expr e = expr();
			consume(Tokens.RIGHT_PAREN);
			return e;
		}

		// Anything else is an error
		else {
			Driver.error("Expected atom, found " + currentToken().type, currentToken().line);
			return null;
		}
	}


	//*** '-' NegExpr | Atom ***//
	public Expr negExpr() {
		Expr expr = null;
		boolean hasNegative = false;

		if (check(Tokens.MINUS)) {
			hasNegative = true;
			consume(Tokens.MINUS);
			expr = negExpr();
		} else {
			expr = atom();
		}

		return new Expr.NegExpr(expr, hasNegative);
	}


	//*** NegExpr [('*' | '/' | '%') MultExpr] ***//
	public Expr multExpr() {
		Expr left = negExpr();
		Tokens op = null;
		Expr right = null;

		if (check(Tokens.TIMES) || check(Tokens.DIVIDE) || check(Tokens.MOD)) {
	        op = currentToken().type;
			consume(op);
			right = multExpr();
		}

		return new Expr.MultExpr(left, op, right);
	}


	//*** MultExpr [('+' | '-') AddExpr] ***//
	public Expr addExpr() {
		Expr left = multExpr();
		Tokens op = null;
		Expr right = null;

		if (check(Tokens.PLUS) || check(Tokens.MINUS)) {
			op = currentToken().type;
			consume(op);
			right = addExpr();
		}

		return new Expr.AddExpr(left, op, right);
	}


	//*** A wrapper for a generic expression ***//
	public Expr expr() {
		return addExpr();
	}


	//*** 'print' Expression ***//
	public Stmt.PrintStmt printStmt() {

		// Eat the print keyword that got us into the method
		consume(Tokens.PRINT);

		// Parse the expression
		Expr e = expr();

		// Return a PrintStmt object containing a reference to the expression
		return new Stmt.PrintStmt(e);
	}


	//*** A single statement ***//
	public Stmt stmt() {
		if (check(Tokens.PRINT)) {
			return printStmt();
		} else {
			Driver.error(currentToken().type + " cannot begin a statement.",
			              currentToken().line);
			return null;
		}
	}


	//*** A block of consecutive statements ***//
	public Stmt.Block block() {
		ArrayList<Stmt> statements = new ArrayList<Stmt>();

		while (!check(Tokens.END)) {
			Stmt s = stmt();  // Parse the next statement in the block
			statements.add(s);
		}

		return new Stmt.Block(statements);
	}


	//*** A program has a block for its body ***//
	public Program parse() {
	    consume(Tokens.PROGRAM);
	    consume(Tokens.NAME);
	    consume(Tokens.COLON);
	    Stmt.Block body = block();
	    consume(Tokens.END);
	    consume(Tokens.EOF);

	    Program p = new Program(body);
	    return p;
	}
}