class BackupThread extends Thread {
    public String getContentType() {
        String type = null;
        try {
            if (url_conn == null) url_conn = url.openConnection();
        } catch (IOException e) {
        }
        if (url_conn != null) type = url_conn.getContentType();
        if (type == null) type = "application/octet-stream";
        return type;
    }
}
