class BackupThread extends Thread {
    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }
        try {
            Security.addProvider(new IAIK());
            try {
                Class eccProviderClass = Class.forName("iaik.security.ecc.provider.ECCProvider");
                Provider eccProvider = (Provider) eccProviderClass.newInstance();
                Security.addProvider(eccProvider);
            } catch (Exception ex) {
            }
            Module pkcs11Module = Module.getInstance(args[0]);
            pkcs11Module.initialize(null);
            Token token = Util.selectToken(pkcs11Module, output_, input_);
            if (token == null) {
                output_.println("We have no token to proceed. Finished.");
                output_.flush();
                System.exit(0);
            }
            TokenInfo tokenInfo = token.getTokenInfo();
            output_.println("################################################################################");
            output_.println("Information of Token:");
            output_.println(tokenInfo);
            output_.println("################################################################################");
            output_.println("################################################################################");
            output_.println("Reading certificate from file: " + args[1]);
            Session session = Util.openAuthorizedSession(token, Token.SessionReadWriteBehavior.RW_SESSION, output_, input_);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "IAIK");
            FileInputStream fileInputStream = new FileInputStream(args[1]);
            Collection certificateChain = certificateFactory.generateCertificates(fileInputStream);
            if (certificateChain.size() < 1) {
                output_.println("Did not find any certificate in the given input file. Finished.");
                output_.flush();
                System.exit(0);
            }
            iaik.x509.X509Certificate x509Certificate = (iaik.x509.X509Certificate) certificateChain.iterator().next();
            certificateChain.remove(x509Certificate);
            output_.println("################################################################################");
            output_.println("################################################################################");
            output_.println("Searching for corresponding private key on token.");
            PublicKey publicKey = x509Certificate.getPublicKey();
            Object searchTemplate = null;
            if (publicKey.getAlgorithm().equalsIgnoreCase("RSA")) {
                java.security.interfaces.RSAPublicKey rsaPublicKey = (java.security.interfaces.RSAPublicKey) publicKey;
                RSAPrivateKey rsaPrivateKeySearchTemplate = new RSAPrivateKey();
                byte[] modulus = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(rsaPublicKey.getModulus());
                rsaPrivateKeySearchTemplate.getModulus().setByteArrayValue(modulus);
                searchTemplate = rsaPrivateKeySearchTemplate;
            } else if (publicKey.getAlgorithm().equalsIgnoreCase("DSA")) {
                java.security.interfaces.DSAPublicKey dsaPublicKey = (java.security.interfaces.DSAPublicKey) publicKey;
                DSAParams dsaParams = dsaPublicKey.getParams();
                DSAPrivateKey dsaPrivateKeySearchTemplate = new DSAPrivateKey();
                byte[] g = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(dsaParams.getG());
                byte[] p = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(dsaParams.getP());
                byte[] q = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(dsaParams.getQ());
                dsaPrivateKeySearchTemplate.getBase().setByteArrayValue(g);
                dsaPrivateKeySearchTemplate.getPrime().setByteArrayValue(p);
                dsaPrivateKeySearchTemplate.getSubprime().setByteArrayValue(q);
                searchTemplate = dsaPrivateKeySearchTemplate;
            } else if (publicKey.getAlgorithm().equalsIgnoreCase("DH") || publicKey.getAlgorithm().equalsIgnoreCase("DiffieHellman")) {
                javax.crypto.interfaces.DHPublicKey dhPublicKey = (javax.crypto.interfaces.DHPublicKey) publicKey;
                DHParameterSpec dhParams = dhPublicKey.getParams();
                DHPrivateKey dhPrivateKeySearchTemplate = new DHPrivateKey();
                byte[] g = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(dhParams.getG());
                byte[] p = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(dhParams.getP());
                dhPrivateKeySearchTemplate.getBase().setByteArrayValue(g);
                dhPrivateKeySearchTemplate.getPrime().setByteArrayValue(p);
                searchTemplate = dhPrivateKeySearchTemplate;
            }
            byte[] objectID = null;
            if (searchTemplate != null) {
                session.findObjectsInit(searchTemplate);
                Object[] foundKeyObjects = session.findObjects(1);
                if (foundKeyObjects.length > 0) {
                    Key foundKey = (Key) foundKeyObjects[0];
                    objectID = foundKey.getId().getByteArrayValue();
                    output_.println("found a correponding key on the token: ");
                    output_.println(foundKey);
                } else {
                    output_.println("found no correponding key on the token.");
                }
                session.findObjectsFinal();
            } else {
                output_.println("public key is neither RSA, DSA nor DH.");
            }
            output_.println("################################################################################");
            output_.println("################################################################################");
            output_.println("Create certificate object(s) on token.");
            iaik.x509.X509Certificate currentCertificate = x509Certificate;
            boolean importedCompleteChain = false;
            while (!importedCompleteChain) {
                X509PublicKeyCertificate pkcs11X509PublicKeyCertificate = new X509PublicKeyCertificate();
                Name subjectName = (Name) currentCertificate.getSubjectDN();
                Name issuerName = (Name) currentCertificate.getIssuerDN();
                String subjectCommonName = subjectName.getRDN(ObjectID.commonName);
                String issuerCommonName = issuerName.getRDN(ObjectID.commonName);
                char[] label = (subjectCommonName + "'s " + ((issuerCommonName != null) ? issuerCommonName + " " : "") + "Certificate").toCharArray();
                byte[] newObjectID;
                if (objectID == null) {
                    if (publicKey instanceof java.security.interfaces.RSAPublicKey) {
                        newObjectID = ((java.security.interfaces.RSAPublicKey) publicKey).getModulus().toByteArray();
                        MessageDigest digest = MessageDigest.getInstance("SHA-1");
                        newObjectID = digest.digest(newObjectID);
                    } else if (publicKey instanceof java.security.interfaces.DSAPublicKey) {
                        newObjectID = ((java.security.interfaces.DSAPublicKey) publicKey).getY().toByteArray();
                        MessageDigest digest = MessageDigest.getInstance("SHA-1");
                        newObjectID = digest.digest(newObjectID);
                    } else {
                        newObjectID = currentCertificate.getFingerprint("SHA-1");
                    }
                } else {
                    newObjectID = objectID;
                }
                byte[] encodedSubject = ((Name) currentCertificate.getSubjectDN()).getEncoded();
                byte[] encodedIssuer = ((Name) currentCertificate.getIssuerDN()).getEncoded();
                byte[] serialNumber = currentCertificate.getSerialNumber().toByteArray();
                pkcs11X509PublicKeyCertificate.getToken().setBooleanValue(Boolean.TRUE);
                pkcs11X509PublicKeyCertificate.getPrivate().setBooleanValue(Boolean.FALSE);
                pkcs11X509PublicKeyCertificate.getLabel().setCharArrayValue(label);
                pkcs11X509PublicKeyCertificate.getId().setByteArrayValue(newObjectID);
                pkcs11X509PublicKeyCertificate.getSubject().setByteArrayValue(encodedSubject);
                pkcs11X509PublicKeyCertificate.getIssuer().setByteArrayValue(encodedIssuer);
                pkcs11X509PublicKeyCertificate.getSerialNumber().setByteArrayValue(serialNumber);
                pkcs11X509PublicKeyCertificate.getValue().setByteArrayValue(currentCertificate.getEncoded());
                output_.println(pkcs11X509PublicKeyCertificate);
                output_.println("________________________________________________________________________________");
                session.createObject(pkcs11X509PublicKeyCertificate);
                if (certificateChain.size() > 0) {
                    currentCertificate = (iaik.x509.X509Certificate) certificateChain.iterator().next();
                    certificateChain.remove(currentCertificate);
                    objectID = null;
                } else {
                    importedCompleteChain = true;
                }
            }
            output_.println("################################################################################");
            session.closeSession();
            pkcs11Module.finalize(null);
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
    }
}
