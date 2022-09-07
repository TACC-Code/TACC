class BackupThread extends Thread {
    public static void nailMain(NGContext context) throws java.security.NoSuchAlgorithmException, java.io.IOException {
        String[] args = context.getArgs();
        if (args.length == 0) {
            Set algs = getCryptoImpls("MessageDigest");
            for (Iterator i = algs.iterator(); i.hasNext(); ) {
                context.out.println(i.next());
            }
        } else {
            MessageDigest md = MessageDigest.getInstance(args[0]);
            byte[] b = new byte[1024];
            int bytesRead = context.in.read(b);
            while (bytesRead != -1) {
                md.update(b, 0, bytesRead);
                bytesRead = System.in.read(b);
            }
            byte[] result = md.digest();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < result.length; ++i) {
                buf.append(HEXCHARS[(result[i] >> 4) & 0x0f]);
                buf.append(HEXCHARS[result[i] & 0x0f]);
            }
            context.out.println(buf);
        }
    }
}
