class BackupThread extends Thread {
    public void push(Object number) {
        for (int i = 0; i < size - 1; i++) {
            queue[i] = queue[i + 1];
        }
        queue[size - 1] = number;
    }
}
