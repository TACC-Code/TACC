class BackupThread extends Thread {
    protected String constructCacheFileName(StaticAsset staticAsset, Map<String, String> parameterMap) {
        StringBuilder sb = new StringBuilder(200);
        sb.append(staticAsset.getFullUrl().substring(0, staticAsset.getFullUrl().lastIndexOf('.')));
        sb.append("---");
        StringBuilder sb2 = new StringBuilder(200);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        sb2.append(format.format(staticAsset.getAuditable().getDateUpdated() == null ? staticAsset.getAuditable().getDateCreated() : staticAsset.getAuditable().getDateUpdated()));
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            sb2.append('-');
            sb2.append(entry.getKey());
            sb2.append('-');
            sb2.append(entry.getValue());
        }
        String digest;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(sb2.toString().getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            digest = number.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        sb.append(pad(digest, 32, '0'));
        sb.append('.');
        sb.append(staticAsset.getFileExtension());
        return sb.toString();
    }
}
