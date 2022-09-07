class BackupThread extends Thread {
    public void shiftLines() {
        int trueNb = tiles.length;
        for (int i = 0; i < (trueNb - 1); i++) {
            tiles[i] = tiles[i + 1];
        }
    }
}
