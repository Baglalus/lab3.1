package sorting;

public class HeapSort extends SortingAlgorithm {

    @Override
    public void sortAsc(double[] values) {
        int n = values.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(values, i, n);
        }
        for (int end = n - 1; end > 0; end--) {
            swap(values, 0, end);
            siftDown(values, 0, end);
        }
    }

    @Override
    public void sortDesc(double[] values) {
        sortAsc(values);
        reverse(values);
    }

    private static void siftDown(double[] a, int i, int size) {
        for (int child = 2 * i + 1; child < size; i = child, child = 2 * i + 1) {
            if (child + 1 < size && a[child + 1] > a[child]) {
                child++;
            }
            if (a[i] >= a[child]) {
                return;
            }
            swap(a, i, child);
        }
    }
}
