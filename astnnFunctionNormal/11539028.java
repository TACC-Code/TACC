class BackupThread extends Thread {
    public static String saveStream(InputStream in, String file) {
        try {
            FileChannel fc = (new FileOutputStream(file)).getChannel();
        } catch (FileNotFoundException e1) {
            return null;
        }
        BufferedReader tmp = new BufferedReader(new InputStreamReader(in));
        char[] target = new char[65536];
        int nb = 0;
        try {
            while ((nb = tmp.read(target)) > 0) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
