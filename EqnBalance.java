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

        for (int i = 0; i < eqnLHSplit.length; ++i){
            if (eqnLHSplit[i].contains("(") && eqnLHSplit[i].contains(")")){
                String[] splitParts = eqnLHSplit[i].split("(\\(+|\\)+)");
                for (int j = 0; j < splitParts.length; ++j){
                    if (eqnLHSplit[i].contains("(" + splitParts[j] + ")")){
                        String compoundString = "";
                        int parenNumber = 0;
                        if (splitParts[j + 1].replaceAll("\\p{Alpha}.*", "").isEmpty()) parenNumber = 1;
                        else parenNumber = Integer.parseInt(splitParts[j + 1].replaceAll("\\p{Alpha}.*", ""));
                        String[] compoundElements = splitParts[j].split("(?=\\p{Upper})");
                        int[] elementNumber = new int[compoundElements.length];
                        for (int k = 0; k < elementNumber.length; ++k){
                            if (compoundElements[k].replaceAll("\\p{Alpha}", "").isEmpty()) elementNumber[k] = parenNumber;
                            else elementNumber[k] = Integer.parseInt(compoundElements[k].replaceAll("\\p{Alpha}", "")) * parenNumber;
                            compoundString = compoundString.concat(compoundElements[k].replaceAll("\\d*", "").concat(Integer.toString(elementNumber[k])));
                        }
                        splitParts[j] = compoundString;
                        splitParts[j + 1] = splitParts[j + 1].replaceAll("^\\d*", "");
                    }
                }
                String finalString = "";
                for (String part : splitParts) finalString = finalString.concat(part);
                eqnLHSplit[i] = finalString;
            }
        }

        for (int i = 0; i < eqnRHSplit.length; ++i){
            if (eqnRHSplit[i].contains("(") && eqnRHSplit[i].contains(")")){
                String[] splitParts = eqnRHSplit[i].split("(\\(+|\\)+)");
                for (int j = 0; j < splitParts.length; ++j){
                    if (eqnRHSplit[i].contains("(" + splitParts[j] + ")")){
                        String compoundString = "";
                        int parenNumber = 0;
                        if (splitParts[j + 1].replaceAll("\\p{Alpha}.*", "").isEmpty()) parenNumber = 1;
                        else parenNumber = Integer.parseInt(splitParts[j + 1].replaceAll("\\p{Alpha}.*", ""));
                        String[] compoundElements = splitParts[j].split("(?=\\p{Upper})");
                        int[] elementNumber = new int[compoundElements.length];
                        for (int k = 0; k < elementNumber.length; ++k){
                            if (compoundElements[k].replaceAll("\\p{Alpha}", "").isEmpty()) elementNumber[k] = parenNumber;
                            else elementNumber[k] = Integer.parseInt(compoundElements[k].replaceAll("\\p{Alpha}", "")) * parenNumber;
                            compoundString = compoundString.concat(compoundElements[k].replaceAll("\\d*", "").concat(Integer.toString(elementNumber[k])));
                        }
                        splitParts[j] = compoundString;
                        splitParts[j + 1] = splitParts[j + 1].replaceAll("^\\d*", "");
                    }
                }
                String finalString = "";
                for (String part : splitParts) finalString = finalString.concat(part);
                eqnRHSplit[i] = finalString;
            }
        }

        String[] eqnLHEleNum = eqnHS[0].replaceAll("(\\++|^)\\d*","").replaceAll("(\\(|\\))", "").split("(?=\\p{Upper})"); // obsolete
        String[] eqnRHEleNum = eqnHS[1].replaceAll("(\\++|^)\\d*","").replaceAll("(\\(|\\))", "").split("(?=\\p{Upper})");

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

        int numMolecules = 0;
        int matrixNegIndex = 0;
        { // find actual # of molecules
            int counter = 0;
            for (int i = 0; i < eqnLHSplit.length - 1; ++i){
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnLHSplit.length; ++j){
                    if (eqnLHSplit[i].equals(eqnLHSplit[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numMoleculesL = eqnLHSplit.length - counter;
            matrixNegIndex = numMoleculesL;

            counter = 0;
            for (int i = 0; i < eqnRHSplit.length - 1; ++i){
                boolean eleBoolean = false;
                for (int j = i + 1; j < eqnRHSplit.length; ++j){
                    if (eqnRHSplit[i].equals(eqnRHSplit[j])) eleBoolean = true;
                }
                if (eleBoolean) ++counter;
            }
            int numMoleculesR = eqnRHSplit.length - counter;

            numMolecules = numMoleculesL + numMoleculesR;
        }

        String[] eqnMolecules = new String[numMolecules];
        {
            int count = 0;

            for (int i = 0; i < eqnLHSplit.length - 1; ++i){
                boolean eleBoolean = true;
                for (int j = i + 1; j < eqnLHSplit.length; ++j){
                    if (eqnLHSplit[i].equals(eqnLHSplit[j])) eleBoolean = false;
                }
                if (eleBoolean){
                    eqnMolecules[count] = eqnLHSplit[i];
                    ++count;
                }
            }
            ++count;
            eqnMolecules[count - 1] = eqnLHSplit[eqnLHSplit.length - 1];

            for (int i = 0; i < eqnRHSplit.length - 1; ++i){
                boolean eleBoolean = true;
                for (int j = i + 1; j < eqnRHSplit.length; ++j){
                    if (eqnRHSplit[i].equals(eqnRHSplit[j])) eleBoolean = false;
                }
                if (eleBoolean){
                    eqnMolecules[count] = eqnRHSplit[i];
                    ++count;
                }
            }
            eqnMolecules[eqnMolecules.length - 1] = eqnRHSplit[eqnRHSplit.length - 1];
        }

        System.out.println("Array of elements: " + Arrays.deepToString(eqnElements));
        System.out.println("Array of molecules: " + Arrays.deepToString(eqnMolecules));
        
        // System.out.println(Arrays.toString(eqnLHEle) + eqnLHEle.length + "\n" + Arrays.toString(eqnRHEle) + eqnRHEle.length);
        // System.out.println(Arrays.toString(eqnLHEleNum) + eqnLHEleNum.length + "\n" + Arrays.toString(eqnRHEleNum) + eqnRHEleNum.length);
        // System.out.println(Arrays.toString(eqnLHSplit) + eqnLHSplit.length + "\n" + Arrays.toString(eqnRHSplit) + eqnRHSplit.length);

        double[][] eqnSolnSys = new double[numElements][numMolecules + 1];

        for (int i = 0; i < numElements; ++i){
            for (int j = 0; j < numMolecules; ++j){
                int lastEleIndex = 0;
                while (eqnMolecules[j].contains(eqnElements[i]) && eqnMolecules[j].indexOf(eqnElements[i], lastEleIndex) != -1){
                    lastEleIndex = eqnMolecules[j].indexOf(eqnElements[i], lastEleIndex) + eqnElements[i].length();
                    // System.out.println(lastEleIndex);
                    String temp = eqnMolecules[j].substring(lastEleIndex, eqnMolecules[j].length()).replaceAll("\\p{Alpha}.*", "");
                    if (temp.isEmpty()) eqnSolnSys[i][j] += 1;
                    else eqnSolnSys[i][j] += Double.parseDouble(temp);
                }
            }
        }

        for (int i = 0; i < eqnSolnSys.length; ++i){
            for (int j = matrixNegIndex; j < eqnSolnSys[0].length; ++j){
                if (eqnSolnSys[i][j] != 0){
                    eqnSolnSys[i][j] *= -1;
                }
            }
        }

        // Solve matrix

        int[] firstMoleculeElementIndex = null;
        {
            String[] firstMoleculeElements = eqnLHSplit[0].split("(?=\\p{Upper})");
            for (int i = 0; i < firstMoleculeElements.length; ++i) firstMoleculeElements[i] = firstMoleculeElements[i].replaceAll("\\d", "");

            // int counter = 0;
            // for (int i = 0; i < firstMoleculeElements.length - 1; ++i){
            //     boolean eleBoolean = false;
            //     for (int j = i + 1; j < firstMoleculeElements.length; ++j){
            //         if (firstMoleculeElements[i].equals(firstMoleculeElements[j])) eleBoolean = true;
            //     }
            //     if (eleBoolean) ++counter;
            // }
            firstMoleculeElementIndex = new int[firstMoleculeElements.length];

            for (int i = 0; i < firstMoleculeElements.length; ++i){
                for (int j = 0; j < eqnElements.length; ++j){
                    if (firstMoleculeElements[i].equals(eqnElements[j])) firstMoleculeElementIndex[i] = j;
                }
            }
        }

        for (int i = 0; i < firstMoleculeElementIndex.length; ++i){
            eqnSolnSys[firstMoleculeElementIndex[i]][eqnSolnSys[0].length - 1] += -1 * eqnSolnSys[firstMoleculeElementIndex[i]][0]; // Let the first coefficient of the chemical equation itself = 1
            eqnSolnSys[firstMoleculeElementIndex[i]][0] = 0;
        }

        System.out.println(Arrays.deepToString(eqnSolnSys));

        toRREF(eqnSolnSys);

        System.out.println(Arrays.deepToString(eqnSolnSys));

        int[] coeffArray = new int[numMolecules];
        {
            double[] doubleCoeffs = new double[numMolecules];
            doubleCoeffs[0] = 1;
            boolean loopBool = true;
            for (int i = 1; i < numMolecules; ++i) doubleCoeffs[i] = eqnSolnSys[i - 1][eqnSolnSys[0].length - 1];
            while (loopBool){
                int count = 0;
                for (int i = 0; i < numMolecules; ++i){
                    double smallestDecFracPart = 0;
                    if (!(Math.abs(doubleCoeffs[i] - Math.round(doubleCoeffs[i])) < 0.0001)){
                        {
                            double smallestDecVal = Double.MAX_VALUE;
                            for (int k = 0; k < numMolecules; ++k) if (doubleCoeffs[k] < smallestDecVal && !(Math.abs(doubleCoeffs[k] - Math.round(doubleCoeffs[k])) < 0.0001)) smallestDecVal = doubleCoeffs[k];
                            smallestDecFracPart = smallestDecVal % 1;
                        }
                        for (int j = 0; j < numMolecules; ++j) doubleCoeffs[j] /= smallestDecFracPart;
                        ++count;
                        break;
                    }
                }
                if (numMolecules - count == numMolecules) loopBool = false;
            }
            for (int i = 0; i < numMolecules; ++i) coeffArray[i] = (int)Math.round(doubleCoeffs[i]);
        }
        System.out.println("\n" + Arrays.toString(coeffArray));
    }

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

// if(LHS.stripNumeric equals RHE.stripNumeric) --> for-loop ---> if(eleLHS.equals(eleRHS) coeff[i] = subscript[i]
// more intelligent control of matrix column # ... collapse like terms (eleNum[index] are equal)
