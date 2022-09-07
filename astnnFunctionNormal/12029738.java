class BackupThread extends Thread {
    public void testSingleAccess() {
        try {
            URL url = new URL(TEST_URL);
            URLConnection conn = url.openConnection();
            System.out.println(conn.getHeaderField(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
