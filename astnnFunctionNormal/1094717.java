class BackupThread extends Thread {
    public static void main(String[] args) {
        String[] args1;
        if (args == null || args.length == 0) {
            System.out.println("Error No class to run");
            return;
        } else if (args.length < 2) {
            args1 = new String[0];
        } else {
            args1 = new String[args.length - 1];
            for (int i = 0; i < args.length - 1; i++) {
                args1[i] = args[i + 1];
            }
        }
        new Run(Run.SYSTEM_JARS_FILENAME, JARS, args[0], args1);
    }
}
