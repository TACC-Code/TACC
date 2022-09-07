class BackupThread extends Thread {
    public static void main(String[] arg) {
        try {
            String filename = arg[arg.length - 1];
            boolean use_default_md5 = false;
            boolean use_native_lib = true;
            for (int i = 0; i < arg.length - 1; i++) {
                if (arg[i].equals("--use-default-md5")) {
                    use_default_md5 = true;
                } else if (arg[i].equals("--no-native-lib")) {
                    use_native_lib = false;
                }
            }
            byte[] buf = new byte[65536];
            int num_read;
            if (use_default_md5) {
                InputStream in = new BufferedInputStream(new FileInputStream(filename));
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                while ((num_read = in.read(buf)) != -1) {
                    digest.update(buf, 0, num_read);
                }
                System.out.println(MD5.asHex(digest.digest()) + "  " + filename);
                in.close();
            } else {
                if (!use_native_lib) {
                    MD5.initNativeLibrary(true);
                }
                MD5InputStream in = new MD5InputStream(new BufferedInputStream(new FileInputStream(filename)));
                while ((num_read = in.read(buf)) != -1) ;
                System.out.println(MD5.asHex(in.hash()) + "  " + filename);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
