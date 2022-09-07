class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        OwlConverter processor = new OwlConverter();
        if (args.length > 0) {
            for (String file : args) {
                InputStream newStream = processor.getStream(file, new FileInputStream(file));
                IOUtil.writeFile(IOUtil.stripExtension(file) + "entries.xml", IOUtil.readInputStream(newStream));
            }
            return;
        }
        processor.convertSweetAll();
    }
}
