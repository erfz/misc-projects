import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class DerivativeSolve{
	public static void main(String[] args){
		Expression f = new ExpressionBuilder("(10-2x)(16-4x)x^500")
                .variables("x")
                .build();
        System.out.println(numericDer(f, 0.5, "x"));
	}
	static double numericDer(Expression func, double evalAt, String var){
		double x = evalAt;
		double h = x * Math.sqrt(Math.ulp(1.0)); // h = sqrt(ɛ) * x is suitable (noted in Numerical Differentiation) -- where should ɛ be evaluated at however?

		func.setVariable(var, x+h);
		double a = func.evaluate();

		func.setVariable(var, x-h);
		double b = func.evaluate();

		double derivative = (a-b) / (2*h); // need to write a function class...
		return derivative;
	}
}