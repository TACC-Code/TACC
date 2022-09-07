class BackupThread extends Thread {
    private static String getProviderName(URL url) throws ConfigurationException, IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String result = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                int commentPos = line.indexOf('#');
                if (commentPos >= 0) {
                    line = line.substring(0, commentPos);
                }
                line = line.trim();
                int len = line.length();
                if (len != 0) {
                    if (result != null) {
                        throw new ConfigurationException("resource specifies multiple providers");
                    }
                    result = line;
                }
            }
            if (result == null) {
                throw new ConfigurationException("resource specifies no providers");
            }
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
