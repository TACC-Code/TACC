class BackupThread extends Thread {
    public Object pop() {
        Object result = queue[0];
        for (int i = 0; i < size - 1; i++) {
            queue[i] = queue[i + 1];
        }
        return result;
    }
}
