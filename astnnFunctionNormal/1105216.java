class BackupThread extends Thread {
    public static String source(URL url) throws GLException {
        try {
            final InputStream in = url.openStream();
            final StringBuilder sb = new StringBuilder();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            in.close();
            return sb.toString();
        } catch (IOException e) {
            throw new GLException(String.format("Could not read resource: %s", url.toString()));
        }
    }
}
