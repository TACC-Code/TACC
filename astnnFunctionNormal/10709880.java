class BackupThread extends Thread {
    public void addReadwriteFieldName(final String name) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        this.readwriteParameterNames.add(name);
    }
}
