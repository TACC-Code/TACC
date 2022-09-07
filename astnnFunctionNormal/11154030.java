class BackupThread extends Thread {
    public void download(URL url, File targetFile) throws Exception {
        if (targetFile == null) {
            return;
        }
        System.out.println("DU download url==" + url.toString() + "==");
        System.out.println("DU download targetFile==" + targetFile.getAbsolutePath() + "==");
        URLConnection urlConnection = url.openConnection();
        System.out.println("DU download 3");
        DataInputStream in = new DataInputStream(urlConnection.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(targetFile));
        int data = in.read();
        while (data != -1) {
            out.write(data);
            data = in.read();
        }
        out.flush();
        in.close();
        out.close();
    }
}
