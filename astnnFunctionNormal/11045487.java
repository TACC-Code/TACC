class BackupThread extends Thread {
    public void run() {
        try {
            osf = new RandomAccessFile(fileName, "rw");
            URL url = new URL(urlt);
            HttpURLConnection http2 = (HttpURLConnection) url.openConnection();
            http2.setRequestProperty("User-Agent", "NetFox");
            http2.setRequestProperty("RANGE", "bytes=" + startl + "-");
            osf.seek(startl);
            InputStream input = http2.getInputStream();
            byte b[] = new byte[1024];
            Date d = new Date();
            int l;
            int i;
            l = 0;
            System.out.println(this.getName() + " 开始下载。。。");
            while ((i = input.read(b, 0, 1024)) != -1 && l < end) {
                osf.write(b, 0, i);
                b = new byte[1024];
                l += i;
            }
            Date d2 = new Date();
            System.out.println(this.getName() + " 线程耗时： " + (d2.getTime() - d.getTime()) / 1000 + " 秒,实际共下载：" + l + "字节");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
