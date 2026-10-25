package sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/** Сравнение среднего времени сортировки случайных массивов разного размера. */
public class PerformanceTester {

    private record Run(int numIters, int numElems) {}

    /** Таблица 1 из задания. */
    private static final List<Run> RUNS = List.of(
            new Run(1000, 100),
            new Run(1000, 1_000),
            new Run(100, 10_000),
            new Run(100, 100_000),
            new Run(10, 1_000_000));

    // shortcut: строка таблицы обрывается через 30 с, а большие размеры пропускаются — иначе O(n²)-сортировки
    // на 10⁵–10⁶ элементов идут часами; для полного прогона увеличьте лимит
    private static final long TIME_BUDGET_MS = 30_000;

    public void test(SortingAlgorithm... algs) {
        List<List<Double>> avgTimes = Arrays.stream(algs).map(PerformanceTester::measure).toList();
        printTable(algs, avgTimes);
    }

    /** Среднее время (мс) по строкам RUNS; после превышения лимита оставшиеся строки не замеряются. */
    private static List<Double> measure(SortingAlgorithm alg) {
        List<Double> avgTimes = new ArrayList<>();
        for (Run run : RUNS) {
            long deadline = System.currentTimeMillis() + TIME_BUDGET_MS;
            var stats = IntStream.range(0, run.numIters())
                    .takeWhile(i -> System.currentTimeMillis() < deadline)
                    .mapToLong(i -> timeSort(alg, run.numElems()))
                    .summaryStatistics();
            avgTimes.add(stats.getAverage());
            System.out.printf("%-14s %,10d элем.: %4d/%-4d итераций, среднее %10.3f мс%n",
                    alg, run.numElems(), stats.getCount(), run.numIters(), stats.getAverage());
            if (stats.getCount() < run.numIters()) {
                System.out.printf("%-14s превышен лимит %d с, большие размеры пропущены%n", alg, TIME_BUDGET_MS / 1000);
                break;
            }
        }
        return avgTimes;
    }

    private static long timeSort(SortingAlgorithm alg, int numElems) {
        double[] arr = createNewRandomDoubleArray(numElems);
        long start = System.currentTimeMillis();
        alg.sortAsc(arr);
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static double[] createNewRandomDoubleArray(int numElems) {
        return ThreadLocalRandom.current().doubles(numElems).toArray();
    }

    private static void printTable(SortingAlgorithm[] algs, List<List<Double>> avgTimes) {
        StringBuilder header = new StringBuilder("%12s |".formatted("Кол-во элем."));
        for (SortingAlgorithm alg : algs) {
            header.append(" %14s |".formatted(alg));
        }
        System.out.println();
        System.out.println("Время выполнения, мс");
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        for (int r = 0; r < RUNS.size(); r++) {
            System.out.printf("%12d |", RUNS.get(r).numElems());
            for (List<Double> times : avgTimes) {
                System.out.print(r < times.size() ? " %14.3f |".formatted(times.get(r)) : " %14s |".formatted("—"));
            }
            System.out.println();
        }
    }
}
