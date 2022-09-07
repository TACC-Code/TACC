class BackupThread extends Thread {
    public void addJarSignature(String alias, char[] keypass) throws AppDescriptorException, MalformedURLException, IOException, KeyStoreException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, UnrecoverableKeyException {
        String urlStr = getProperty(JAR_URL);
        if (urlStr == null) {
            throw new AppDescriptorException(JAR_URL + " not in descriptor");
        }
        URL url = new URL(urlStr);
        InputStream jarStream = url.openStream();
        addJarSignature(alias, keypass, jarStream);
    }
}
