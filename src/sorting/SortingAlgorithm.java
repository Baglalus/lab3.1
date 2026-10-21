package sorting;

/** Базовый класс алгоритмов сортировки массивов double. */
public abstract class SortingAlgorithm {

    public abstract void sortAsc(double[] values);

    public abstract void sortDesc(double[] values);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    protected static void swap(double[] a, int i, int j) {
        double t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    protected static void reverse(double[] a) {
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            swap(a, i, j);
        }
    }
}
