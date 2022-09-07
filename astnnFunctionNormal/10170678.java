class BackupThread extends Thread {
    public void endUpdate() {
        int i = 0;
        int total = 0;
        for (; i < counts.length - 1; i++) {
            counts[i] = counts[i + 1];
        }
        counts[i] = updateCount;
        updateCount = 0;
        int num = rects.size();
        for (i = counts.length - 1; i >= 0; i--) {
            if (counts[i] > num) {
                counts[i] = num;
            }
            num -= counts[i];
        }
        counts[0] += num;
    }
}
