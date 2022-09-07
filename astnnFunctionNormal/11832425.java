class BackupThread extends Thread {
    private Object[] getArgs(Object value[]) {
        Object[] args = new Object[value.length - 1];
        for (int i = 0; i < args.length; i++) {
            args[i] = value[i + 1];
        }
        return args;
    }
}
