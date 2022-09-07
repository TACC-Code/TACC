class BackupThread extends Thread {
    public static void main(String[] args) {
        SPKWriter spw = new DirectoryToSPK(112233332244l);
        try {
            spw.writeSPK(".", null, new File("out.spk"));
            SPKReader reader = new SPKReader(new File("out.spk").toURI().toURL(), 0x1A21A0BE14l);
            byte[] buf = new byte[65536];
            for (String file : reader.files()) {
                FileData data = reader.getFileData(file);
                System.out.println("EXTRACT " + file + " " + data.getPos() + " " + data.getLength());
                File outf = new File("out" + file);
                outf.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(outf);
                InputStream is = reader.getDecStream(file);
                int read;
                while ((read = is.read(buf)) >= 0) {
                    if (read == 0) {
                        continue;
                    }
                    fos.write(buf, 0, read);
                }
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
