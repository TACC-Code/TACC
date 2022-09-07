class BackupThread extends Thread {
    private void shiftArray(byte[] array) {
        for (int i = 0; i < (array.length - 1); i++) array[i] = array[i + 1];
        array[array.length - 1] = 0;
    }
}
