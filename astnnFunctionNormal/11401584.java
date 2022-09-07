class BackupThread extends Thread {
    protected void setURL(URL jarurl) {
        this.url = jarurl;
        try {
            if (this.url.toString().toLowerCase().startsWith("jar:")) {
                File tempFile = File.createTempFile("SBSTUTORIAL", String.valueOf(System.currentTimeMillis()));
                tempFile.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempFile);
                URLConnection uc = this.url.openConnection();
                InputStream is = uc.getInputStream();
                byte[] buf = new byte[1024];
                int i = 0;
                while ((i = is.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
                is.close();
                fos.flush();
                fos.close();
                this.url = tempFile.toURI().toURL();
            }
            if (!url.toString().toLowerCase().startsWith("jar:")) {
                this.url = new URL("jar", "", -1, this.url.toString() + "!/");
            }
            URLConnection uc = this.url.openConnection();
            jar = ((JarURLConnection) uc).getJarFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
