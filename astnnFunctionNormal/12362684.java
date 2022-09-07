class BackupThread extends Thread {
    public String handlePutRequestWithResponse(final File fileForUpload, final InetSocketAddress serverAddr) throws FileNotFoundException, IOException {
        final long fileLength = fileForUpload.length();
        final ByteBuffer requestBuffer = ByteBuffer.allocate((int) fileLength + 1024);
        final String requestHeader = "PUT /databases/test3?version=5&device=Aladin HTTP/1.1\r\n" + "Host: localhost\r\n" + "Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==\r\n" + "Content-Type: application/octet-stream\r\n" + "Content-Length: " + fileLength + "\r\n" + "\r\n";
        requestBuffer.put(requestHeader.getBytes());
        final ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
        final FileInputStream streamFromFile = new FileInputStream(fileForUpload);
        try {
            final FileChannel chanFromFile = streamFromFile.getChannel();
            try {
                chanFromFile.read(requestBuffer);
                final SocketChannel chanToServer = SocketChannel.open(serverAddr);
                try {
                    chanToServer.configureBlocking(false);
                    final Selector sel = Selector.open();
                    try {
                        final SelectionKey readyForWrite = chanToServer.register(sel, SelectionKey.OP_WRITE);
                        final SelectionKey readyForRead = chanToServer.register(sel, SelectionKey.OP_READ);
                        try {
                            boolean finished = false;
                            while (!finished) {
                                sel.select();
                                final Iterator<SelectionKey> availableOperations = sel.selectedKeys().iterator();
                                while (availableOperations.hasNext()) {
                                    final SelectionKey op = availableOperations.next();
                                    availableOperations.remove();
                                    if (!op.isValid()) {
                                        continue;
                                    }
                                    if (op.isWritable()) {
                                        if (!nioHandleWrite(op, requestBuffer)) {
                                            readyForWrite.cancel();
                                        }
                                    } else if (op.isReadable()) {
                                        if (!nioHandleRead(op, responseBuffer)) {
                                            finished = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        } finally {
                            readyForRead.cancel();
                            readyForWrite.cancel();
                            chanToServer.close();
                        }
                    } finally {
                        sel.close();
                    }
                } finally {
                    chanToServer.close();
                }
            } finally {
                chanFromFile.close();
            }
        } finally {
            streamFromFile.close();
        }
        if (!responseBuffer.hasArray()) {
            Assert.fail("readBuffer has no array - needs alternate implementation");
        }
        return responseBuffer.array().toString();
    }
}
