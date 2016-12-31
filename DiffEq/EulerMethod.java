import java.util.Scanner;
import com.udojava.evalex.Expression;
import java.math.BigDecimal;
import java.math.MathContext;

public class EulerMethod{
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		System.out.println("This program considers a particular solution y(t) to some (first-order) ODE: dy/dt = f(y, t).\n");

		System.out.print("Enter the differential equation: dy/dt = ");
		String diffEqStr = input.nextLine();
		Expression diffEq = new Expression(diffEqStr, MathContext.DECIMAL64); // MathContext later

		System.out.print("Initial t value: ");
		BigDecimal t = new BigDecimal(input.next()); // initial condition

		System.out.print("Initial y(t): ");
		BigDecimal y = new BigDecimal(input.next()); // y(t_0) = y_0

		System.out.print("Final t value: ");
		BigDecimal totaldt = new BigDecimal(input.next()).subtract(t); // total change in t -- final t = t_0 + totaldt
		
		System.out.print("Step size: ");
		BigDecimal dt = new BigDecimal(input.next()); // step size

		System.out.print("Show each iteration? (y/n): ");
		String showIter = input.next();
		
		BigDecimal i = BigDecimal.ZERO; // variable of iteration
		while (i.compareTo(totaldt.divide(dt)) < 0){ // totaldt/dt iterations in total
			BigDecimal eval = diffEq.with("y", y).and("t", t).eval();
			y = y.add(eval.multiply(dt)); // y = y + dy = y + dt(dy/dt) -- here dt is finite so in actuality this is approximate
			t = t.add(dt); // iterate to next t value

			if (showIter.toLowerCase().equals("y")){
				System.out.println("y(" + t + ") = " + y);
			}

			i = i.add(BigDecimal.ONE);
		}
		if (!showIter.toLowerCase().equals("y")) System.out.println(y.toString());
	}
}

// Euler's Method