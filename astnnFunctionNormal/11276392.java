class BackupThread extends Thread {
        @Override
        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1) sw.write(c);
            } catch (IOException e) {
            }
        }
}
