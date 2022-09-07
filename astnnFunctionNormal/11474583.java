class BackupThread extends Thread {
    public GenericCommunication(InputStream in, OutputStream out, IEvent event, JFTPConnection conn) {
        super("org.nilisoft.jftp4i.conn.GenericCommunication");
        this.event = event;
        this.connection = conn;
        InputStreamReader inreader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inreader);
        PrintWriter writer = new PrintWriter(out, false);
        BufferedInputStream _in = new BufferedInputStream(in);
        BufferedOutputStream _out = new BufferedOutputStream(out);
        JFTPStream stream = new JFTPStream(reader, _in, writer, _out, this);
        try {
            this.handler = new JFTPStreamHandler(JFTPStreamHandler.ASCII_TYPE, stream);
        } catch (JFTPConnectionException ftpex) {
            log.severe("An error ocurred while traying to initialize " + "the server streams. Error description: " + ftpex.toString());
        }
    }
}
