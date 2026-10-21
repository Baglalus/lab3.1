package sorting;

public class BubbleSort extends SortingAlgorithm {

    @Override
    public void sortAsc(double[] values) {
        for (int end = values.length - 1; end > 0; end--) {
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                if (values[i] > values[i + 1]) {
                    swap(values, i, i + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                return;
            }
        }
    }

    @Override
    public void sortDesc(double[] values) {
        sortAsc(values);
        reverse(values);
    }
}
