class BackupThread extends Thread {
    public void randomize(IntegerChromosome chrom, int min, int max) {
        int len = max - min + 1;
        int[] base = new int[len];
        for (int i = 0; i < len; i++) base[i] = chrom.getValue(min + i);
        for (int i = 0; len > 0; --len, ++i) {
            int pos = Random.getInstance().nextInt(0, len);
            chrom.setValue(min + i, base[pos]);
            for (int j = pos; j < (len - 1); j++) {
                base[j] = base[j + 1];
            }
        }
    }
}
