class BackupThread extends Thread {
    public static void initProofread(String filename) {
        try {
            out = new BufferedWriter(new FileWriter(filename));
            out.write("javadoc proofread file: " + filename + "\n");
        } catch (IOException e) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
                out = null;
            }
            System.err.println("error opening file: " + filename);
        }
    }
}
