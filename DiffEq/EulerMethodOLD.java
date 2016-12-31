public class EulerMethodOLD{
	public static void main(String[] args){
		double t = 0; // initial condition
		double x = 1; // x(t_0) = x_0
		
		double dt = 0.000000001; // step size
		double totaldt = .9; // total change in t -- final t = t_0 + totaldt
		
		double i = 0; // variable of iteration
		while (i < totaldt/dt){ // totaldt/dt iterations in total
			x += x*t*dt; // x = x + dx = x + dt(dx/dt) -- here dt is finite so in actuality this is approximate
			t += dt; // iterate to next t value
			++i;
		}
		System.out.println(x);
	}
}

// Euler's Method
