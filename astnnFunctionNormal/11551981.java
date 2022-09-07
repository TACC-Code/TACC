class BackupThread extends Thread {
    protected String captureStream(InputStream s) throws IOException {
        BufferedInputStream in = new BufferedInputStream(s);
        StringWriter out = new StringWriter();
        try {
            int c;
            while ((c = in.read()) != -1) out.write(c);
        } finally {
            close(out);
            close(in);
        }
        return out.toString();
    }
}
