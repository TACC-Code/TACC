class BackupThread extends Thread {
    public static String DownloadFile(String _url, String _out_path) {
        CheckAppFolder();
        String file_name = new File(_url).getName();
        String out_file = _out_path + "/" + file_name;
        if (new File(out_file).exists()) return out_file;
        try {
            if (!new File(_out_path + "/" + file_name).isFile()) {
                URL myFileUrl = new URL(_url);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                int length = conn.getContentLength();
                byte[] buffer = new byte[length];
                InputStream is = conn.getInputStream();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out_file, false));
                int readed = 0;
                int current_read = 0;
                while (readed < length) {
                    current_read = is.read(buffer);
                    readed += current_read;
                    bos.write(buffer, 0, current_read);
                }
                is.close();
                bos.close();
                conn.disconnect();
            }
        } catch (IOException e) {
            Log.e("ouned", "Could not write file " + e.getMessage());
            out_file = "";
        }
        return out_file;
    }
}
