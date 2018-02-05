import java.util.HashMap;

public class Interpreter {


    //*** The evaluation of an integer expression is just its value ***//
	public Integer evalInteger(Expr.IntegerValue n) {
		return (Integer) n.value;
	}


	//*** Evaluate a single atom value ***//
	public Object evalAtom(Expr e) {
	    if (e instanceof Expr.IntegerValue) {
			return evalInteger((Expr.IntegerValue) e);
		} else {
			Driver.error("Unexpected expression type for atom: " + e);
			return null;
		}
	}


	//*** An addition expression that works only on atoms ***//
	public Object evalAddExpr(Expr.AddExpr expr) {
		Object left = evalAtom((Expr) expr.left);

		// If there is a right-hand part, evaluate it and then apply the
		// appropriate operator
		Object right = null;
		if (expr.right != null) {
			right = evalAddExpr((Expr.AddExpr) expr.right);

			if (expr.operator == Tokens.PLUS) {
				return (Integer) left + (Integer) right;
			} else if (expr.operator == Tokens.MINUS) {
				return (Integer) left - (Integer) right;
			}
		}

		// Default: return the left value by itself
		return left;
	}


	//*** Wrapper for expression evaluations ***//
	public Object evalExpr(Expr e) {
		return evalAddExpr((Expr.AddExpr) e);
	}


	//*** Print statememt: evaluate expression and print result ***//
	public void evalPrintStmt(Stmt.PrintStmt stmt) {
		Object result = evalExpr(stmt.expr);
		System.out.println(result);
	}


	//*** Evaluate a single statement ***//
	public void evalStmt(Stmt stmt) {
		if (stmt instanceof Stmt.PrintStmt) {
			evalPrintStmt((Stmt.PrintStmt) stmt);
		}

		// Add cases for other kinds of statements
	}


    //*** Evaluate a block of statements ***//
	public void evalBlock(Stmt.Block block) {
		for (Stmt stmt : block.statements) {
		    evalStmt(stmt);
		}
	}


	//*** Evaluate a program by evluating its body ***//
	public void evalProgram(Program program) {
		evalBlock(program.body);
	}

}