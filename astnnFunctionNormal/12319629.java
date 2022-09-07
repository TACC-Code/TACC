class BackupThread extends Thread {
    @Override
    public void requestPatchDump(final int bankNum, final int patchNum) {
        send(patchRequestHandler.toSysexMessage(getChannel(), new SysexHandler.NameValue("bankNum", bankNum + 1), new SysexHandler.NameValue("patchNum", patchNum)));
    }
}
