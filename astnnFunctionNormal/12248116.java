class BackupThread extends Thread {
    public void deleteItem(int posToDelete) {
        for (int i = posToDelete; i < size - 1; i++) {
            A[i] = A[i + 1];
            B[i] = B[i + 1];
            similarity[i] = similarity[i + 1];
        }
        size--;
    }
}
