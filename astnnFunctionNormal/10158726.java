class BackupThread extends Thread {
    @Override
    protected void setFile(ActionContext actionContext, PropertySet config, String path, File file) throws IOException {
        File target = getFile(actionContext, config, path);
        FileUtils.copyFile(file, target);
    }
}
