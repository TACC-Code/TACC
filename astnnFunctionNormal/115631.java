class BackupThread extends Thread {
    public static void main(String args[]) {
        int num = args.length;
        String classname = args[0];
        String mainArgs[] = new String[num - 1];
        for (int i = 0; i < num - 2; i++) {
            mainArgs[i] = args[i + 1];
        }
        Launcher launcher = new Launcher();
        launcher.startApp(classname, mainArgs);
    }
}
