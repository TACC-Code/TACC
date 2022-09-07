class BackupThread extends Thread {
    public void run() {
        try {
            newLine = con.getCRLF();
            if (Settings.getFtpPasvMode()) {
                try {
                    sock = new Socket(host, port);
                    sock.setSoTimeout(Settings.getSocketTimeout());
                } catch (Exception ex) {
                    ok = false;
                    debug("Can't open Socket on port " + port);
                }
            } else {
                try {
                    ssock = new ServerSocket(port);
                } catch (Exception ex) {
                    ok = false;
                    Log.debug("Can't open ServerSocket on port " + port);
                }
            }
        } catch (Exception ex) {
            debug(ex.toString());
        }
        isThere = true;
        boolean ok = true;
        RandomAccessFile fOut = null;
        BufferedOutputStream bOut = null;
        RandomAccessFile fIn = null;
        try {
            if (!Settings.getFtpPasvMode()) {
                int retry = 0;
                while ((retry++ < 5) && (sock == null)) {
                    try {
                        ssock.setSoTimeout(Settings.connectionTimeout);
                        sock = ssock.accept();
                    } catch (IOException e) {
                        sock = null;
                        debug("Got IOException while trying to open a socket!");
                        if (retry == 5) {
                            debug("Connection failed, tried 5 times - maybe try a higher timeout in Settings.java...");
                        }
                        finished = true;
                        throw e;
                    } finally {
                        ssock.close();
                    }
                    debug("Attempt timed out, retrying...");
                }
            }
            if (ok) {
                byte[] buf = new byte[Settings.bufferSize];
                start = System.currentTimeMillis();
                int buflen = 0;
                if (type.equals(GET) || type.equals(GETDIR)) {
                    if (!justStream) {
                        try {
                            if (resume) {
                                File f = new File(file);
                                fOut = new RandomAccessFile(file, "rw");
                                fOut.skipBytes((int) f.length());
                                buflen = (int) f.length();
                            } else {
                                if (localfile == null) {
                                    localfile = file;
                                }
                                File f2 = new File(Settings.appHomeDir);
                                f2.mkdirs();
                                File f = new File(localfile);
                                if (f.exists()) {
                                    f.delete();
                                }
                                bOut = new BufferedOutputStream(new FileOutputStream(localfile), Settings.bufferSize);
                            }
                        } catch (Exception ex) {
                            debug("Can't create outputfile: " + file);
                            ok = false;
                            ex.printStackTrace();
                        }
                    }
                    if (ok) {
                        try {
                            in = new BufferedInputStream(sock.getInputStream(), Settings.bufferSize);
                            if (justStream) {
                                return;
                            }
                        } catch (Exception ex) {
                            ok = false;
                            debug("Can't get InputStream");
                        }
                        if (ok) {
                            try {
                                int len = buflen;
                                if (fOut != null) {
                                    while (true) {
                                        int read = -2;
                                        try {
                                            read = in.read(buf);
                                        } catch (IOException es) {
                                            Log.out("got a IOException");
                                            ok = false;
                                            fOut.close();
                                            finished = true;
                                            con.fireProgressUpdate(file, FAILED, -1);
                                            Log.out("last read: " + read + ", len: " + (len + read));
                                            es.printStackTrace();
                                            return;
                                        }
                                        len += read;
                                        if (read == -1) {
                                            break;
                                        }
                                        if (newLine != null) {
                                            byte[] buf2 = modifyGet(buf, read);
                                            fOut.write(buf2, 0, buf2.length);
                                        } else {
                                            fOut.write(buf, 0, read);
                                        }
                                        con.fireProgressUpdate(file, type, len);
                                        if (time()) {
                                        }
                                        if (read == StreamTokenizer.TT_EOF) {
                                            break;
                                        }
                                    }
                                } else {
                                    while (true) {
                                        int read = -2;
                                        try {
                                            read = in.read(buf);
                                        } catch (IOException es) {
                                            Log.out("got a IOException");
                                            ok = false;
                                            bOut.close();
                                            finished = true;
                                            con.fireProgressUpdate(file, FAILED, -1);
                                            Log.out("last read: " + read + ", len: " + (len + read));
                                            es.printStackTrace();
                                            return;
                                        }
                                        len += read;
                                        if (read == -1) {
                                            break;
                                        }
                                        if (newLine != null) {
                                            byte[] buf2 = modifyGet(buf, read);
                                            bOut.write(buf2, 0, buf2.length);
                                        } else {
                                            bOut.write(buf, 0, read);
                                        }
                                        con.fireProgressUpdate(file, type, len);
                                        if (time()) {
                                        }
                                        if (read == StreamTokenizer.TT_EOF) {
                                            break;
                                        }
                                    }
                                    bOut.flush();
                                }
                            } catch (IOException ex) {
                                ok = false;
                                debug("Old connection removed");
                                con.fireProgressUpdate(file, FAILED, -1);
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                if (type.equals(PUT) || type.equals(PUTDIR)) {
                    if (in == null) {
                        try {
                            fIn = new RandomAccessFile(file, "r");
                            if (resume) {
                                fIn.skipBytes(skiplen);
                            }
                        } catch (Exception ex) {
                            debug("Can't open inputfile: " + " (" + ex + ")");
                            ok = false;
                        }
                    }
                    if (ok) {
                        try {
                            out = new BufferedOutputStream(sock.getOutputStream());
                        } catch (Exception ex) {
                            ok = false;
                            debug("Can't get OutputStream");
                        }
                        if (ok) {
                            try {
                                int len = skiplen;
                                char b;
                                while (true) {
                                    int read;
                                    if (in != null) {
                                        read = in.read(buf);
                                    } else {
                                        read = fIn.read(buf);
                                    }
                                    len += read;
                                    if (read == -1) {
                                        break;
                                    }
                                    if (newLine != null) {
                                        byte[] buf2 = modifyPut(buf, read);
                                        out.write(buf2, 0, buf2.length);
                                    } else {
                                        out.write(buf, 0, read);
                                    }
                                    con.fireProgressUpdate(file, type, len);
                                    if (time()) {
                                    }
                                    if (read == StreamTokenizer.TT_EOF) {
                                        break;
                                    }
                                }
                                out.flush();
                            } catch (IOException ex) {
                                ok = false;
                                debug("Error: Data connection closed.");
                                con.fireProgressUpdate(file, FAILED, -1);
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Log.debug("Can't connect socket to ServerSocket");
            ex.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (bOut != null) {
                    bOut.flush();
                    bOut.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (in != null && !justStream) {
                    in.close();
                }
                if (fIn != null) {
                    fIn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            sock.close();
        } catch (Exception ex) {
            debug(ex.toString());
        }
        if (!Settings.getFtpPasvMode()) {
            try {
                ssock.close();
            } catch (Exception ex) {
                debug(ex.toString());
            }
        }
        finished = true;
        if (ok) {
            con.fireProgressUpdate(file, FINISHED, -1);
        } else {
            con.fireProgressUpdate(file, FAILED, -1);
        }
    }
}
