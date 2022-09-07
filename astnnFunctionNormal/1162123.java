class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        String host;
        int port;
        char[] passphrase1;
        char[] passphrase2;
        if ((args.length > 0)) {
            String[] c = args[0].split(":");
            host = c[0];
            port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);
            String p1 = (args.length == 1) ? "changeit" : args[1];
            passphrase1 = p1.toCharArray();
            String p2 = (args.length == 2) ? "" : args[2];
            passphrase2 = p2.toCharArray();
        } else {
            System.out.println("Uso: java InstallCert <host>[:port] [senha1] [senha2]");
            System.out.println("Onde senha1 � a senha do armazem seguro, que por default � 'changeit' (sem os 's)");
            System.out.println("e senha2 � a senha do armazem com a sua chave particular e certificado.");
            return;
        }
        KeyStore ts = openTrustStore(passphrase1);
        X509TrustManager tm = openTrustManager(ts, CONNECTION_KEYSTORE_LOCATION, passphrase2);
        SSLSocket socket = customSocketFactory((TrustManagerDecorator) tm, host, port);
        socket.setSoTimeout(10000);
        try {
            System.out.println("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            System.out.println();
            System.out.println("No errors, certificate is already trusted");
        } catch (SSLException e) {
            System.out.println();
            e.printStackTrace(System.out);
        }
        X509Certificate[] chain = ((TrustManagerDecorator) tm).chain;
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
        System.out.println("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
        String line = reader.readLine().trim();
        int k;
        try {
            k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
        } catch (NumberFormatException e) {
            System.out.println("KeyStore not changed");
            return;
        }
        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ts.setCertificateEntry(alias, cert);
        OutputStream out = new FileOutputStream(EXTRACTED_KEYSTORE_LOCATION);
        ts.store(out, passphrase1);
        out.close();
        System.out.println();
        System.out.println(cert);
        System.out.println();
        System.out.println("Added certificate to keystore '" + EXTRACTED_KEYSTORE_LOCATION + "' using alias '" + alias + "'");
    }
}
