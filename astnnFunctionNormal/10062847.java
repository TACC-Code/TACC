class BackupThread extends Thread {
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, State state) throws Exception {
        switch(state) {
            case SKIP_CONTROL_CHARS:
                {
                    try {
                        skipControlCharacters(buffer);
                        checkpoint(State.READ_INITIAL);
                    } finally {
                        checkpoint();
                    }
                }
            case READ_INITIAL:
                {
                    String[] initialLine = splitInitialLine(readLine(buffer, maxInitialLineLength));
                    if (initialLine.length < 3) {
                        checkpoint(State.SKIP_CONTROL_CHARS);
                        return null;
                    }
                    message = createMessage(initialLine);
                    checkpoint(State.READ_HEADER);
                }
            case READ_HEADER:
                {
                    State nextState = readHeaders(buffer);
                    checkpoint(nextState);
                    if (nextState == State.READ_CHUNK_SIZE) {
                        message.setChunked(true);
                        return message;
                    } else if (nextState == State.SKIP_CONTROL_CHARS) {
                        message.removeHeader(HttpHeaders.Names.TRANSFER_ENCODING);
                        return message;
                    } else {
                        long contentLength = HttpHeaders.getContentLength(message, -1);
                        if (contentLength == 0 || contentLength == -1 && isDecodingRequest()) {
                            content = ChannelBuffers.EMPTY_BUFFER;
                            return reset();
                        }
                        switch(nextState) {
                            case READ_FIXED_LENGTH_CONTENT:
                                if (contentLength > maxChunkSize || HttpHeaders.is100ContinueExpected(message)) {
                                    checkpoint(State.READ_FIXED_LENGTH_CONTENT_AS_CHUNKS);
                                    message.setChunked(true);
                                    chunkSize = HttpHeaders.getContentLength(message, -1);
                                    return message;
                                }
                                break;
                            case READ_VARIABLE_LENGTH_CONTENT:
                                if (buffer.readableBytes() > maxChunkSize || HttpHeaders.is100ContinueExpected(message)) {
                                    checkpoint(State.READ_VARIABLE_LENGTH_CONTENT_AS_CHUNKS);
                                    message.setChunked(true);
                                    return message;
                                }
                                break;
                            default:
                                throw new IllegalStateException("Unexpected state: " + nextState);
                        }
                    }
                    return null;
                }
            case READ_VARIABLE_LENGTH_CONTENT:
                {
                    if (content == null) {
                        content = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
                    }
                    content.writeBytes(buffer.readBytes(buffer.readableBytes()));
                    return reset();
                }
            case READ_VARIABLE_LENGTH_CONTENT_AS_CHUNKS:
                {
                    int chunkSize = Math.min(maxChunkSize, buffer.readableBytes());
                    HttpChunk chunk = new DefaultHttpChunk(buffer.readBytes(chunkSize));
                    if (!buffer.readable()) {
                        reset();
                        if (!chunk.isLast()) {
                            return new Object[] { chunk, HttpChunk.LAST_CHUNK };
                        }
                    }
                    return chunk;
                }
            case READ_FIXED_LENGTH_CONTENT:
                {
                    readFixedLengthContent(buffer);
                    return reset();
                }
            case READ_FIXED_LENGTH_CONTENT_AS_CHUNKS:
                {
                    long chunkSize = this.chunkSize;
                    HttpChunk chunk;
                    if (chunkSize > maxChunkSize) {
                        chunk = new DefaultHttpChunk(buffer.readBytes(maxChunkSize));
                        chunkSize -= maxChunkSize;
                    } else {
                        assert chunkSize <= Integer.MAX_VALUE;
                        chunk = new DefaultHttpChunk(buffer.readBytes((int) chunkSize));
                        chunkSize = 0;
                    }
                    this.chunkSize = chunkSize;
                    if (chunkSize == 0) {
                        reset();
                        if (!chunk.isLast()) {
                            return new Object[] { chunk, HttpChunk.LAST_CHUNK };
                        }
                    }
                    return chunk;
                }
            case READ_CHUNK_SIZE:
                {
                    String line = readLine(buffer, maxInitialLineLength);
                    int chunkSize = getChunkSize(line);
                    this.chunkSize = chunkSize;
                    if (chunkSize == 0) {
                        checkpoint(State.READ_CHUNK_FOOTER);
                        return null;
                    } else if (chunkSize > maxChunkSize) {
                        checkpoint(State.READ_CHUNKED_CONTENT_AS_CHUNKS);
                    } else {
                        checkpoint(State.READ_CHUNKED_CONTENT);
                    }
                }
            case READ_CHUNKED_CONTENT:
                {
                    assert chunkSize <= Integer.MAX_VALUE;
                    HttpChunk chunk = new DefaultHttpChunk(buffer.readBytes((int) chunkSize));
                    checkpoint(State.READ_CHUNK_DELIMITER);
                    return chunk;
                }
            case READ_CHUNKED_CONTENT_AS_CHUNKS:
                {
                    long chunkSize = this.chunkSize;
                    HttpChunk chunk;
                    if (chunkSize > maxChunkSize) {
                        chunk = new DefaultHttpChunk(buffer.readBytes(maxChunkSize));
                        chunkSize -= maxChunkSize;
                    } else {
                        assert chunkSize <= Integer.MAX_VALUE;
                        chunk = new DefaultHttpChunk(buffer.readBytes((int) chunkSize));
                        chunkSize = 0;
                    }
                    this.chunkSize = chunkSize;
                    if (chunkSize == 0) {
                        checkpoint(State.READ_CHUNK_DELIMITER);
                    }
                    if (!chunk.isLast()) {
                        return chunk;
                    }
                }
            case READ_CHUNK_DELIMITER:
                {
                    for (; ; ) {
                        byte next = buffer.readByte();
                        if (next == HttpCodecUtil.CR) {
                            if (buffer.readByte() == HttpCodecUtil.LF) {
                                checkpoint(State.READ_CHUNK_SIZE);
                                return null;
                            }
                        } else if (next == HttpCodecUtil.LF) {
                            checkpoint(State.READ_CHUNK_SIZE);
                            return null;
                        }
                    }
                }
            case READ_CHUNK_FOOTER:
                {
                    HttpChunkTrailer trailer = readTrailingHeaders(buffer);
                    if (maxChunkSize == 0) {
                        return reset();
                    } else {
                        reset();
                        return trailer;
                    }
                }
            default:
                {
                    throw new Error("Shouldn't reach here.");
                }
        }
    }
}
