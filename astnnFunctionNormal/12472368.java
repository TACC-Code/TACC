class BackupThread extends Thread {
    public static void signFile(String inFile, String outFile, Certificate[] certChain, PrivateKey privKey, TSAClient tsaCli, Rectangle Rctng, String location, String reason) throws IOException, DocumentException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, CertificateParsingException, SignatureException, NotEnoughSpaceException {
        PdfReader reader = new PdfReader(inFile);
        FileOutputStream fout = new FileOutputStream(outFile);
        PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setCrypto(null, certChain, null, PdfSignatureAppearance.WINCER_SIGNED);
        sap.setVisibleSignature(Rctng, 1, "Signature");
        sap.setLocation(location);
        sap.setReason(reason);
        PdfSignature pdfSig = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
        pdfSig.setReason(sap.getReason());
        pdfSig.setLocation(sap.getLocation());
        pdfSig.setContact(sap.getContact());
        pdfSig.setDate(new PdfDate(sap.getSignDate()));
        sap.setCryptoDictionary(pdfSig);
        int contentEstimated = 15000;
        HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
        exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
        sap.preClose(exc);
        PdfPKCS7 sgn = new PdfPKCS7(privKey, certChain, null, "SHA1", null, false);
        InputStream data = sap.getRangeStream();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte buf[] = new byte[8192];
        int n;
        while ((n = data.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
        byte hash[] = messageDigest.digest();
        Calendar cal = Calendar.getInstance();
        byte[] ocsp = null;
        if (certChain.length >= 2) {
            String url = PdfPKCS7.getOCSPURL((X509Certificate) certChain[0]);
            if (url != null && url.length() > 0) ocsp = new OcspClientBouncyCastle((X509Certificate) certChain[0], (X509Certificate) certChain[1], url).getEncoded();
        }
        byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp);
        sgn.update(sh, 0, sh.length);
        byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, tsaCli, ocsp);
        if (contentEstimated + 2 < encodedSig.length) throw new NotEnoughSpaceException();
        byte[] paddedSig = new byte[contentEstimated];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        sap.close(dic2);
    }
}
