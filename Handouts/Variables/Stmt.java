import java.util.ArrayList;

abstract class Stmt {

    static class Block extends Stmt {
    	ArrayList<Stmt> statements;

    	public Block(ArrayList<Stmt> statements) {
    		this.statements = statements;
    	}
    }


    static class PrintStmt extends Stmt {
    	Expr expr;

    	public PrintStmt(Expr expr) {
    		this.expr = expr;
    	}
    }
}