class BackupThread extends Thread {
    @Override
    public FederationRequest discover(InetAddress serviceProvider) throws FederationServiceDiscoveryFailed {
        try {
            int port = FederationService.getInstance().getFederatingPort();
            Socket sock = new Socket(serviceProvider, port);
            SSLSocketFactory sockFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket federatingSocket = (SSLSocket) sockFactory.createSocket(sock, serviceProvider.getHostAddress(), port, true);
            SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            federatingSocket.setEnabledCipherSuites(serverFactory.getSupportedCipherSuites());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(federatingSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(federatingSocket.getInputStream()));
            writer.write(FederationRequestType.ECHO.toString() + '\n');
            writer.flush();
            String responce = reader.readLine();
            if (FederationServiceMessageCodes.valueOf(responce) != FederationServiceMessageCodes.SERVICE_AVAILABLE) {
                throw new IOException("Unexpected responce: " + responce);
            }
            return new FederationRequest(federatingSocket, reader, writer, FederationRequestType.ECHO);
        } catch (IOException ioe) {
            throw new FederationServiceDiscoveryFailed("Error while discovery: " + ioe.toString());
        }
    }
}
