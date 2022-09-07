class BackupThread extends Thread {
    public String generateToken(String seed) {
        try {
            byte id[] = seed.getBytes();
            byte now[] = new Long(System.currentTimeMillis()).toString().getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            String pre = RandomTools.toHex(md.digest());
            StringBuffer post = new StringBuffer();
            for (int l = 0; l < pre.length() - 1; l += 4) {
                post.append(pre.substring(l, l + 1));
            }
            return post.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
