class BackupThread extends Thread {
    public boolean isAlsoReadwriteField(final String name) {
        return this.readwriteParameterNames.contains(name);
    }
}
