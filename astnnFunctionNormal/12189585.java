class BackupThread extends Thread {
    public boolean download() {
        try {
            byte[] buffer = new byte[4 * 1024];
            int read;
            URLConnection con;
            con = url.openConnection();
            con.connect();
            this.contentType = con.getContentType();
            this.destination = File.getTemp("dow");
            InputStream in = con.getInputStream();
            FileOutputStream w = this.destination.getOutputStream();
            while ((read = in.read(buffer)) > 0) w.write(buffer, 0, read);
            w.close();
            in.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger().error(ex);
            return false;
        }
    }
}
