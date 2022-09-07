class BackupThread extends Thread {
    public static void main(String[] args) {
        Md5 m = new Md5();
        String k = args[0];
        m.update(k.getBytes());
        long h = m.digest();
        System.out.println(h);
    }
}
