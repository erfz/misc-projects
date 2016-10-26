import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.math.BigInteger;

public class DerivativeSolve{
	public static void main(String[] args){
		Expression f = new ExpressionBuilder("x^3")
                .variables("x")
                .build();
        System.out.println(numericDer(f, 2, "x", 5));
        System.out.println(numericDer(f, "x", 5));
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
	static double numericDer(Expression func, int timesDiff, String var, double evalAt){ // FIX
		double x = evalAt;
		int n = timesDiff;
		double h = 0.0001;	// correct choice of h here..........???
		double sum = 0;

		for (int i = 0; i < n; ++i){
			sum += Math.pow(-1, i) * binomial(n-1, i).intValue() * numericDer(func, var, x + h*(n - 1 - 2*i));
		}
		sum /= Math.pow(2*h, n-1);

		return sum;
	}
	static double nMethod(Expression func, int numIter, String var, double a, double b){ // Newton's Method
		double x = findStartVal(func, var, a, b);
		func.setVariable(var, x);
		double fEval = func.evaluate();
		double fDerEval = numericDer(func, var, x);

		while (numIter > 0){
			if (fDerEval == 0){
				double h = x * Math.sqrt(Math.ulp(1.0));
				double leftDer = numericDer(func, var, x-h);
				double rightDer = numericDer(func, var, x+h);
				if (fEval < 0){
					if (leftDer < 0) x -= 0.01;
					else if (rightDer > 0) x += 0.01;
				}
				else if (fEval > 0){
					if (leftDer > 0) x -= 0.01;
					else if (rightDer < 0) x += 0.01;
				}
			}
			else x -= fEval/fDerEval;
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
	private static BigInteger binomial(final int N, final int K) {
	    BigInteger ret = BigInteger.ONE;
	    for (int k = 0; k < K; k++) {
	        ret = ret.multiply(BigInteger.valueOf(N-k))
	                 .divide(BigInteger.valueOf(k+1));
	    }
	    return ret;
	}
}