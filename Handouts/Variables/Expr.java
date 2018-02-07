import java.util.ArrayList;

abstract class Expr {

	static class AddExpr extends Expr {
		Expr left;
		Tokens operator;
		Expr right;

		public AddExpr(Expr left, Tokens op, Expr right) {
			this.left = left;
			this.operator = op;
			this.right = right;
		}
	}


	static class MultExpr extends Expr {
		Expr left;
		Tokens operator;
		Expr right;

		public MultExpr(Expr left, Tokens op, Expr right) {
            this.left = left;
			this.operator = op;
			this.right = right;
		}
	}


	static class NegExpr extends Expr {
		Expr expr;
		boolean hasNegative;

		public NegExpr(Expr expr, boolean hasNegative) {
			this.expr = expr;
			this.hasNegative = hasNegative;
		}
	}


	static class IntegerValue extends Expr {
		Integer value;

		public IntegerValue(Integer value) {
			this.value = value;
		}
	}
}