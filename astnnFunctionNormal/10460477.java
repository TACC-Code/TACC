class BackupThread extends Thread {
    private void login(java.awt.event.ActionEvent evt) {
        String ipAddress = "";
        int tcpPort = 0;
        String host;
        int port;
        int delay;
        char[] passphrase;
        int runloop = 2;
        ipAddress = this.getParameter("IP");
        tcpPort = Integer.parseInt(this.getParameter("PORT"));
        delay = Integer.parseInt(this.getParameter("DELAY"));
        while (runloop > 0) {
            try {
                host = ipAddress;
                port = tcpPort;
                String p = "changeit";
                passphrase = p.toCharArray();
                System.out.println("Passphrase is: " + passphrase);
                System.out.println("test");
                File file = new File("netwallcacerts");
                KeyStore ks;
                try {
                    System.out.println("Loading KeyStore " + file + "...");
                    InputStream in = new FileInputStream(file);
                    ks = KeyStore.getInstance(KeyStore.getDefaultType());
                    ks.load(in, passphrase);
                    in.close();
                } catch (Exception h) {
                    System.out.println("Makeing new store!");
                    ks = KeyStore.getInstance(KeyStore.getDefaultType());
                    ks.load(null, passphrase);
                }
                SSLContext context = SSLContext.getInstance("TLS");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(ks);
                X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
                SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
                context.init(null, new TrustManager[] { tm }, null);
                factory = context.getSocketFactory();
                System.out.println("Opening connection to " + host + ":" + port + "...");
                socket = (SSLSocket) factory.createSocket(host, port);
                socket.setSoTimeout(2000);
                socket.setKeepAlive(true);
                try {
                    System.out.println("Starting SSL handshake...");
                    socket.startHandshake();
                    System.out.println("Handshake successful.");
                    System.out.flush();
                    int i;
                    byte[] b = new byte[1];
                    byte[] tb;
                    try {
                        i = socket.getInputStream().read(b);
                        if ((int) b[0] == 0) {
                            String user = jTextFieldUserName.getText();
                            String pass = jPasswordUserPassword.getText();
                            b = new byte[3 + user.length() + pass.length()];
                            b[0] = 0;
                            b[1] = new Integer(user.length()).byteValue();
                            System.out.println("user length: " + user.length());
                            b[2] = new Integer(pass.length()).byteValue();
                            System.out.println("password length: " + pass.length());
                            tb = (user + pass).getBytes();
                            int k;
                            for (k = 0; k < tb.length; k++) {
                                b[3 + k] = tb[k];
                            }
                            socket.getOutputStream().write(b);
                            socket.getOutputStream().flush();
                        } else {
                            socket.close();
                            return;
                        }
                        b = new byte[1];
                        i = socket.getInputStream().read(b);
                        if ((int) b[0] == 2) {
                            socket.setSoTimeout(0);
                            jLogin.setVisible(false);
                            jConnect.setVisible(true);
                            jLogin.setEnabled(false);
                            System.out.println("Established connection.");
                            pingTimer = new Timer();
                            pingTimer.scheduleAtFixedRate(new pingTheServer(socket.getOutputStream()), 200, delay);
                            msgRec = new messageReceiver(socket.getInputStream(), this);
                            msgRec.start();
                            return;
                        } else {
                            socket.close();
                            writeMessage("Error: Username or Password is incorrect.");
                            return;
                        }
                    } catch (IOException e) {
                        System.out.println("IO Exception");
                        socket.close();
                        return;
                    }
                } catch (SSLException e) {
                }
                Frame f = new Frame();
                MessageDialog md = new MessageDialog(f, "Accept Netwall Certificate?", "Yes", "No");
                if ("Yes".equals(md.getUserAction())) {
                    X509Certificate[] chain = tm.chain;
                    if (chain == null) {
                        System.out.println("Could not obtain server certificate chain");
                        return;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println();
                    System.out.println("Server sent " + chain.length + " certificate(s):");
                    System.out.println();
                    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    for (int i = 0; i < chain.length; i++) {
                        X509Certificate cert = chain[i];
                        System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
                        System.out.println("   Issuer  " + cert.getIssuerDN());
                        sha1.update(cert.getEncoded());
                        System.out.println("   sha1    " + toHexString(sha1.digest()));
                        md5.update(cert.getEncoded());
                        System.out.println("   md5     " + toHexString(md5.digest()));
                        System.out.println();
                    }
                    int k = 0;
                    X509Certificate cert = chain[k];
                    String alias = host + "-" + (k + 1);
                    ks.setCertificateEntry(alias, cert);
                    OutputStream out = new FileOutputStream("netwallcacerts");
                    ks.store(out, passphrase);
                    out.close();
                } else {
                    writeMessage("Error: Cannot connect to Netwall Server");
                    reset();
                    return;
                }
            } catch (Exception exception) {
            }
        }
        runloop--;
        writeMessage("Error: Failed to connect Netwall Server");
        reset();
    }
}
