package sorting;

public class Main {

    public static void main(String[] args) {
        SortingAlgorithm[] algs = {
                new BubbleSort(), new InsertionSort(), new SelectionSort(),
                new MergeSort(), new HeapSort(), new QuickSort(), new RadixSort()
        };
        new PerformanceTester().test(algs);
    }
}
