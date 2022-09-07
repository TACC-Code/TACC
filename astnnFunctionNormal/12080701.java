class BackupThread extends Thread {
    public void requestSingleBankDump(int bankNum, int patchNum) {
        for (int i = 0; i < 8; i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
            }
            SysexHandler.NameValue nVs[] = new SysexHandler.NameValue[2];
            nVs[0] = new SysexHandler.NameValue("patchnumber", bankNum * 64 + patchNum * 8 + i);
            nVs[1] = new SysexHandler.NameValue("checksum", 0);
            Patch p = new Patch(SYS_REQ.toByteArray(getChannel(), nVs));
            calculateChecksum(p, 5, 10, 11);
            send(p.sysex);
            try {
                Thread.sleep(sleepTime);
            } catch (Exception e) {
            }
        }
    }
}
