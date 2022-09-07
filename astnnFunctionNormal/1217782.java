class BackupThread extends Thread {
    private static String getReply(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is);
        StringWriter out = new StringWriter(8192);
        char[] buffer = new char[8192];
        int count;
        while ((count = isr.read(buffer)) > 0) out.write(buffer, 0, count);
        isr.close();
        return out.toString();
    }
}
