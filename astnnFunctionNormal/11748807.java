class BackupThread extends Thread {
    private String sha() {
        byte[] bytes = (city + street).getBytes();
        try {
            byte[] b = MessageDigest.getInstance("SHA").digest(bytes);
            String s = new BASE64Encoder().encode(b);
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }
}
