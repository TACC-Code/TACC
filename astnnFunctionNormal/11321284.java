class BackupThread extends Thread {
    private void bImportActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("URL is: " + this.ep.getPage());
        java.awt.Component comp[] = ep.getComponents();
        for (int i = 0; i < comp.length; i++) {
            System.out.println(comp[i].getClass());
        }
        try {
            java.net.URLConnection urlcon = this.url.openConnection();
            urlcon.setDoOutput(true);
            System.out.println(urlcon.getContentType());
            System.out.println(urlcon.getContentEncoding());
        } catch (Exception exp) {
            System.out.println(exp);
        }
    }
}
