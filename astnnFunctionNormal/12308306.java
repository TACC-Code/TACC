class BackupThread extends Thread {
    public void saveMemory(int start, int end) {
        File file = showSaveDialog("Save Memory");
        if (file != null) {
            try {
                FileOutputStream io = new FileOutputStream(file);
                try {
                    Memory mem = computer.getMemory();
                    for (int addr = start; addr <= end; addr++) io.write(mem.readByte(addr));
                } finally {
                    io.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
