class BackupThread extends Thread {
    public static String signature(Map<String, CharSequence> params, String secret, String signName) {
        String result = null;
        if (params == null) return result;
        params.remove(signName);
        Map<String, CharSequence> treeMap = new TreeMap<String, CharSequence>();
        treeMap.putAll(params);
        Iterator<String> iter = treeMap.keySet().iterator();
        StringBuffer orgin = new StringBuffer(secret);
        while (iter.hasNext()) {
            String name = (String) iter.next();
            orgin.append(name).append(params.get(name));
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
        } catch (Exception e) {
            throw new java.lang.RuntimeException("sign error !");
        }
        return result;
    }
}
