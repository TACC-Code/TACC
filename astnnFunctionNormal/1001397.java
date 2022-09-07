class BackupThread extends Thread {
    protected File writeTemporaryMessageFile(MimeMessage msg) throws UserMenuEntryInvocationException {
        String temp = Flap.getProperty("tempPath");
        if (temp != null) {
            File dir = new File(temp);
            if (dir.isDirectory()) {
                File f;
                do {
                    f = new File(dir, "flap-" + System.currentTimeMillis() + ".msg");
                    if (f.isFile()) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                        }
                    }
                } while (f.isFile());
                {
                    boolean folderOpen = false;
                    InputStream in = null;
                    BufferedOutputStream out = null;
                    PrintWriter outWriter = null;
                    try {
                        String lineSep = System.getProperty("line.separator", "\n");
                        byte[] buf = new byte[4096];
                        Enumeration headers = msg.getAllHeaderLines();
                        int len;
                        Flap.getFolderManager().open(msg.getFolder(), FolderManager.READ_ONLY);
                        folderOpen = true;
                        in = msg.getRawInputStream();
                        out = new BufferedOutputStream(new FileOutputStream(f));
                        outWriter = new PrintWriter(out);
                        while (headers.hasMoreElements()) {
                            outWriter.print((String) headers.nextElement());
                            outWriter.print(lineSep);
                        }
                        outWriter.print(lineSep);
                        outWriter.flush();
                        while ((len = in.read(buf, 0, buf.length)) >= 0) out.write(buf, 0, len);
                        return f;
                    } catch (IOException e) {
                        throw new UserMenuEntryInvocationException(e.getMessage());
                    } catch (MessagingException e) {
                        throw new UserMenuEntryInvocationException(e.getMessage());
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                            }
                        }
                        if (folderOpen) {
                            try {
                                Flap.getFolderManager().close(msg.getFolder(), false);
                            } catch (MessagingException e) {
                            }
                        }
                    }
                }
            } else throw new UserMenuEntryInvocationException("temporary path is a file");
        } else throw new UserMenuEntryInvocationException("no temporary path specified");
    }
}
