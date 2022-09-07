class BackupThread extends Thread {
    TestLibrary() {
        InputStream in = TestLibrary.class.getResourceAsStream("/libTestLibrary.so");
        try {
            File tmp = File.createTempFile("libTestLibrary", "so");
            tmp.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tmp);
            while (in.available() > 0) {
                out.write(in.read());
            }
            in.close();
            out.close();
            Runtime.getRuntime().load(tmp.getAbsolutePath());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
}
