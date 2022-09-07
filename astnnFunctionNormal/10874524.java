class BackupThread extends Thread {
    public static void exchange(int[] a, int i) {
        int temp;
        temp = a[i];
        a[i] = a[i + 1];
        a[i + 1] = temp;
    }
}
