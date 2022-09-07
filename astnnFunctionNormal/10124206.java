class BackupThread extends Thread {
    public void requestPatchDump(int bankNum, int patchNum) {
        if (bankNum == 0) send(bankARequestDump.toSysexMessage(getChannel(), patchNum)); else send(bankBRequestDump.toSysexMessage(getChannel(), patchNum));
    }
}
