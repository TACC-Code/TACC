class BackupThread extends Thread {
    public void adjust(File file) throws IOException {
        BufferedReader reader = getBufferedReader(file);
        PrintWriter writer = getPrintWriter(file);
        findStartOfTypeMapping(reader, writer);
        writeCustomTypeMapping(writer);
        writeStartOfTypeMapping(writer);
        writeTail(reader, writer);
        writer.close();
        reader.close();
        changeFiles(file);
    }
}
