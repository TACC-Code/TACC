class BackupThread extends Thread {
    public void write() throws IOException {
        OutputStream stream;
        if (url.getProtocol().equals("file")) {
            stream = new FileOutputStream(url.getFile());
        } else {
            stream = url.openConnection().getOutputStream();
        }
        Writer writer = new OutputStreamWriter(stream);
        writeXML(writer);
        writer.close();
    }
}
