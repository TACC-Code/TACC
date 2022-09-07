class BackupThread extends Thread {
    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equals("--help")) {
            try {
                EFHSSF viewer = new EFHSSF();
                viewer.setInputFile(args[0]);
                viewer.setOutputFile(args[1]);
                viewer.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("EFHSSF");
            System.out.println("General testbed for HSSFEventFactory based testing and " + "Code examples");
            System.out.println("Usage: java net.sourceforge.poi.hssf.dev.EFHSSF " + "file1 file2");
            System.out.println("   --will rewrite the file reading with the event api");
            System.out.println("and writing with the standard API");
        }
    }
}
