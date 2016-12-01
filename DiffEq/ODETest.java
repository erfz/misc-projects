public class ODETest{
	public static void main(String[] args){
		double x = 1;
		double dt = 0.000000001;
		double totaldt = .9;
		double t = 0;
		double i = 0;
		while (i < totaldt/dt){
			x += x*t*dt;
			t += dt;
			++i;
		}
		System.out.println(x);
	}
}

// holy fuck this works...