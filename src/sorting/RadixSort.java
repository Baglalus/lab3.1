package sorting;

// Поразрядная LSD-сортировка по байтам 64-битного представления double. 
public class RadixSort extends SortingAlgorithm {

    @Override
    public void sortAsc(double[] values) {
        int n = values.length;
        long[] keys = new long[n];
        long[] buf = new long[n];
        for (int i = 0; i < n; i++) {
            keys[i] = toKey(values[i]);
        }
        for (int shift = 0; shift < Long.SIZE; shift += Byte.SIZE) {
            int[] start = new int[257];
            for (long k : keys) start[digit(k, shift) + 1]++;
            for (int d = 0; d < 256; d++) start[d + 1] += start[d];
            for (long k : keys) buf[start[digit(k, shift)]++] = k;
            long[] t = keys;
            keys = buf;
            buf = t;
        }
        for (int i = 0; i < n; i++) {
            values[i] = fromKey(keys[i]);
        }
    }

    @Override
    public void sortDesc(double[] values) {
        sortAsc(values);
        reverse(values);
    }

    private static int digit(long key, int shift) {
        return (int) (key >>> shift & 0xFF);
    }

    // у отрицательных инвертируем все биты, у положительных — знаковый: беззнаковый порядок ключей = числовой порядок
    private static long toKey(double d) {
        long bits = Double.doubleToRawLongBits(d);
        return bits ^ (bits >> 63 | Long.MIN_VALUE);
    }

    private static double fromKey(long key) {
        return Double.longBitsToDouble(key ^ (~key >> 63 | Long.MIN_VALUE));
    }
}
