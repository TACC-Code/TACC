class BackupThread extends Thread {
    public void mouseClicked(java.awt.event.MouseEvent e) {
        try {
            this.ep.setPage(this.url);
            java.net.URL urlloc = new java.net.URL("http://catalog.loc.gov/cgi-bin/Pwebrecon.cgi");
            if (this.url != null) {
                if (this.url.equals(urlloc)) {
                    java.net.URLConnection urlcon = this.url.openConnection();
                    urlcon.setDoOutput(true);
                    java.io.InputStream is = urlcon.getInputStream();
                    java.io.DataInputStream dis = new java.io.DataInputStream(is);
                    byte[] bx = new byte[10000];
                    dis.readFully(bx);
                }
            }
        } catch (Exception exp) {
            System.out.println(exp);
        }
    }
}
