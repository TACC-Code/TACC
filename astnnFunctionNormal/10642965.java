class BackupThread extends Thread {
    private VFS() {
        List factoryList = new ArrayList();
        BufferedReader reader = null;
        try {
            URL url = getClass().getResource(FILE_SYSTEMS);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.length() > 0 && !line.startsWith("#")) {
                    try {
                        FileFactory factory = (FileFactory) Class.forName(line).newInstance();
                        factory.setFileSystemManager(this);
                        factoryList.add(0, factory);
                    } catch (Exception e) {
                        System.err.println("Failed to initialize class : " + line);
                        e.printStackTrace();
                    }
                }
            }
            factories = new FileFactory[factoryList.size()];
            factories = (FileFactory[]) factoryList.toArray(factories);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
