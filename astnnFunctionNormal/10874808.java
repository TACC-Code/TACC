class BackupThread extends Thread {
    @ActionScriptProperty(read = true, write = true, bindable = true)
    public String getTitle() {
        return title;
    }
}
