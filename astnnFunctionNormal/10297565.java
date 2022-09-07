class BackupThread extends Thread {
    private static void pipeString(Reader reader, StringBuffer writer) throws IOException {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            if (!(buf[0] == '<' && buf[1] == '?')) writer.append(buf, 0, read);
        }
    }
}
