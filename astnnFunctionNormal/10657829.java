class BackupThread extends Thread {
    @Override
    protected String getChannelImplName() {
        return "biz.xsoftware.impl.nio.cm.threaded.ThdTCPChannel";
    }
}
