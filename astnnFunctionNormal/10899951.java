class BackupThread extends Thread {
    public void saveToFile(String filename, String ftype, boolean overwrite) throws IOException {
        log.info(filename);
        if (!ftype.equalsIgnoreCase(ASCII)) {
            throw new IOException("WRITING " + ftype + " IS NOT IMPLEMENTED");
        }
        File ff = new File(filename);
        if (!ff.isAbsolute()) {
            ff = new File(CoGSetUtil.getFullPath(filename));
        }
        if (ff.exists() && !overwrite) throw new IOException("File already exists");
        FileWriter fstream = new FileWriter(ff);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(toString());
        out.close();
    }
}
