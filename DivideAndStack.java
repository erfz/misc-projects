import java.util.Arrays;

public class DivideAndStack {

    public static void main(String[] args) {
        int[] a = stack(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }, 1);
        System.out.println(Arrays.toString(a));
    }

    public static int[] stack(int[] arr, int stackDepth) {
        if (stackDepth == arr.length)
            return arr;

        int a1[] = Arrays.copyOfRange(arr, 0, arr.length / 2);
        int a2[] = Arrays.copyOfRange(arr, arr.length / 2, arr.length);

        for (int i = 0; i < arr.length / 2 / stackDepth; ++i) {
            for (int j = 0; j < stackDepth; ++j) {
                arr[2 * i * stackDepth + j] = a1[i * stackDepth + j];
                arr[2 * i * stackDepth + stackDepth + j] = a2[i * stackDepth + j];
            }
        }

        return stack(arr, stackDepth * 2);
    }
}