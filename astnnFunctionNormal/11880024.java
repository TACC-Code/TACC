class BackupThread extends Thread {
    private boolean downFile(byte[] buf, FileInfo info, String ip) {
        try {
            showStep("正在下载文件：" + info.getRemoteName());
            URL url = new URL(fileUri.replace("{path}", info.getRemotePath()).replace("{file}", info.getRemoteName()).replace("{newVer}", info.getVersion()).replace("{oldVer}", currVer).replace("{ip}", ip));
            URLConnection con = url.openConnection();
            java.io.File file = new java.io.File("tmp", info.getLocalName1());
            if (!file.exists() || !file.isFile()) {
                file.createNewFile();
            }
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            java.io.BufferedInputStream bis = new java.io.BufferedInputStream(con.getInputStream());
            int len = bis.read(buf, 0, buf.length);
            while (len >= 0) {
                fos.write(buf, 0, len);
                len = bis.read(buf, 0, buf.length);
            }
            bis.close();
            fos.close();
            return true;
        } catch (Exception exp) {
            javax.swing.JOptionPane.showMessageDialog(this, exp.getLocalizedMessage());
            showStep("文件 " + info.getRemoteName() + " 下载失败！");
            return false;
        }
    }
}
