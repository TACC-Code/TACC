class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        String host;
        int port;
        char[] passphrase;
        if ((args.length == 1) || (args.length == 2)) {
            String[] c = args[0].split(":");
            host = c[0];
            port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
            String p = (args.length == 1) ? "changeit" : args[1];
            passphrase = p.toCharArray();
        } else {
            logger.debug("Usage: java InstallCert <host>[:port] [passphrase]");
            return;
        }
        File file = new File("jssecacerts");
        if (file.isFile() == false) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
            file = new File(dir, "jssecacerts");
            if (file.isFile() == false) {
                file = new File(dir, "cacerts");
            }
        }
        logger.debug("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory factory = context.getSocketFactory();
        logger.debug("Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);
        try {
            logger.debug("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            logger.debug("No errors, certificate is already trusted");
        } catch (SSLException e) {
            logger.debug("");
            e.printStackTrace(System.out);
        }
        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            logger.debug("Could not obtain server certificate chain");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        logger.debug("");
        logger.debug("Server sent " + chain.length + " certificate(s):");
        logger.debug("");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            logger.debug(" " + (i + 1) + " Subject " + cert.getSubjectDN());
            logger.debug("   Issuer  " + cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            logger.debug("   sha1    " + toHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            logger.debug("   md5     " + toHexString(md5.digest()));
            logger.debug("");
        }
        logger.debug("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
        String line = reader.readLine().trim();
        int k;
        try {
            k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
        } catch (NumberFormatException e) {
            logger.debug("KeyStore not changed");
            return;
        }
        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ks.setCertificateEntry(alias, cert);
        OutputStream out = new FileOutputStream("jssecacerts");
        ks.store(out, passphrase);
        out.close();
        logger.debug("");
        logger.debug(cert);
        logger.debug("");
        logger.debug("Added certificate to keystore 'jssecacerts' using alias '" + alias + "'");
    }
}
