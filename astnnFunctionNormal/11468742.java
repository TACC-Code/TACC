class BackupThread extends Thread {
    public static QPixmap loadPluginPixmap(PluginImpl plugin, String path) {
        try {
            InputStream in = plugin.getClass().getClassLoader().getResourceAsStream(path);
            int idx = path.lastIndexOf('.');
            String ext = "";
            if (idx >= 0 && idx < path.length() - 2) {
                ext = path.substring(idx + 1);
            }
            File f = File.createTempFile("aura_plugin_extract", ext);
            FileOutputStream out = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int read = in.read(buffer);
            while (read > 0) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            out.close();
            QPixmap pix = new QPixmap(f.getAbsolutePath());
            f.delete();
            return pix;
        } catch (IOException ex) {
            return null;
        }
    }
}
