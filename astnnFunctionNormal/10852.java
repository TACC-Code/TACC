class BackupThread extends Thread {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("PixelDataTest: Error: Too few parameters");
            System.out.println("Usage: PixelDataTest <dicom-file>");
            System.exit(1);
        }
        for (int i = 0; i < args.length; i++) {
            try {
                File inFile = new File(args[i]), outFile = new File(inFile.getAbsolutePath() + ".TEST");
                readAndRewrite(inFile, outFile);
            } catch (IOException ioe) {
                System.err.println("FAILED: " + ioe);
            }
        }
    }
}
