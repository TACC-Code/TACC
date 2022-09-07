class BackupThread extends Thread {
    public static void writeToFile(InputStream is, File file) {
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            byte[] b = new byte[1024];
            int r = 0;
            boolean again = true;
            while (again) {
                if ((r = is.read(b)) > -1) out.write(b, 0, r); else again = false;
            }
            is.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Error Writing/Reading Streams.");
        }
    }
}
