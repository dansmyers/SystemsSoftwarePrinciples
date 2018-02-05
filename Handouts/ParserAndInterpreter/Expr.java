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


	static class IntegerValue extends Expr {
		Integer value;

		public IntegerValue(Integer value) {
			this.value = value;
		}
	}
}