class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object obj = e.getMessage();
        if (!readingChunks && (obj instanceof HttpResponse)) {
            HttpResponse response = (HttpResponse) e.getMessage();
            HttpResponseStatus status = response.getStatus();
            System.out.println("STATUS: " + status);
            if (!response.getHeaderNames().isEmpty()) {
                for (String name : response.getHeaderNames()) {
                    for (String value : response.getHeaders(name)) {
                        System.out.println("HEADER: " + name + " = " + value);
                    }
                }
                System.out.println();
            }
            if (response.getStatus().getCode() == 200 && response.isChunked()) {
                readingChunks = true;
                HttpClient.ok.incrementAndGet();
                System.out.print("CHUNKED CONTENT {");
            } else if (response.getStatus().getCode() != 200) {
                HttpClient.ko.incrementAndGet();
                System.err.print("Error: ");
                ChannelBuffer content = response.getContent();
                System.err.println(content.toString(CharsetUtil.UTF_8));
                Channels.close(e.getChannel());
            } else {
                HttpClient.ok.incrementAndGet();
                System.out.print("CONTENT NOT CHUNKED: ");
                ChannelBuffer content = response.getContent();
                System.out.println(content.readableBytes());
                content.skipBytes(content.readableBytes());
                Channels.close(e.getChannel());
            }
        } else {
            readingChunks = true;
            HttpChunk chunk = (HttpChunk) e.getMessage();
            chunk.getContent().clear();
            if (chunk.isLast()) {
                readingChunks = false;
                System.out.println("} END OF CHUNKED CONTENT");
                Channels.close(e.getChannel());
            } else {
                System.out.print("o");
            }
        }
    }
}
