class BackupThread extends Thread {
    public static void subBomb() {
        for (int k = 0; k < 19; k++) {
            bomb[k] = bomb[k + 1];
            bomb[19] = null;
        }
    }
}
