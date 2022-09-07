class BackupThread extends Thread {
    public static void writeProperties(File prefsFile, Properties props, String header) throws IOException {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(prefsFile);
            lockIfPossible(fout.getChannel(), false);
            props.store(fout, header);
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
