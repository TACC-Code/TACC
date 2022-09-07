class BackupThread extends Thread {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            PrintWriter out = resp.getWriter();
            resp.setHeader("Content-type", "text/html");
            out.write("<html><head>");
            out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"capture.css\" />");
            out.write("</head><body>");
            String capFileName = req.getRequestURI().substring(req.getContextPath().length() + 1);
            String rawFileName = capFileName.substring(0, capFileName.length() - 4) + ".raw";
            File capFile = new File(getServletContext().getRealPath(capFileName));
            File rawFile = new File(getServletContext().getRealPath(rawFileName));
            FileInputStream capFis = new FileInputStream(capFile);
            FileInputStream rawFis = new FileInputStream(rawFile);
            FileChannel capChannel = capFis.getChannel();
            FileChannel rawChannel = rawFis.getChannel();
            MappedByteBuffer capMappedFile, rawMappedFile;
            try {
                capMappedFile = capChannel.map(FileChannel.MapMode.READ_ONLY, 0, capChannel.size());
                rawMappedFile = rawChannel.map(FileChannel.MapMode.READ_ONLY, 0, rawChannel.size());
            } catch (IOException e) {
                log.error("error mapping file", e);
                return;
            }
            capMappedFile.order(ByteOrder.BIG_ENDIAN);
            rawMappedFile.order(ByteOrder.BIG_ENDIAN);
            ByteBuffer cap = ByteBuffer.wrap(capMappedFile);
            ByteBuffer in = ByteBuffer.wrap(rawMappedFile);
            boolean serverMode = (cap.get() == (byte) 0x01);
            out.write("Mode: " + (serverMode ? "UPSTREAM" : "DOWNSTREAM"));
            RTMP state = new RTMP(serverMode);
            int id = 0;
            try {
                int nextTimePos = 0;
                long time = 0;
                long offset = -1;
                long read = 0;
                try {
                    while (true) {
                        while (in.position() >= nextTimePos) {
                            if (cap.remaining() >= 12) {
                                time = cap.getLong();
                                if (offset == -1) {
                                    offset = time;
                                }
                                time -= offset;
                                read = cap.getInt();
                                nextTimePos += read;
                                out.write("<div class=\"time\">TIME: " + time + " READ: " + read + "</div>");
                            }
                        }
                        final int remaining = in.remaining();
                        if (state.canStartDecoding(remaining)) {
                            state.startDecoding();
                        } else {
                            break;
                        }
                        final Object decodedObject = decoder.decode(state, in);
                        if (state.hasDecodedObject()) {
                            if (decodedObject instanceof Packet) {
                                out.write(formatHTML((Packet) decodedObject, id++, 0));
                            } else if (decodedObject instanceof ByteBuffer) {
                                ByteBuffer buf = (ByteBuffer) decodedObject;
                                out.write("<div class=\"handshake\"><pre>" + HexDump.formatHexDump(buf.getHexDump()) + "</pre></div>");
                            }
                        } else if (state.canContinueDecoding()) {
                            continue;
                        } else {
                            break;
                        }
                        if (!in.hasRemaining()) {
                            break;
                        }
                    }
                } catch (ProtocolException pvx) {
                    log.error("Error decoding buffer", pvx);
                } catch (Exception ex) {
                    log.error("Error decoding buffer", ex);
                } finally {
                }
            } catch (RuntimeException e) {
                log.error("Error", e);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
