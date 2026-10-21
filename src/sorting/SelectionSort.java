package sorting;

public class SelectionSort extends SortingAlgorithm {

    @Override
    public void sortAsc(double[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < values.length; j++) {
                if (values[j] < values[min]) {
                    min = j;
                }
            }
            swap(values, i, min);
        }
    }

    @Override
    public void sortDesc(double[] values) {
        sortAsc(values);
        reverse(values);
    }
}
