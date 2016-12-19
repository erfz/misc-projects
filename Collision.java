import java.util.Scanner;
import java.util.Arrays;

public class Collision{
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		System.out.println("How many objects are colliding?");
		while (!input.hasNextInt()){
			System.out.println("This is not a valid (integer) number of objects.");
			input.nextLine();
		}
		int numObj = input.nextInt(); // gives number of rows of matrix
		input.nextLine();

		double[][] matrix = new double[2][numObj + 1]; // deal with massless particles later
		for (int i = 0; i < numObj; ++i){
			System.out.println();
			System.out.println("Enter the mass of Object " + (i+1) + ":");
			while (!input.hasNextDouble()){
				System.out.println("This is not a valid (double) mass. Try again.");
				input.nextLine();
			}
			matrix[0][i] += input.nextDouble();
			matrix[1][i] += matrix[0][i];
			input.nextLine();
		}

		for (int i = 0; i < numObj; ++i){
			System.out.println();
			System.out.println("Enter the initial velocity of Object " + (i+1) + ":");
			while (!input.hasNextDouble()){
				System.out.println("This is not a valid (double) velocity. Try again.");
				input.nextLine();
			}
			double vel = input.nextDouble();
			System.out.println("Enter the initial angle of Object " + (i+1) + " in degrees:");
			while(!input.hasNextDouble()){
				System.out.println("This is not a valid (double) angle. Try again.");
				input.nextLine();
			}
			double angle = input.nextDouble();
			matrix[0][numObj] += matrix[0][i]*vel*Math.cos(Math.toRadians(angle));
			matrix[1][numObj] += matrix[0][i]*vel*Math.sin(Math.toRadians(angle));
			input.nextLine();
		}

		for(int i = 0; i < numObj; ++i){ // account for unknown angles
			System.out.println();
			System.out.println("Enter the final angle of Object " + (i+1) + " in degrees:");
			while (!input.hasNextDouble()){
				System.out.println("This is not a valid (double) angle. Try again.");
				input.nextLine();
			}
			double angle = input.nextDouble();
			matrix[0][i] *= Math.cos(Math.toRadians(angle));
			matrix[1][i] *= Math.sin(Math.toRadians(angle));
			input.nextLine();
		}

		int numVarReq = numObj - 2; // assumes all angles are known
		for (int i = 0; i < numVarReq; ++i){
			System.out.println();
			System.out.println("Which object's velocity do you know? (" + numVarReq + "needed):");
			while (!input.hasNextInt()){
				System.out.println("This is not a valid (int) object. Try again");
				input.nextLine();
			}
			int index = input.nextInt(); // check that this is valid
			input.nextLine();
			System.out.println("Enter the velocity of Object " + index + ":");
			while (!input.hasNextDouble()){
				System.out.println("This is not a valid (double) velocity. Try again.");
				input.nextLine();
			}
			double vel = input.nextDouble();
			input.nextLine();

			matrix[0][index - 1] *= vel;
			matrix[1][index - 1] *= vel;
			matrix[0][numObj] -= matrix[0][index - 1];
			matrix[1][numObj] -= matrix[1][index - 1];
			matrix[0][index - 1] = 0;
			matrix[1][index - 1] = 0;
		}
		System.out.println(Arrays.deepToString(matrix));
		toRREF(matrix);
		System.out.println(Arrays.deepToString(matrix));
	}

	static void toRREF(double[][] M) {
        int rowCount = M.length;
        if (rowCount == 0)
            return;

        int columnCount = M[0].length;

        int lead = 0;
        for (int r = 0; r < rowCount; r++) {
            if (lead >= columnCount)
                break;
            {
                int i = r;
                while (Math.round(M[i][lead]) == 0 && Math.abs(M[i][lead] - Math.round(M[i][lead])) < 0.0001) { // account for double precision -- originally the argument was M[i][lead] == 0
                    i++;
                    if (i == rowCount) {
                        i = r;
                        lead++;
                        if (lead == columnCount)
                            return;
                    }
                }
                double[] temp = M[r];
                M[r] = M[i];
                M[i] = temp;
            }

            {
                double lv = M[r][lead];
                for (int j = 0; j < columnCount; j++)
                    M[r][j] /= lv;
            }

            for (int i = 0; i < rowCount; i++) {
                if (i != r) {
                    double lv = M[i][lead];
                    for (int j = 0; j < columnCount; j++)
                        M[i][j] -= lv * M[r][j];
                }
            }
            lead++;
        }
    }
}