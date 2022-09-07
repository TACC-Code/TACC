class BackupThread extends Thread {
    public static void subMove() {
        for (int k = 0; k < 19; k++) {
            move[k] = move[k + 1];
            move[19] = null;
        }
    }
}
