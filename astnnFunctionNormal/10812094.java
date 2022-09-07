class BackupThread extends Thread {
    public final Channel findChannel(String name) {
        return ScriptVars.curConnection.getChannel(name);
    }
}
