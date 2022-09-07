class BackupThread extends Thread {
    public static String download(String locn, String targetDirPath) {
        try {
            URL url = new URL(locn);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            String fileName = "";
            if (url.getFile().equals("") || url.getFile().endsWith("/")) fileName = "index.html"; else {
                int index = url.getFile().lastIndexOf("/");
                if (index == -1) {
                    fileName = url.getFile();
                } else {
                    fileName = url.getFile().substring(index + 1);
                }
            }
            if (fileName.equals("")) {
                System.err.println("Couldn't figure out file name");
                return null;
            }
            File downloadedFile = new File(targetDirPath, fileName);
            FileOutputStream fos = new FileOutputStream(downloadedFile);
            byte[] b = new byte[1000];
            int len = -1;
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            fos.flush();
            fos.close();
            is.close();
            return downloadedFile.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
