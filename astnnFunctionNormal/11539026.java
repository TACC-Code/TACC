class BackupThread extends Thread {
        public void run() {
            try {
                for (int i = 0; i < 100; i++) {
                    URLConnection con = url.openConnection();
                    con.connect();
                    System.out.println(i + " " + url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
