class BackupThread extends Thread {
        public void addToQueue(Object o) {
            if (!isFull) {
                pos++;
                q[pos] = o;
            } else {
                for (int i = 0; i < size - 1; i++) {
                    q[i] = q[i + 1];
                }
                q[size - 1] = o;
            }
            if (pos >= (size - 1)) {
                isFull = true;
            }
        }
}
