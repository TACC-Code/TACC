class BackupThread extends Thread {
    public RPoint[] getSideHandles(int s) {
        RPoint[] sideHandles;
        sideHandles = new RPoint[2];
        if (s != -1) {
            sideHandles[0] = handles[s];
            sideHandles[1] = handles[s + 1];
        }
        return sideHandles;
    }
}
