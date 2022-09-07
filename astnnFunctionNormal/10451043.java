class BackupThread extends Thread {
    public void add(final String name, final File file) {
        try {
            if (this.boundary != null) {
                boundary();
                writeName(name);
                write("; filename=\"");
                write(file.getName());
                write("\"");
                newline();
                write("Content-Type: ");
                String type = URLConnection.guessContentTypeFromName(file.getName());
                if (type == null) {
                    type = "application/octet-stream";
                }
                writeln(type);
                newline();
                final byte[] buf = new byte[FormUtility.BUFFER_SIZE];
                int nread;
                final InputStream in = new FileInputStream(file);
                while ((nread = in.read(buf, 0, buf.length)) >= 0) {
                    this.os.write(buf, 0, nread);
                }
                newline();
            }
        } catch (final IOException e) {
            throw new BotError(e);
        }
    }
}
