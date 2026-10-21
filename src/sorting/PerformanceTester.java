package sorting;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
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
        List<double[]> averages = Arrays.stream(algs).map(PerformanceTester::measure).toList();
        printTable(algs, averages);
    }

    /** Среднее время (мс) для каждой строки RUNS; NaN — строка пропущена. */
    private static double[] measure(SortingAlgorithm alg) {
        double[] avg = new double[RUNS.size()];
        Arrays.fill(avg, Double.NaN);
        for (int r = 0; r < RUNS.size(); r++) {
            Run run = RUNS.get(r);
            long deadline = System.currentTimeMillis() + TIME_BUDGET_MS;
            var stats = IntStream.range(0, run.numIters())
                    .takeWhile(i -> System.currentTimeMillis() < deadline)
                    .mapToLong(i -> timeSort(alg, run.numElems()))
                    .summaryStatistics();
            avg[r] = stats.getAverage();
            System.out.printf("%-14s %,10d элем.: %4d/%-4d итераций, среднее %10.3f мс%n",
                    alg, run.numElems(), stats.getCount(), run.numIters(), avg[r]);
            if (stats.getCount() < run.numIters()) {
                System.out.printf("%-14s превышен лимит %d с, бóльшие размеры пропущены%n", alg, TIME_BUDGET_MS / 1000);
                break;
            }
        }
        return avg;
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

    private static void printTable(SortingAlgorithm[] algs, List<double[]> averages) {
        String header = Arrays.stream(algs)
                .map("%14s"::formatted)
                .collect(Collectors.joining(" | ", "%12s | ".formatted("Кол-во элем."), ""));
        System.out.println();
        System.out.println("Время выполнения, мс");
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        IntStream.range(0, RUNS.size())
                .mapToObj(r -> averages.stream()
                        .map(avg -> Double.isNaN(avg[r]) ? "%14s".formatted("—") : "%14.3f".formatted(avg[r]))
                        .collect(Collectors.joining(" | ", "%12d | ".formatted(RUNS.get(r).numElems()), "")))
                .forEach(System.out::println);
    }
}
