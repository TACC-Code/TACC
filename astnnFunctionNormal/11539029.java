class BackupThread extends Thread {
    public static void display(URL url) {
        URLConnection con;
        try {
            con = url.openConnection();
            con.connect();
            String s = readStream(con.getInputStream());
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
