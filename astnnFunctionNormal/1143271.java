class BackupThread extends Thread {
    private void initStores() throws IOException, GeneralSecurityException {
        String path, type, passwd;
        if ((path = System.getProperty(NAME + ".trustStore")) != null) {
            type = System.getProperty(NAME + ".trustStoreType", KeyStore.getDefaultType());
            passwd = System.getProperty(NAME + ".trustStorePassword");
        } else if ((path = System.getProperty(JSSE + ".trustStore")) != null) {
            type = System.getProperty(JSSE + ".trustStoreType", KeyStore.getDefaultType());
            passwd = System.getProperty(JSSE + ".trustStorePassword");
        } else {
            path = System.getProperty("java.home") + "/lib/security/cacerts";
            type = KeyStore.getDefaultType();
            passwd = null;
        }
        KeyStore kstore = KeyStore.getInstance(type);
        InputStream in;
        URL url = null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
        }
        if (url != null) {
            in = url.openStream();
        } else {
            in = new FileInputStream(path);
        }
        try {
            kstore.load(in, (passwd != null) ? passwd.toCharArray() : null);
        } finally {
            in.close();
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "loaded trust store from {0} ({1})", new Object[] { path, type });
        }
        String cstores = System.getProperty(NAME + ".ldapCertStores");
        List l = new ArrayList();
        if (cstores != null) {
            StringTokenizer tok = new StringTokenizer(cstores, ",");
            while (tok.hasMoreTokens()) {
                String s = tok.nextToken().trim();
                Matcher m = hostPortPattern.matcher(s);
                try {
                    CertStoreParameters params = m.matches() ? new LDAPCertStoreParameters(m.group(1), Integer.parseInt(m.group(2))) : new LDAPCertStoreParameters(s);
                    l.add(CertStore.getInstance("LDAP", params));
                } catch (Exception e) {
                    logger.log(Level.WARNING, "exception initializing cert store", e);
                }
            }
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "using cert stores {0}", new Object[] { l });
        }
        certStores = (CertStore[]) l.toArray(new CertStore[l.size()]);
        trustStore = kstore;
    }
}
