class BackupThread extends Thread {
    public void run() {
        while (!stopped) {
            try {
                if (session.getShellOutputReader().ready()) {
                    readChars = session.getShellOutputReader().read(resultBuffer, 0, resultBufferSize);
                    if (readChars > 0 && !stopped) {
                        connection.getResultWriter().write(resultBuffer, 0, readChars);
                        connection.getResultWriter().flush();
                    } else {
                        stopped = true;
                        break;
                    }
                } else {
                    Thread.sleep(1);
                }
            } catch (Exception e) {
                stopped = true;
                break;
            }
        }
        try {
            synchronized (session.getShell()) {
                session.getShell().notify();
            }
        } catch (Exception e) {
        }
    }
}
