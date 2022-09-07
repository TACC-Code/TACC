class BackupThread extends Thread {
    public static String DownloadTareefFromUrl(String strActivePath, String imgName, String fileName) {
        try {
            URL url = new URL(strActivePath + "/taareef/" + imgName + ".html");
            File file = new File(fileName);
            long startTime = System.currentTimeMillis();
            Log.d("ImageManager", "downloaded file name:" + fileName);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, 8192);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("ImageManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
            return "";
        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
            return e.getMessage();
        }
    }
}
