class BackupThread extends Thread {
    private static void downloadFile(String addr) {
        try {
            String filename, ext, path;
            path = (String) vals.get("dir");
            filename = addr.substring(addr.lastIndexOf('/') + 1, addr.lastIndexOf('.'));
            ext = addr.substring(addr.lastIndexOf('.'));
            URL url = new URL(addr);
            URLConnection con = url.openConnection();
            long length = con.getContentLength();
            InputStream in = con.getInputStream();
            File file = new File(path + File.separator + filename + ext);
            if (file.length() == length) {
                in.close();
                return;
            }
            for (int x = 0; file.exists(); x++) {
                file = new File(path + File.separator + filename + String.valueOf(x + 1) + ext);
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            append("Downloading file " + file.toString() + "\n");
            int read;
            double percent;
            long count = 0;
            while ((read = in.read()) != -1) {
                if (bar != null && length != -1 && (count % 1024) == 0) {
                    count += read;
                    percent = (double) count / (double) length;
                    bar.setValue((int) (percent * 100));
                }
                out.write(read);
                count++;
            }
            if (bar != null) {
                bar.setValue(100);
            }
            out.close();
            in.close();
        } catch (Exception e) {
        }
    }
}
