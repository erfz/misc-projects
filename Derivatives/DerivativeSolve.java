import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class DerivativeSolve{
	public static void main(String[] args){
		Expression f = new ExpressionBuilder("x^3")
                .variables("x")
                .build();
        System.out.println(nMethod(f, 200, "x", -100, 100));
	}
	static double numericDer(Expression func, String var, double evalAt){
		double x = evalAt;
		double h = x * Math.sqrt(Math.ulp(1.0)); // h = sqrt(ɛ) * x is suitable (noted in Numerical Differentiation) -- where should ɛ be evaluated at however?

		func.setVariable(var, x+h);
		double a = func.evaluate();

		func.setVariable(var, x-h);
		double b = func.evaluate();

		double derivative = (a-b) / (2*h); // need to write a function class...
		return derivative;
	}
	static double nMethod(Expression func, int numIter, String var, double a, double b){ // Newton's Method
		double x = findStartVal(func, var, a, b);
		func.setVariable(var, x);
		double fEval = func.evaluate();
		double fDerEval = numericDer(func, var, x);

		while (numIter > 0){
			x -= fEval/fDerEval;
			func.setVariable(var, x);
			fEval = func.evaluate();
			fDerEval = numericDer(func, var, x);
			--numIter;
		}
		return x;
	}
	private static double findStartVal(Expression func, String var, double a, double b){ // computational effect could be simulated by more nMethod iterations in some cases, but not all
		if (!(a < b) && a != b){
			double c = a;
			a = b;
			b = c;
		}
		else if (a == b){
			return a;
		}
		double delta = 0.1; // arbitrarily small, 0.1 is probably sufficient
		String sign = "";
		func.setVariable(var, a);
		double fEval = func.evaluate();
		if (fEval < 0) sign = "-";
		else if (fEval > 0) sign = "+";
		else return a;

		String signLoop = sign;

		while (a < b && sign.equals(signLoop)){ // there must be a zero in (a, b)
			a += delta;
			func.setVariable(var, a);
			fEval = func.evaluate();
			if (fEval < 0) signLoop = "-";
			else if (fEval > 0) signLoop = "+";
			else return a;
		}
		return (2*a - delta)/2;
	}
}