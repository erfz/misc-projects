import com.udojava.evalex.Expression;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import java.math.RoundingMode;
import java.math.MathContext;
import java.math.BigDecimal;
import java.math.BigInteger;
// use ApFloat library for function implementations

public class DerivativeSolve{
	public static final BigDecimal H_VALUE = new BigDecimal("1E-8");
	public static final int NEWTON_NUM_DECIMAL = 10;

	public static void main(String[] args){
		Expression f = new Expression("x^3 - 2*x + 5", MathContext.UNLIMITED); // always evaluate in RAD (evalex)
        // System.out.println(numericDer(f, "x", new BigDecimal(Expression.PI.doubleValue()/36)).toString());
        // System.out.println(nMethod(f, 10, "x", new BigDecimal("5"), new BigDecimal("-5")));
        // System.out.println(numericDer(f, 6, "x", 5));
        System.out.println(numericDer(f, 3, "x", new BigDecimal("100000000000000")));
	}
	static BigDecimal numericDer(Expression func, String var, BigDecimal evalAt){
		BigDecimal x = evalAt;
		BigDecimal h = H_VALUE; // double implementation of functions breaks with smaller h's --- make this scale with powers
		BigDecimal a = func.with(var, x.add(h)).eval();
		BigDecimal b = func.with(var, x.subtract(h)).eval();
		// System.out.println(a.toString() + "\n" + b.toString() + "\n" + a.subtract(b).toString() + "\n" + h.toString());

		BigDecimal derivative = a.subtract(b).divide(h.multiply(new BigDecimal("2")));
		return derivative;
	}
	static BigDecimal numericDer(Expression func, int timesDiff, String var, BigDecimal evalAt){ // ABSOLUTELY REQUIRE BIG DECIMAL
		int n = timesDiff;
		int m = 0; // evaluate in terms of mth derivs.

		BigDecimal x = evalAt;
		BigDecimal h = H_VALUE.pow(n-m); // scale with pow((n-m) * greatest exponent if applicable))
		BigDecimal sum = BigDecimal.ZERO;

		for (int i = 0; i < n - m + 1; ++i){
			sum = sum.add(new BigDecimal(Math.pow(-1, i)).multiply(new BigDecimal(binomial(n - m, i))).multiply(func.with(var, x.add(h.multiply(new BigDecimal(n).subtract(new BigDecimal(m)).subtract(new BigDecimal(i))))).eval()));
			System.out.println(sum);
		}

		sum = sum.divide(h.pow(n-m));
		return sum;
	}
	static BigDecimal nMethod(Expression func, int numIter, String var, BigDecimal a, BigDecimal b){ // Newton's Method
		BigDecimal x = findStartVal(func, var, a, b);
		BigDecimal fEval = func.with(var, x).eval();
		BigDecimal fDerEval = numericDer(func, var, x);
		System.out.println("\n" + fEval.toString() + " " + fDerEval.toString());

		while (numIter > 0){
			if (fDerEval.compareTo(BigDecimal.ZERO) == 0){
				BigDecimal h = H_VALUE;
				BigDecimal leftDer = numericDer(func, var, x.subtract(h));
				BigDecimal rightDer = numericDer(func, var, x.add(h));
				if (fEval.compareTo(BigDecimal.ZERO) < 0){
					if (leftDer.compareTo(BigDecimal.ZERO) < 0) x = x.subtract(new BigDecimal("0.01"));
					else if (rightDer.compareTo(BigDecimal.ZERO) > 0) x = x.add(new BigDecimal("0.01"));
				}
				else if (fEval.compareTo(BigDecimal.ZERO) > 0){
					if (leftDer.compareTo(BigDecimal.ZERO) > 0) x = x.subtract(new BigDecimal("0.01"));
					else if (rightDer.compareTo(BigDecimal.ZERO) < 0) x = x.add(new BigDecimal("0.01"));
				}
			}
			else x = x.subtract(fEval.divide(fDerEval, NEWTON_NUM_DECIMAL, RoundingMode.HALF_DOWN));
			fEval = func.with(var, x).eval();
			fDerEval = numericDer(func, var, x);
			--numIter;
		}
		return x;
	}
	private static BigDecimal findStartVal(Expression func, String var, BigDecimal a, BigDecimal b){ // computational effect could be simulated by more nMethod iterations in some cases, but not all
		if (a.compareTo(b) > 0){
			BigDecimal c = a;
			a = b;
			b = c;
		}
		else if (a.compareTo(b) == 0){
			return a;
		}
		BigDecimal delta = new BigDecimal("0.1"); // arbitrarily small, 0.1 is probably sufficient
		String sign = "";
		BigDecimal fEval = func.with(var, a).eval();
		if (fEval.compareTo(BigDecimal.ZERO) < 0) sign = "-";
		else if (fEval.compareTo(BigDecimal.ZERO) > 0) sign = "+";
		else return a;

		String signLoop = sign;

		while (a.compareTo(b) < 0 && sign.equals(signLoop)){ // there must be a zero in (a, b)
			a = a.add(delta);
			fEval = func.with(var, a).eval();
			if (fEval.compareTo(BigDecimal.ZERO) < 0) signLoop = "-";
			else if (fEval.compareTo(BigDecimal.ZERO) > 0) signLoop = "+";
			else return a;
		}
		return (a.multiply(new BigDecimal("2")).subtract(delta)).divide(new BigDecimal("2"));
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