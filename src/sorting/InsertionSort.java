package sorting;

public class InsertionSort extends SortingAlgorithm {

    @Override
    public void sortAsc(double[] values) {
        for (int i = 1; i < values.length; i++) {
            double x = values[i];
            int j = i - 1;
            while (j >= 0 && values[j] > x) {
                values[j + 1] = values[j];
                j--;
            }
            values[j + 1] = x;
        }
    }

    @Override
    public void sortDesc(double[] values) {
        sortAsc(values);
        reverse(values);
    }
}
