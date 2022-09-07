class BackupThread extends Thread {
    private boolean checkJar(URL url) throws CertificateException, NoSuchProviderException, IOException, Exception {
        JarEntry next;
        boolean signatureFound = false;
        boolean acceptAllSigner = true;
        userCert = null;
        InputStream is = url.openConnection().getInputStream();
        JarInputStream jis = new JarInputStream(is);
        while ((next = jis.getNextJarEntry()) != null) {
            Certificate[] chain = next.getCertificates();
            if (chain != null) {
                for (int i = 0; i < chain.length; i++) {
                    X509Certificate x509 = (X509Certificate) chain[i];
                }
            }
            if (next.getName().endsWith("RSA")) {
                signatureFound = true;
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Collection coll = cf.generateCertificates(jis);
                if (coll == null || coll.size() < 1) {
                    UserMessages.error("Plugin " + url + res.getString("NOT SIGNED"));
                    return false;
                }
                X509Certificate userChain[] = CertificateUtility.toX509Chain(coll);
                userCert = userChain[0];
                userCert.checkValidity();
                boolean acceptSigner = false;
                boolean userCertVerified = false;
                KeyStoreManager manager = ResourceManager.getKeystoreManager();
                String signer = manager.getAliasFromCertifcate(userCert);
                if (signer != null) {
                    acceptSigner = true;
                    userCertVerified = true;
                    logger.info("Plugin signer known as: " + signer);
                } else {
                    userCertVerified = manager.verify(userChain);
                    if (!userCertVerified) {
                        CertificateGeneralDialog dialog = new CertificateGeneralDialog(userChain, url.toString());
                        dialog.show();
                        this.dialogShow = true;
                    }
                }
                if (!acceptSigner && userCertVerified) {
                    AddToKeystoreDialog dialog = new AddToKeystoreDialog(ResourceManager.getCurrentInstance(), url, userCert);
                    dialog.show();
                    this.dialogShow = true;
                    if (dialog.getResult() == AddToKeystoreDialog.ACCEPT) {
                        manager.addTrustedCertificate(userCert, true);
                        manager.reWriteKeyStore();
                        acceptSigner = true;
                    } else if (dialog.getResult() == AddToKeystoreDialog.ONLY_FOR_SESSION) {
                        acceptSigner = true;
                    }
                }
                acceptAllSigner = acceptAllSigner && acceptSigner;
            }
            jis.closeEntry();
        }
        jis.close();
        if (!signatureFound) {
            throw new IOException(res.getString("PLUGIN_JAR") + url + res.getString("NOT_SIGNED"));
        }
        return signatureFound && acceptAllSigner;
    }
}
