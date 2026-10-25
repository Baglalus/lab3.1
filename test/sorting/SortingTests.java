package sorting;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Black-box тесты по спецификации задания, без зависимостей.
 * Запуск: java -cp out sorting.SortingTests — код выхода 1, если что-то упало.
 */
public class SortingTests {

    private static final List<Supplier<SortingAlgorithm>> ALGORITHMS = List.of(
            BubbleSort::new, InsertionSort::new, SelectionSort::new,
            MergeSort::new, HeapSort::new, QuickSort::new, RadixSort::new);

    private static final Random RND = new Random(42);

    private static final Map<String, double[]> INPUTS = Map.ofEntries(
            Map.entry("пустой", new double[0]),
            Map.entry("один элемент", new double[] {7.5}),
            Map.entry("два элемента", new double[] {2, 1}),
            Map.entry("уже отсортирован", DoubleStream.iterate(0, x -> x + 0.5).limit(1000).toArray()),
            Map.entry("обратный порядок", DoubleStream.iterate(1000, x -> x - 1).limit(1000).toArray()),
            Map.entry("все равны", DoubleStream.generate(() -> 3.14).limit(500).toArray()),
            Map.entry("много дубликатов", RND.ints(2000, -5, 5).asDoubleStream().toArray()),
            Map.entry("отрицательные", RND.doubles(1000, -1e9, -1e-9).toArray()),
            Map.entry("крайние значения", new double[] {
                    Double.POSITIVE_INFINITY, -Double.MIN_VALUE, Double.MAX_VALUE, 1, Double.NEGATIVE_INFINITY,
                    Double.MIN_VALUE, -Double.MAX_VALUE, -1, 1e-300, -1e300}),
            Map.entry("случайные [0,1)", RND.doubles(5000).toArray()),
            Map.entry("случайные со знаком", RND.doubles(5000, -1e6, 1e6).toArray()),
            Map.entry("пилообразный", IntStream.range(0, 3000).mapToDouble(i -> i % 17 - 8).toArray()));

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        for (var algorithm : ALGORITHMS) {
            String name = algorithm.get().getClass().getSimpleName();
            INPUTS.forEach((caseName, input) -> {
                check(name + " sortAsc: " + caseName, () -> {
                    double[] actual = input.clone();
                    algorithm.get().sortAsc(actual);
                    assertArrayEquals(ascending(input), actual);
                });
                check(name + " sortDesc: " + caseName, () -> {
                    double[] actual = input.clone();
                    algorithm.get().sortDesc(actual);
                    assertArrayEquals(descending(input), actual);
                });
            });
            check(name + " повторное использование экземпляра", () -> {
                SortingAlgorithm alg = algorithm.get();
                for (int i = 0; i < 5; i++) {
                    double[] input = RND.doubles(300, -10, 10).toArray();
                    double[] actual = input.clone();
                    alg.sortAsc(actual);
                    assertArrayEquals(ascending(input), actual);
                }
            });
        }
        check("PerformanceTester.test выводит таблицу по всем размерам и алгоритмам", SortingTests::performanceTesterPrintsTable);

        System.out.printf("%nИтого: %d пройдено, %d упало%n", passed, failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    /** Подставляем мгновенную «сортировку», чтобы проверить сам тестер, а не скорость алгоритмов. */
    static class NoOpSort extends SortingAlgorithm {
        int calls;

        @Override
        public void sortAsc(double[] values) {
            calls++;
        }

        @Override
        public void sortDesc(double[] values) {
            calls++;
        }
    }

    private static void performanceTesterPrintsTable() {
        var noOp = new NoOpSort();
        var out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out, true));
        try {
            new PerformanceTester().test(noOp);
        } finally {
            System.setOut(original);
        }
        String printed = out.toString();

        // по таблице 1: 1000 + 1000 + 100 + 100 + 10 итераций
        assertTrue(noOp.calls == 2210, "ожидалось 2210 сортировок, было " + noOp.calls);
        assertTrue(printed.contains("NoOpSort"), "в выводе нет имени алгоритма");
        for (String size : List.of("100", "1000", "10000", "100000", "1000000")) {
            assertTrue(printed.lines().anyMatch(line -> line.trim().startsWith(size + " ")),
                    "в таблице нет строки для " + size + " элементов");
        }
    }

    private static double[] ascending(double[] input) {
        double[] expected = input.clone();
        Arrays.sort(expected);
        return expected;
    }

    private static double[] descending(double[] input) {
        double[] asc = ascending(input);
        return IntStream.range(0, asc.length).mapToDouble(i -> asc[asc.length - 1 - i]).toArray();
    }

    private static void check(String name, Runnable test) {
        try {
            test.run();
            passed++;
        } catch (Throwable e) {
            failed++;
            System.out.println("FAIL " + name + ": " + e);
        }
    }

    private static void assertArrayEquals(double[] expected, double[] actual) {
        if (!Arrays.equals(expected, actual)) {
            int at = Arrays.mismatch(expected, actual);
            throw new AssertionError("расхождение на индексе " + at + " (длина " + expected.length + " vs " + actual.length + ")");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
