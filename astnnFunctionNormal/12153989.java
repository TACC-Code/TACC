class BackupThread extends Thread {
                    @Override
                    public Object run() throws IOException {
                        java.io.InputStream in = fCodebase.openStream();
                        JarArchive jar = new JarArchive(in);
                        Archive.Entry ae[] = jar.entries();
                        ClassName[] tab = new ClassName[ae.length];
                        for (int i = 0; i < ae.length; i++) {
                            tab[i] = new ClassName(ae[i].name(), DigestTable.toByteArray(ae[i].digest()));
                        }
                        in.close();
                        return tab;
                    }
}
