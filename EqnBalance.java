import java.util.Arrays;
import java.util.Scanner;

public class EqnBalance { // put everything into a neat class
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the chemical equation:");
        String eqnStr = input.nextLine();
        String[] eqnHS = eqnStr.split("=|(-+\\>)|(\\<-+)");
        String[] eqnLHSplit = eqnHS[0].trim().split("\\s*\\+\\s*");
        String[] eqnRHSplit = eqnHS[1].trim().split("\\s*\\+\\s*");
        String[] eqnLHEleNum = eqnHS[0].replaceAll("[^A-Za-z0-9]","").split("(?=\\p{Upper})");
        String[] eqnRHEleNum = eqnHS[1].replaceAll("[^A-Za-z0-9]","").split("(?=\\p{Upper})");
        String[] eqnLHEle = new String[eqnLHEleNum.length];
        String[] eqnRHEle = new String[eqnRHEleNum.length];

        for (int i = 0; i < eqnLHEleNum.length; ++i){
            eqnLHEle[i] = eqnLHEleNum[i].replaceAll("[0-9]","");
        }
        for (int i = 0; i < eqnRHEleNum.length; ++i){
            eqnRHEle[i] = eqnRHEleNum[i].replaceAll("[0-9]","");
        }

        for (int i = 0; i < eqnLHEle.length; ++i){
            for (int j = 0; j < eqnRHEle.length; ++j){
                if (eqnLHEle[i].equals(eqnRHEle[j]) && i != j){
                    String temp = eqnRHEle[i];
                    eqnRHEle[i] = eqnRHEle[j];
                    eqnRHEle[j] = temp;

                    temp = eqnRHEleNum[i];
                    eqnRHEleNum[i] = eqnRHEleNum[j];
                    eqnRHEleNum[j] = temp;
                }
            }
        }

        // System.out.println(Arrays.toString(eqnLHEle) + "\n" + Arrays.toString(eqnRHEle));
        // System.out.println(Arrays.toString(eqnLHEleNum) + "\n" + Arrays.toString(eqnRHEleNum));
        // System.out.println(Arrays.toString(eqnLHSplit) + "\n" + Arrays.toString(eqnRHSplit));

        double[][] eqnSolnSys = new double[eqnLHEle.length][eqnLHSplit.length + eqnRHSplit.length + 1];

        for (int i = 0; i < eqnLHEle.length; ++i){
            for (int j = 0; j < eqnLHSplit.length; ++j){
                if(eqnLHSplit[j].contains(eqnLHEle[i])) {
                    if (eqnLHEle[i].equals(eqnLHEleNum[i])) eqnSolnSys[i][j] = 1;
                    else eqnSolnSys[i][j] = Double.parseDouble(eqnLHEleNum[i].replaceAll("[A-Za-z]",""));
                }
            }
        }
        for (int i = 0; i < eqnRHEle.length; ++i){
            for (int j = 0; j < eqnRHSplit.length; ++j){
                if(eqnRHSplit[j].contains(eqnRHEle[i])) {
                    if (eqnRHEle[i].equals(eqnRHEleNum[i])) eqnSolnSys[i][j + eqnLHSplit.length] = 1;
                    else eqnSolnSys[i][j + eqnLHSplit.length] = Double.parseDouble(eqnRHEleNum[i].replaceAll("[A-Za-z]",""));
                }
            }
        }

        for (int i = 0; i < eqnSolnSys.length; ++i){
            for (int j = eqnLHSplit.length; j < eqnSolnSys[0].length; ++j){
                if (eqnSolnSys[i][j] != 0){
                    eqnSolnSys[i][j] *= -1;
                }
            }
        }

        // System.out.println(Arrays.deepToString(eqnSolnSys));

        eqnSolnSys[0][eqnSolnSys[0].length - 1] = -1 * eqnSolnSys[0][0]; // Let the first coefficient of the chemical equation itself = 1
        eqnSolnSys[0][0] = 0;

        RREF.toRREF(eqnSolnSys);

        // System.out.println(Arrays.deepToString(eqnSolnSys));

        String[] chemEqn = new String[eqnLHSplit.length + eqnRHSplit.length];
        chemEqn[0] = eqnLHSplit[0].concat(" + ");

        for (int i = 1; i < eqnLHSplit.length - 1; ++i){
            chemEqn[i] = Double.toString(eqnSolnSys[i-1][eqnLHSplit.length + eqnRHSplit.length]).concat(eqnLHSplit[i]).concat(" + ");
        }
        chemEqn[eqnLHSplit.length - 1] = Double.toString(eqnSolnSys[eqnLHSplit.length - 1][eqnLHSplit.length + eqnRHSplit.length]).concat(eqnLHSplit[eqnLHSplit.length - 1]).concat(" --> ");
        
        for (int i = eqnLHSplit.length; i < eqnLHSplit.length + eqnRHSplit.length - 1; ++i){
            chemEqn[i] = Double.toString(eqnSolnSys[i-1][eqnLHSplit.length + eqnRHSplit.length]).concat(eqnRHSplit[i - eqnLHSplit.length]).concat(" + ");
        }
        chemEqn[chemEqn.length - 1] = Double.toString(eqnSolnSys[eqnSolnSys.length - 1][eqnSolnSys[0].length - 1]).concat(eqnRHSplit[eqnRHSplit.length - 1]);

        System.out.println();
        for (String str : chemEqn) System.out.print(str);
        System.out.println();

    }
}

class RREF {

    public static void toRREF(double[][] M) {
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
                while (M[i][lead] == 0) {
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