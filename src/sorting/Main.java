package sorting;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        SortingAlgorithm[] algs = {
                new BubbleSort(), new InsertionSort(), new SelectionSort(),
                new MergeSort(), new HeapSort(), new QuickSort(), new RadixSort()
        };
        selfCheck(algs);
        new PerformanceTester().test(algs);
    }

    /** Сверяет каждый алгоритм с Arrays.sort на граничных и случайных данных. */
    private static void selfCheck(SortingAlgorithm... algs) {
        List<double[]> inputs = List.of(
                new double[0],
                new double[] {42},
                new double[] {3, -1, 3, 0, -0.5, 3, Double.MAX_VALUE, -Double.MAX_VALUE},
                DoubleStream.iterate(0, x -> x + 1).limit(500).toArray(),
                ThreadLocalRandom.current().doubles(1000, -1e6, 1e6).toArray());
        for (SortingAlgorithm alg : algs) {
            for (double[] input : inputs) {
                double[] expected = input.clone();
                Arrays.sort(expected);
                double[] asc = input.clone();
                alg.sortAsc(asc);
                double[] desc = input.clone();
                alg.sortDesc(desc);
                if (!Arrays.equals(asc, expected) || !Arrays.equals(desc, reversed(expected))) {
                    throw new IllegalStateException(alg + " сортирует неверно: " + Arrays.toString(input));
                }
            }
        }
        System.out.println("Самопроверка пройдена: все алгоритмы сортируют корректно\n");
    }

    private static double[] reversed(double[] a) {
        return IntStream.range(0, a.length).mapToDouble(i -> a[a.length - 1 - i]).toArray();
    }
}
