class BackupThread extends Thread {
    private String encrypt(MessageDigest md, String data) {
        md.reset();
        md.update(data.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String bhex = Integer.toHexString(b >= 0 ? b : 256 + b);
            bhex = bhex.substring(bhex.length() > 2 ? bhex.length() - 2 : 0);
            while (bhex.length() < 2) {
                bhex = "0" + bhex;
            }
            sb.append(bhex);
        }
        return sb.toString();
    }
}
