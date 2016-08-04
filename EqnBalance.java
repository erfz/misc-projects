import java.util.Arrays;
import java.util.Scanner;

public class EqnBalance { // put everything into a neat class
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the chemical equation:");
        String eqnStr = input.nextLine();
        String[] eqnHS = eqnStr.replaceAll("\\s+","").split("=|(-+\\>)|(\\<-+)");
        String[] eqnLHSplit = eqnHS[0].split("\\++");
        String[] eqnRHSplit = eqnHS[1].split("\\++");

        for (int i = 0; i < eqnLHSplit.length; ++i){ // nuke user-entered coefficients
            eqnLHSplit[i] = eqnLHSplit[i].replaceAll("(\\++|^)\\d*","");
        }

        for (int i = 0; i < eqnRHSplit.length; ++i){
            eqnRHSplit[i] = eqnRHSplit[i].replaceAll("(\\++|^)\\d*","");
        }

        String[] eqnLHEleNum = eqnHS[0].replaceAll("(\\++|^)\\d*","").split("(?=\\p{Upper})"); // only useful for printing?
        String[] eqnRHEleNum = eqnHS[1].replaceAll("(\\++|^)\\d*","").split("(?=\\p{Upper})");

        String[] eqnLHEle = new String[eqnLHEleNum.length];
        String[] eqnRHEle = new String[eqnRHEleNum.length];

        for (int i = 0; i < eqnLHEleNum.length; ++i){
            eqnLHEle[i] = eqnLHEleNum[i].replaceAll("[0-9]","");
        }
        for (int i = 0; i < eqnRHEleNum.length; ++i){
            eqnRHEle[i] = eqnRHEleNum[i].replaceAll("[0-9]","");
        }

        // System.out.println(Arrays.toString(eqnLHEle) + eqnLHEle.length + "\n" + Arrays.toString(eqnRHEle) + eqnRHEle.length);            // debug

        int numElements = 0;

        { // find actual # of elements
            int counter = 0;
            for (int i = 0; i < eqnLHEle.length - 1; ++i){
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnLHEle.length; ++j){
                    if (eqnLHEle[i].equals(eqnLHEle[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numElementsL = eqnLHEle.length - counter;

            counter = 0;
            for (int i = 0; i < eqnRHEle.length - 1; ++i){
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnRHEle.length; ++j){
                    if (eqnRHEle[i].equals(eqnRHEle[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numElementsR = eqnRHEle.length - counter;

            if (numElementsL == numElementsR) numElements = numElementsL;
            else throw new IllegalArgumentException("Your equation is invalid.");
        }
        
        String[] eqnElements = new String[numElements];
        {
            int arrayLength = 0;
            int count = 0;
            String[] tempArray = null;
            if (eqnLHEle.length <= eqnRHEle.length) tempArray = eqnLHEle;
            else tempArray = eqnRHEle;

            System.out.println(Arrays.deepToString(tempArray));
            for (int i = 0; i < tempArray.length - 1; ++i){
                boolean eleBoolean = true;
                for (int j = i + 1; j < tempArray.length; ++j){
                    if (tempArray[i].equals(tempArray[j])) eleBoolean = false;
                }
                if (eleBoolean){
                    eqnElements[count] = tempArray[i];
                    ++count;
                }
            }
            eqnElements[eqnElements.length - 1] = tempArray[tempArray.length - 1];
        }
        // System.out.println(Arrays.deepToString(eqnElements));                                                                                // debug
        
        // System.out.println(Arrays.toString(eqnLHEle) + eqnLHEle.length + "\n" + Arrays.toString(eqnRHEle) + eqnRHEle.length);
        // System.out.println(Arrays.toString(eqnLHEleNum) + eqnLHEleNum.length + "\n" + Arrays.toString(eqnRHEleNum) + eqnRHEleNum.length);
        // System.out.println(Arrays.toString(eqnLHSplit) + eqnLHSplit.length + "\n" + Arrays.toString(eqnRHSplit) + eqnRHSplit.length);

        double[][] eqnSolnSys = new double[numElements][eqnLHSplit.length + eqnRHSplit.length + 1];

        for (int i = 0; i < numElements; ++i){
            for (int j = 0; j < eqnLHSplit.length; ++j){
                int lastEleIndex = 0;
                while (eqnLHSplit[j].contains(eqnElements[i]) && eqnLHSplit[j].indexOf(eqnElements[i], lastEleIndex) != -1){
                    lastEleIndex = eqnLHSplit[j].indexOf(eqnElements[i], lastEleIndex) + eqnElements[i].length();
                    // System.out.println(lastEleIndex);
                    String temp = eqnLHSplit[j].substring(lastEleIndex, eqnLHSplit[j].length()).replaceAll("\\p{Alpha}.*", "");
                    if (temp.isEmpty()) eqnSolnSys[i][j] += 1;
                    else eqnSolnSys[i][j] += Double.parseDouble(temp);
                }
            }
        }

        for (int i = 0; i < numElements; ++i){
            for (int j = eqnLHSplit.length; j < eqnLHSplit.length + eqnRHSplit.length; ++j){
                int lastEleIndex = 0;
                while (eqnRHSplit[j - eqnLHSplit.length].contains(eqnElements[i]) && eqnRHSplit[j - eqnLHSplit.length].indexOf(eqnElements[i], lastEleIndex) != -1){
                    lastEleIndex = eqnRHSplit[j - eqnLHSplit.length].indexOf(eqnElements[i], lastEleIndex) + eqnElements[i].length();
                    // System.out.println(lastEleIndex);
                    String temp = eqnRHSplit[j - eqnLHSplit.length].substring(lastEleIndex, eqnRHSplit[j - eqnLHSplit.length].length()).replaceAll("\\p{Alpha}.*", "");
                    if (temp.isEmpty()) eqnSolnSys[i][j] += 1;
                    else eqnSolnSys[i][j] += Double.parseDouble(temp);
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

        // Solve matrix

        int n = 0;
        for (int i = 0; i < eqnElements.length; ++i){
            if (eqnLHSplit[0].indexOf(eqnElements[i]) == 0) n = i;
        }

        eqnSolnSys[n][eqnSolnSys[0].length - 1] = -1 * eqnSolnSys[n][0]; // Let the first coefficient of the chemical equation itself = 1
        eqnSolnSys[n][0] = 0;

        System.out.println(Arrays.deepToString(eqnSolnSys));

        RREF.toRREF(eqnSolnSys);

        System.out.println(Arrays.deepToString(eqnSolnSys));

        // String[] chemEqn = new String[eqnLHSplit.length + eqnRHSplit.length];
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

/* future use

    double[] solnArray = new double[eqnSolnSys.length + 1];
    solnArray[0] = 1;

    boolean loopBool = false;
    while (!loopBool){
        double smallestVal = Double.MAX_VALUE;
        for (int i = 0; i < eqnSolnSys.length; ++i){
            if (eqnSolnSys[i][eqnSolnSys[0].length - 1] < smallestVal && eqnSolnSys[i][eqnSolnSys[0].length - 1] != 1) smallestVal = eqnSolnSys[i][eqnSolnSys[0].length - 1];
            solnArray[i + 1] = eqnSolnSys[i][eqnSolnSys[0].length - 1];
        }
        for (int i = 0; i < solnArray.length; ++i){
            if (Math.abs(solnArray[i] - Math.round(solnArray[i])) < 0.01) solnArray[i] = Math.floor(solnArray[i]);
            else{
                for (int j = 0; j < solnArray.length; ++j){
                    solnArray[j] /= smallestVal;
                    smallestVal = Double.MAX_VALUE;
                }
            }
            if (i + 1 == solnArray.length) loopBool = true;
        }
    }

    static int gcd(int... vals){
        int resultantSum = 0;
        for (int i = 0; i < vals.length - 1; ++i){
            while(vals[i]!=0 && vals[i+1]!=0)
            {
                int a = vals[i+1];
                vals[i+1] = vals[i]%vals[i+1];
                vals[i] = a;
            }
        }
        for (int result : vals) resultantSum += result;
        return resultantSum;
    }

// if(LHS.stripNumeric equals RHE.stripNumeric) --> for-loop ---> if(eleLHS.equals(eleRHS) coeff[i] = subscript[i]
