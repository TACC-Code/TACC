class BackupThread extends Thread {
    protected AbstractEvaluator() {
        String inputId = UUID.randomUUID().toString();
        this.merId = inputId;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] resultingBytes = md5.digest(inputId.getBytes());
            this.merId = Base64.encodeBase64URLSafeString(resultingBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
