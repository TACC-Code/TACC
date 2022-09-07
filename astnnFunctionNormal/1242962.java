class BackupThread extends Thread {
    public static String DownloadActivePath() {
        try {
            URL url = new URL("http://dl.dropbox.com/u/27675084/ActivePath.txt");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/hQuran/ActivePath.txt");
            file.deleteOnExit();
            long startTime = System.currentTimeMillis();
            Log.d("ImageManager", "downloaded file name:" + "ActivePath.txt");
            URLConnection ucon = url.openConnection();
            int lenghtOfFile = ucon.getContentLength();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = ucon.getInputStream();
            byte data[] = new byte[1024];
            int count = 0;
            long total = 0;
            int progress = 0;
            while ((count = is.read(data)) != -1) {
                total += count;
                int progress_temp = (int) total * 100 / lenghtOfFile;
                if (progress_temp % 10 == 0 && progress != progress_temp) {
                    progress = progress_temp;
                    Log.v("Downloading", "total = " + progress);
                }
                fos.write(data, 0, count);
            }
            is.close();
            fos.close();
            Log.d("ImageManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
            return "";
        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
            return e.getMessage();
        }
    }
}
