class BackupThread extends Thread {
    public void download() throws MalformedURLException, IOException {
        String destination = "config" + File.separator + "servers" + File.separator + serverFile;
        if (ftpServer != null && destination != null) {
            StringBuffer sb = new StringBuffer("ftp://");
            if (user != null && password != null) {
                sb.append(user);
                sb.append(':');
                sb.append(password);
                sb.append('@');
            }
            sb.append(ftpServer);
            sb.append('/');
            sb.append("cockfight/" + serverFile);
            sb.append(";type=i");
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                URL url = new URL(sb.toString());
                URLConnection urlc = url.openConnection();
                bis = new BufferedInputStream(urlc.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(new File(destination)));
                int i;
                while ((i = bis.read()) != -1) {
                    bos.write(i);
                }
            } finally {
                if (bis != null) try {
                    bis.close();
                } catch (IOException ioe) {
                    error();
                }
                if (bos != null) try {
                    bos.close();
                } catch (IOException ioe) {
                    error();
                }
            }
        } else {
            System.out.println("Input not available");
        }
    }
}
