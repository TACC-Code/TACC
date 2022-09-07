class BackupThread extends Thread {
    private String getUploadId(String srv) {
        String id = null;
        try {
            URL url = new URL("http://" + srv + ".odsiebie.com/link_upload.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream stream = conn.getInputStream();
            Scanner in = new Scanner(stream);
            in.findInLine("\"");
            in.useDelimiter("\"");
            id = in.next();
            conn.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(Odsiebie.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return id;
        }
    }
}
