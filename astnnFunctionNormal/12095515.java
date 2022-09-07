class BackupThread extends Thread {
    public void setPatchNum(int patchNum) {
        try {
            send(0xC0 + (getChannel() - 1), xvrt[patchNum]);
            Thread.sleep(150);
        } catch (Exception e) {
        }
        ;
    }
}
