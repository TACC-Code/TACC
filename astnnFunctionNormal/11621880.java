class BackupThread extends Thread {
    public void connect(String chost, int cport) throws IOException, IllegalStateException {
        ProtocolReader reader;
        ProtocolWriter writer;
        if (isConnected) {
            throw new IllegalStateException();
        }
        if (cport == NetworkServer.DEFAULT_PORT || cport == NetworkServer.ANY_PORT) {
            cport = STBPNetworkServer.STBP_DEFAULT_PORT;
        }
        this.host = chost;
        this.port = cport;
        this.sock = new Socket(chost, cport);
        reader = new ProtocolReader(new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8")));
        writer = new ProtocolWriter(sock.getOutputStream());
        this.stbpproxy = initConnection(reader, writer);
        isConnected = true;
    }
}
