# Adding Variables to the Interpreter

This lab will guide you through the code modifications required to add support for variables and assignment statements to your interpreter.

The key to supporting variables is a **symbol table**. which stores a mapping between a variable's name and its value:

- Assigning to a variable is done by updating the value associated with its name in the symbol table.

- Getting the value of a variable is done by looking up the value for a given name.

- Creating a new variable is done by simply adding a new `name->value` mapping to the table.

Your symbol table will be a **Java `HashMap`** object.

## Updated Grammar

Here is the EBNF grammar for the language including assignments.

```
Program --> 'program' NAME ':' Block 'end'

Block --> {Statement}

Statement --> PrintStatement
              | AssignStatement

PrintStatement --> 'print' Expression
              
AssignStatement --> Name ':=' Expression

Expression --> AddExpr

AddExpr --> MultExpr [('+' | '-') AddExpr]

MultExpr --> NegExpr [('*' | '/') MultExpr]

NegExpr --> '-' NegExpr | Atom
                    
Atom --> IntegerLiteral
         | '(' Expression ')'
         | Name
```

Notice that `Statement` now has two choices: `PrintStatement` and `AssignStatement`. An `AssignStatement` is required to begin with a
`Name` token.

`Name` has also been added as a option for `Atom`: this corresponds to using a variable in an expression.

The final new element is `NegExpr`, which now sits between `MultExpr`, which we added last time, and `Atom`. A `NegExpr` implements the
unary negation operator. `NegExpr` can be chained, so it's possible to negate a negation. The following example is valid:

```
program NegationExample:
    print ---(2 + 2)   { Prints -4 } 
end
```

## Get the Starter Code

First, download the starter code, as in our previous labs:

```
prompt$ cd ~/workspace/SystemsSoftwarePrinciples
prompt$ git pull
prompt$ cp -r Handouts/Variables ..
prompt$ cd ../Variables
```

Open up the `Parser`, `Interpreter`, `Expr`, and `Stmt` source files.


## Unary Negation

Implementing `NegExpr` required a few changes. Look at each of the files:

- Adding a `NegExpr` class to `Expr.java`.

- Adding a `negExpr` method to the parser. The method has two cases, one where the `NegExpr` begins with a minus symbol and one where it's simply an atom.

- Adding an `evalNegExpr` method to the interpreter, which also has two cases.

Take a moment and reflect on how the structure of these changes follows directly from the grammar.

The other change is support for nested expressions. This is done by adding a new case to the `atom` methods in both the parser and interpreter. Take a look at those.

## Test

Before you go any further, create a test file that uses nested expressions and negation. Compile the program and verify that it produces the correct output:

```
prompt$ javac *.java
prompt$ java Driver Test.y
```

## Towards Variables

To add support for variables, we have to do the following:

- Create an `AssignStmt` class in `Stmt.java` and a `VarAccess` class in `Expr.java`.

- Modify the parser to recognize assignments and variable accesses.

- Add the symbol table to `Interpreter.java`.

- Update the interpreter to process assignments and look up variable values.

## Statements and Expressions

Question: **What is the purpose of the classes defined in `Stmt.java` and `Expr.java`?**

Answer: They are blobs of state that represent nodes in the parse tree. Each class has a set of state variables that represent that 
important children of that node, as defined in the language grammar.

An assignment statement has a variable name on the left hand side and an expression on the right-hand side. Add the following to `Stmt.java`:

```
static class AssignStmt extends Stmt {
    String name;
    Expr expr;
    	
    public AssignStmt (String name, Expr expr) {
        this.name = name;
    		this.expr = expr;
    }
}
```

A variable access is described by the name of the variable. Add this to `Expr.java`:

```
static class VarAccess extends Expr {
    String name;
		
		public VarAccess(String name) {
			this.name = name;
		}
}
```

Before going on, compile your program and fix any errors.

## Parser

There are two places in the parser that need to change. First, adding code to recognize assignment statements. The `stmt` method recognizes each kind of statement. If a statement begins with a `NAME` token, it must be an assignment:

```
public Stmt stmt() {
    if (check(Tokens.PRINT)) {
		    return printStmt();
		} else if (check(Tokens.NAME)) {
		    return assignStmt();
		} else {
			  Driver.error(currentToken().type + " cannot begin a statement.",
			               currentToken().line);
			  return null;
		}
	}
```

Next, add the `assignStmt` method:

```
//*** Name := Expr ***//
public Stmt.AssignStmt assignStmt() {
		String name = (String) currentToken().value;
		consume(Tokens.NAME);
    consume(Tokens.ASSIGN);
    Expr e = expr();
    return new Stmt.AssignStmt(name, e);
}
```

The second set of changes are to the `atom` method, to recognize variables used in expressions:

```
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
		
		// Var name
		else if (check(Tokens.NAME)) {
			  String name = (String) currentToken().value;
			  consume(Tokens.NAME);
			  return new Expr.VarAccess(name);
		}

		// Anything else is an error
		else {
			  Driver.error("Expected atom, found " + currentToken().type, currentToken().line);
			  return null;
		}
}
```

**Compile break!**

Take another break to compile your program (`javac *.java`) and fix any errors that have shown up. Remember to always start with the first error produced by the compiler.

## Interpreter

The symbol table will be a `HashMap` named `environment`, which is the traditional name for this sort of thing. Add it as as class member of `Interpreter.java`. Also add a constructor that initializes an empty `HashMap`.

```
// Environment is the current local scope
HashMap<String, Object> environment;
	
	
public Interpreter() {
		this.environment = new HashMap<String, Object>();
}
```

If it isn't already there, you must add a brief incantation to get `HashMap`.

```
import java.util.HashMap;
```

Imports for the Import God! [Classes for the Class Throne](http://tvtropes.org/pmwiki/pmwiki.php/TabletopGame/Warhammer40000)!

The remaining changes are mirrors of their counterparts in the parser. First, update the `evalStmt` method to detect `AssignStmt`:

```
//*** Evaluate a single statement ***//
public void evalStmt(Stmt stmt) {
		if (stmt instanceof Stmt.PrintStmt) {
		    evalPrintStmt((Stmt.PrintStmt) stmt);
		} else if (stmt instanceof Stmt.AssignStmt) {
			  evalAssignStmt((Stmt.AssignStmt) stmt);
		}

		// Add cases for other kinds of statements
}
```

Then the `evalAssignStmt` method:

```
//*** Assign statement: update mapping in environment table ***//
public void evalAssignStmt(Stmt.AssignStmt stmt) {
		String name = stmt.name;
		Object value = evalExpr(stmt.expr);
		this.environment.put(name, value);
}
```

The method is straighforward: it evaluates the right-hand side expression, then inserts the `name-->value` mapping into `environment`.
Note that this will create a mapping for `name` if it doesn't already exist, so we're able to declare new variables by simply assigning to them, like in Python.

The second set of changes are to incorporate variables into expressions:

```
//*** Evaluate a single atom value ***//
public Object evalAtom(Expr e) {

		// Integer
	    if (e instanceof Expr.IntegerValue) {
			return evalInteger((Expr.IntegerValue) e);
		}
		
		// Var name
		else if (e instanceof Expr.VarAccess) {
			  String name = ((Expr.VarAccess) e).name;

		    if (!this.environment.containsKey(name)) {
			    Driver.error("Unknown variable name " + name + ".");
		    }

		    return this.environment.get(name);
		}

		// Nested expression
		else {
			return evalExpr(e);
		}
}
```

Now compile everything one final time.

## Test Programs

```
{ Print a variable }

program Test:
    a := 999
    print a
end
```

```
{ Math with variables }

program Test:
    a := 5
    b := 4
    c := 6
    sum := a + b + c
    print sum
end
```

```
{ Reassign a variable }

program Test:
    a := 100
    a := -(a * 99)
    print a
end
```

```
{ Error! }

program Test:
    print unknown_var
end
```

## What's Next

Let's reflect on what you've built: a **complete interpreter** program that can
  - Take an input source file
  - Convert it to a stream of tokens using lexical analysis
  - Parse the token stream into a tree (detecting errors along the way)
  - Walk over the tree to execute the program

Your language supports arbitrarily complex integer arithmetic expressions, variables, and dynamic variable declaration.

What other features could we add?

Well, to have a Turing complete language we need the ability to conditionally execute blocks of code and perform loops, which implies support for comparisons using relational operators.

Guess what you need to implement for Phase 2 of the project?

Some other features that might be nifty:

- A mod operator, so we can test for divisibility

- Real-valued numbers

- `for` loops

- Subroutine definitions

- Subroutines that can take arguments and return results

- Lexical scoping, which would allow for subroutines that have their own local variables
