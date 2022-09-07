class BackupThread extends Thread {
    public static String fileToString(File _file) throws FileNotFoundException, IOException {
        StringWriter writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_file)));
        char buf[] = new char[1024];
        int len = 0;
        while ((len = reader.read(buf, 0, 1024)) != -1) writer.write(buf, 0, len);
        return writer.toString();
    }
}
