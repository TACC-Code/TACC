class BackupThread extends Thread {
    public void load(final File config) {
        try {
            final FileInputStream fis = new FileInputStream(config);
            final Properties props = new Properties();
            props.loadFromXML(fis);
            this.name = props.getProperty("name", Fighter.defaultName);
            for (final Object key : props.keySet()) if (key instanceof Move) this.moves.add((Move) key);
        } catch (final IOException e) {
            Log.getSingleton().write("#020 Couldn't read file to load fighter: " + config.getPath());
            Log.getSingleton().write(" -> using fall back settings");
            this.name = "Fall Back";
        }
    }
}
