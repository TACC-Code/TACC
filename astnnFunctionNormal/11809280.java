class BackupThread extends Thread {
    public void run() {
        while (running) {
            try {
                if (in.ready()) {
                    readChars = in.read(resultBuffer, 0, resultBufferSize);
                    if (readChars > 0 && running) {
                        if (managed) {
                            out.write(resultBuffer, 0, readChars);
                            out.flush();
                        }
                    } else {
                        running = false;
                        break;
                    }
                } else {
                    Thread.sleep(1);
                }
            } catch (Exception e) {
                running = false;
                break;
            }
        }
    }
}
