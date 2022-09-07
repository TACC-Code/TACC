class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: GZIPTestServer port file");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSock = new ServerSocket(port);
        Socket sock = serverSock.accept();
        GZIPInputStream in = new GZIPInputStream(sock.getInputStream());
        FileOutputStream out = new FileOutputStream(args[1]);
        byte[] buf = new byte[8192];
        int nread;
        while ((nread = in.read(buf)) > 0) {
            out.write(buf, 0, nread);
        }
        out.close();
        sock.close();
    }
}
