package sorting;

public class QuickSort extends SortingAlgorithm {

    @Override
    public void sortAsc(double[] values) {
        sort(values, 0, values.length - 1);
    }

    @Override
    public void sortDesc(double[] values) {
        sortAsc(values);
        reverse(values);
    }

    private static void sort(double[] a, int lo, int hi) {
        while (lo < hi) {
            double pivot = a[(lo + hi) >>> 1];
            int i = lo, j = hi;
            while (i <= j) {
                while (a[i] < pivot) i++;
                while (a[j] > pivot) j--;
                if (i <= j) swap(a, i++, j--);
            }
            // рекурсия в меньшую часть, цикл по большей — глубина стека O(log n)
            if (j - lo < hi - i) {
                sort(a, lo, j);
                lo = i;
            } else {
                sort(a, i, hi);
                hi = j;
            }
        }
    }
}
