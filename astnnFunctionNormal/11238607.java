class BackupThread extends Thread {
    public void requestPatchDump(int bankNum, int patchNum) {
        int channel = getChannel();
        int progNum = bankNum * 5 + patchNum;
        SysexHandler.NameValue nVs[] = new SysexHandler.NameValue[2];
        nVs[0] = new SysexHandler.NameValue("channel", channel);
        nVs[1] = new SysexHandler.NameValue("progNum", progNum);
        send(SYS_REQ.toSysexMessage(channel, nVs));
    }
}
