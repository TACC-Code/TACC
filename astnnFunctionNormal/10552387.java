class BackupThread extends Thread {
    public void connect() throws Exception {
        InetAddress serverAddress = null;
        String servername = _options.getHostname();
        try {
            serverAddress = InetAddress.getByName(servername);
        } catch (UnknownHostException e) {
            System.err.println("  Unknown host " + servername);
            System.exit(-1);
        }
        System.out.print("  Connecting to " + servername + "...");
        Socket socket = new Socket(serverAddress, _options.getPort());
        System.out.println("  Connected");
        _handler = new ClientProtocolHandler(socket, _options);
        ReaderThread reader_thread = new ReaderThread(_handler.getSTDOUT());
        reader_thread.setDaemon(true);
        reader_thread.start();
        _handler.exchangeIdStrings();
        _handler.receiveServerKey();
        String server = socket.getInetAddress().getHostAddress();
        if (server.equals("127.0.0.1") == false) {
            check_host_key(_handler.getServerKeyPacket(), servername);
        } else debug("Forcing acceptance of host key for localhost");
        ITrueRandom trueRandom = new DevURandom();
        _handler.sendSessionKey(trueRandom);
        InputStreamReader inreader = new InputStreamReader(System.in);
        BufferedReader keyboardReader = new BufferedReader(inreader);
        String userName = _options.getUser();
        boolean authRequired = _handler.declareUser(userName);
        if (authRequired) {
            RSAPrivateKeyFile keyfile = null;
            try {
                keyfile = new RSAPrivateKeyFile(_options.getIdentityFile());
            } catch (FileNotFoundException e) {
            }
            if (keyfile != null) {
                RSAPrivateKey privateKey = null;
                if (keyfile.getCipherType() != Cipher.SSH_CIPHER_NONE) {
                    System.out.print("Enter passphrase for RSA key '" + _options.getIdentityFile() + "': ");
                    String passphrase = keyboardReader.readLine();
                    System.out.println();
                    privateKey = keyfile.getPrivateKey(passphrase);
                } else {
                    privateKey = keyfile.getPrivateKey();
                }
                if (_handler.authenticateUser(privateKey) == true) {
                    authRequired = false;
                }
            } else {
                _handler.debug("unknown identity file " + _options.getIdentityFile());
            }
            if (authRequired) {
                System.out.print("Password for " + userName + ": ");
                String password = keyboardReader.readLine();
                System.out.println();
                if (!_handler.authenticateUser(userName, password)) throw new SSHAuthFailedException();
            }
        }
        _handler.preparatoryOperations();
        _setupSucceeded = true;
        String command = _options.getCommand();
        if (command != null) {
            _handler.execCmd(command);
        } else {
            STDIN_OutputStream out = _handler.getSTDIN();
            WriterThread writer_thread = new WriterThread(out);
            writer_thread.start();
            _handler.execShell();
        }
        return;
    }
}
