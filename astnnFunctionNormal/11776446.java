class BackupThread extends Thread {
    public void openConnection(String url, String user, String pw, String className) {
        try {
            Class.forName(className).newInstance();
            conn = DriverManager.getConnection(url, user, pw);
            conn.setAutoCommit(true);
        } catch (Exception ex) {
            System.out.println("Error opening connection");
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }
}
