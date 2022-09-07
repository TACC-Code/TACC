class BackupThread extends Thread {
    public String check(final File file, final JProgressBar p, final boolean batch, final boolean suppressDialogs) {
        MessageDigest md5;
        MessageDigest sha;
        bar = p;
        long filesize = file.length();
        if (bar != null) {
            bar.setMinimum(0);
            bar.setMaximum((int) filesize);
        }
        try {
            md5 = MessageDigest.getInstance("MD5");
            sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return Main.res.getString("digesterr");
        }
        FileInputStream fis = null;
        byte[] in = new byte[bufsize];
        int read = 0;
        already = new IntegerWrapper();
        timer = new Updater(timerms, bar, already);
        timer.start();
        try {
            fis = new FileInputStream(file);
            read = fis.read(in);
            while (read > 0) {
                already.addValue(read);
                if (read == bufsize) {
                    md5.update(in);
                    sha.update(in);
                } else {
                    md5.update(Arrays.copyOfRange(in, 0, read));
                    sha.update(Arrays.copyOfRange(in, 0, read));
                }
                if (getBreak()) {
                    fis.close();
                    timer.stop();
                    resetBarValue();
                    if (!batch) ps.println(Main.res.getString("break"));
                    return "break";
                }
                read = fis.read(in);
            }
            fis.close();
            timer.stop();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(ps);
            return Main.res.getString("fnferr");
        } catch (IOException ex) {
            ex.printStackTrace(ps);
            return Main.res.getString("ioerr");
        }
        StringBuffer res = new StringBuffer(16);
        Formatter f = new Formatter(res);
        for (byte b : md5.digest()) f.format("%02x", b);
        resultmd5 = res.toString().toLowerCase();
        res = new StringBuffer(20);
        f = new Formatter(res);
        for (byte b : sha.digest()) f.format("%02x", b);
        resultsha = res.toString().toLowerCase();
        resetBarValue();
        if (!batch) {
            ps.println(Main.res.getString("file") + ": " + file.getName() + "\n  MD-5 checksum        " + resultmd5 + "\n  SHA-1 checksum       " + resultsha);
            String checksum;
            try {
                File filemd5 = new File(file.getAbsoluteFile() + ".md5");
                FileReader fr = new FileReader(filemd5);
                LineNumberReader lnr = new LineNumberReader(fr);
                checksum = lnr.readLine();
                if (checksum == null) checksum = "";
                lnr.close();
            } catch (IOException ex) {
                return Main.res.getString("ioerr");
            }
            int spacepos = checksum.indexOf("  ");
            if (spacepos < 0) spacepos = checksum.indexOf(" *");
            if (spacepos > 0) {
                String checkname = checksum.substring(spacepos + 2, checksum.length());
                if (checkname.contains("/")) {
                    String[] tmp = checkname.split("/");
                    checkname = tmp[tmp.length - 1];
                }
                checksum = checksum.substring(0, spacepos).toLowerCase();
                if (!checkname.equals(file.getName())) userOutput(Main.res.getString("comparefilefailed"), Main.res.getString("comparetitle"), JOptionPane.WARNING_MESSAGE, suppressDialogs);
            }
            if (checksum.equals(resultmd5)) userOutput(Main.res.getString("comparesuccess.md5file"), Main.res.getString("comparetitle"), JOptionPane.INFORMATION_MESSAGE, suppressDialogs); else userOutput(Main.res.getString("comparefailed"), Main.res.getString("comparetitle"), JOptionPane.WARNING_MESSAGE, suppressDialogs);
        }
        return "ok";
    }
}
