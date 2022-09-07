class BackupThread extends Thread {
    private static void error(String[] args) {
        if (args.length != 2) {
            System.out.println("The program needs two filenames. The first one is the file," + " where to read from, the second one is, where to write on!!");
            System.exit(0);
        }
    }
}
