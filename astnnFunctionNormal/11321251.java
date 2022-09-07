class BackupThread extends Thread {
    private void loop() {
        while (m_running) {
            try {
                SocketReadData data;
                try {
                    data = m_reader.take();
                } catch (final InterruptedException e) {
                    continue;
                }
                if (data == null || !m_running) continue;
                if (s_logger.isLoggable(Level.FINEST)) {
                    s_logger.finest("Decoding packet:" + data);
                }
                final ByteArrayInputStream stream = new ByteArrayInputStream(data.getData());
                try {
                    final MessageHeader header = readMessageHeader(data.getChannel(), m_objectStreamFactory.create(stream));
                    if (s_logger.isLoggable(Level.FINEST)) {
                        s_logger.log(Level.FINEST, "header decoded:" + header);
                    }
                    final Socket s = data.getChannel().socket();
                    if (!m_running || s == null || s.isInputShutdown()) continue;
                    final QuarantineConversation converstation = m_quarantine.get(data.getChannel());
                    if (converstation != null) {
                        sendQuarantine(data.getChannel(), converstation, header);
                    } else {
                        if (m_nioSocket.getLocalNode() == null) throw new IllegalStateException("we are writing messages, but no local node");
                        if (header.getFrom() == null) throw new IllegalArgumentException("Null from:" + header);
                        if (s_logger.isLoggable(Level.FINER)) {
                            s_logger.log(Level.FINER, "decoded  msg:" + header.getMessage() + " size:" + data.size());
                        }
                        m_nioSocket.messageReceived(header, data.getChannel());
                    }
                } catch (final Exception ioe) {
                    s_logger.log(Level.SEVERE, "error reading object", ioe);
                    m_errorReporter.error(data.getChannel(), ioe);
                }
            } catch (final Exception e) {
                s_logger.log(Level.WARNING, "error in decoder", e);
            }
        }
    }
}
