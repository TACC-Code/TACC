class BackupThread extends Thread {
    public void DownloadFromUrL(String imageURL, String fileName) {
        try {
            long startTime = System.currentTimeMillis();
            URL url = new URL("http://fffmusi99zq1.webs.com/" + imageURL);
            File file = new File("/sdcard/download/" + fileName);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Toast.makeText(getApplicationContext(), "Hooray! Resource files" + " downloaded!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "This app needs Internet accesss to" + " download resource files. Duh.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
