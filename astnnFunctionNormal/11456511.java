class BackupThread extends Thread {
        public FileWriter(File f) throws IOException {
            _pipe = Pipe.open();
            _out = new FileOutputStream(f).getChannel();
            _source = _pipe.source();
        }
}
