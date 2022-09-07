class BackupThread extends Thread {
    public void download(String command) throws IOException {
        SocketChannel sc = null;
        boolean append = false;
        if (panel.ftpSession.passive) {
            sc = doPASV(command);
        } else {
            sc = acceptConnection(command);
        }
        Date d1 = new Date();
        if (sc == null) {
            return;
        }
        if (rest > 0) {
            append = true;
        }
        FileOutputStream fos = new FileOutputStream(new File(panel.otherPanel.dir + File.separator + command.substring(5, command.length())), append);
        FileChannel fc = fos.getChannel();
        int amount;
        dbuf.clear();
        long size = rest;
        Date d3 = new Date();
        Date d4;
        long diffSize = 0;
        long diffTime = 0;
        try {
            while ((amount = sc.read(dbuf)) != -1 && !panel.frame.tm.abort) {
                d4 = new Date();
                size += amount;
                diffSize += amount;
                if ((diffTime = d4.getTime() - d3.getTime()) > 1000) {
                    panel.frame.pBar.setValue((int) (size * 100 / this.size));
                    String perf = Utilities.humanReadable(size) + "@" + Utilities.humanReadable(diffSize / (diffTime / 1000.0)) + "/s";
                    panel.frame.statusLabel.setText(perf);
                    panel.frame.setTitle(perf);
                    diffSize = 0;
                    diffTime = 0;
                    d3 = new Date();
                }
                dbuf.flip();
                fc.write(dbuf);
                dbuf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        panel.frame.pBar.setValue(0);
        panel.frame.statusLabel.setText("");
        panel.frame.setTitle("wlFxp");
        fc.force(true);
        fos.close();
        gThread.ret = 0;
        sc.close();
        Date d2 = new Date();
        getTransferRate(d2.getTime() - d1.getTime(), size);
    }
}
