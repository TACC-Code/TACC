class BackupThread extends Thread {
    public void update(Writer writer) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm unavailable.", e);
        }
        File file = writer.getFile();
        try {
            digest(md5, new Long(System.currentTimeMillis()));
            if (file.getPath() != null) {
                digest(md5, file.getPath());
            }
            digest(md5, new Long(writer.getStream().getLength()));
            Information information = file.getDocument().getInformation();
            if (information != null) {
                for (Map.Entry<PdfName, PdfDirectObject> informationObjectEntry : information.getBaseDataObject().entrySet()) {
                    digest(md5, informationObjectEntry.getKey());
                    digest(md5, informationObjectEntry.getValue());
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("File identifier digest failed.", e);
        }
        PdfString versionID = new PdfString(md5.digest(), SerializationModeEnum.Hex);
        getBaseDataObject().set(1, versionID);
        if (getBaseDataObject().get(0).equals(PdfString.Empty)) {
            getBaseDataObject().set(0, versionID);
        }
    }
}
