class BackupThread extends Thread {
    public void requestPatchDump(int bankNum, int patchNum) {
        send(sysexRequestDump.toSysexMessage(getChannel(), bankNum));
    }
}
