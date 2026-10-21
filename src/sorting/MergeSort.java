package sorting;

public class MergeSort extends SortingAlgorithm {

    @Override
    public void sortAsc(double[] values) {
        sort(values, new double[values.length], 0, values.length);
    }

    @Override
    public void sortDesc(double[] values) {
        sortAsc(values);
        reverse(values);
    }

    private static void sort(double[] a, double[] buf, int from, int to) {
        if (to - from < 2) {
            return;
        }
        int mid = (from + to) >>> 1;
        sort(a, buf, from, mid);
        sort(a, buf, mid, to);
        System.arraycopy(a, from, buf, from, to - from);
        for (int i = from, j = mid, k = from; k < to; k++) {
            a[k] = j >= to || (i < mid && buf[i] <= buf[j]) ? buf[i++] : buf[j++];
        }
    }
}
