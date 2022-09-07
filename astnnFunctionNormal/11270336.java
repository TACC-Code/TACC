class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        JizzAudioSource src = new JizzAudioSource() {

            public String getContributor() {
                return "Contributor";
            }

            public String getArtist() {
                return "Artist";
            }

            public String getAlbum() {
                return "Album";
            }

            public String getTitle() {
                return "Title";
            }

            public AudioInputStream getInputStream() throws IOException, UnsupportedAudioFileException {
                return AudioSystem.getAudioInputStream(new java.io.File("input.mp3"));
            }
        };
        JizzAudioInputStream in = JizzAudioSystem.convertToMp3(src, 64);
        java.io.OutputStream out = new java.io.FileOutputStream("output.jizz");
        byte[] buf = new byte[4 * 1024];
        int read = -1;
        while ((read = in.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
        out.close();
        in.close();
    }
}
