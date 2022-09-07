class BackupThread extends Thread {
    public void requestPatchDump(int bankNum, int patchNum) {
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("bankNum", (bankNum << 1) + 1), new SysexHandler.NameValue("patchNum", patchNum)));
    }
}
