class BackupThread extends Thread {
        public LoadRunner(String pathToFile, IceStyledDocument document) throws IOException {
            super();
            FileInputStream input = new FileInputStream(pathToFile);
            FileChannel channel = input.getChannel();
            int fileLength = (int) channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
            this.data = new byte[buffer.limit()];
            buffer.get(this.data);
            buffer.clear();
            buffer = null;
            this.document = document;
        }
}
