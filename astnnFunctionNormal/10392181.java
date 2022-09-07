class BackupThread extends Thread {
    public static String loadPatchFile(String filename) throws Exception {
        Reader reader = new BufferedReader(new FileReader(filename));
        StringWriter writer = new StringWriter();
        copyToWriter(reader, writer);
        return writer.toString();
    }
}
