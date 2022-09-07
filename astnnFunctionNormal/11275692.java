class BackupThread extends Thread {
    public static FileIO open(String path, int mode) throws FileNotFoundException {
        if (path.toLowerCase().startsWith("http://")) {
            try {
                URL url = new URL(path);
                URLConnection urlConn = url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setUseCaches(true);
                byte[] b = null;
                long size = 0;
                InputStream is = urlConn.getInputStream();
                ByteArrayOutputStream os;
                if (path.toLowerCase().endsWith(".zip")) {
                    ZipInputStream zis = new ZipInputStream(is);
                    ZipEntry entry = zis.getNextEntry();
                    is = zis;
                    os = new MyByteArrayOutputStream((int) entry.getSize());
                    b = ((MyByteArrayOutputStream) os).getBuf();
                    size = entry.getSize();
                } else {
                    size = urlConn.getContentLength();
                    os = new ByteArrayOutputStream();
                }
                int read;
                byte[] buffer = new byte[8096];
                String msg = "Downloading " + path.substring(path.lastIndexOf('/') + 1);
                Main.showProgress(msg, 0);
                long completed = 0;
                while (true) {
                    read = is.read(buffer);
                    if (read <= 0) break;
                    os.write(buffer, 0, read);
                    completed += read;
                    Main.showProgress(msg, (int) (completed * 100 / size));
                }
                is.close();
                if (b == null) b = os.toByteArray();
                return new RamIO(b, mode);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Main.showProgress(null, 0);
            }
            throw new FileNotFoundException(path);
        } else if (path.toLowerCase().startsWith("jar://")) {
            path = path.substring(6);
            InputStream is = Dosbox.class.getResourceAsStream(path);
            if (is == null) {
                return null;
            }
            try {
                is.close();
            } catch (Exception e) {
            }
            return new JarIO(path, mode);
        } else {
            path = FileHelper.resolve_path(path);
            File f = new File(path);
            String m = "r";
            if ((mode & MODE_WRITE) != 0) m += "w";
            if ((mode & MODE_TRUNCATE) != 0) {
                if (f.exists()) f.delete();
            } else if (!f.exists()) {
                throw new FileNotFoundException();
            }
            return new RandomIO(f, m);
        }
    }
}
