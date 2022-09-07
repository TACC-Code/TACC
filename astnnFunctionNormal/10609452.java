class BackupThread extends Thread {
    private static void start() throws IOException, ClassNotFoundException {
        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        InetAddress lh = InetAddress.getLocalHost();
        InetSocketAddress isa = new InetSocketAddress(lh, 8989);
        ssc.socket().bind(isa);
        ssc.register(selector, OP_ACCEPT);
        int keysAdded;
        while ((keysAdded = selector.select()) > 0) {
            Set readyKeys = selector.selectedKeys();
            Iterator i = readyKeys.iterator();
            while (i.hasNext()) {
                SelectionKey sk = (SelectionKey) i.next();
                i.remove();
                if (sk.isAcceptable()) {
                    ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
                    Socket s = nextReady.accept().socket();
                    s.getChannel().configureBlocking(false);
                    s.getChannel().register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    SocketChannel sc = (SocketChannel) sk.channel();
                    if (!sc.isOpen()) {
                        sc.register(selector, 0);
                    }
                    Attachment at = (Attachment) sk.attachment();
                    if (at == null) {
                        log.info("New Client Connected");
                        URLClassLoader cl = (URLClassLoader) Thread.currentThread().getContextClassLoader();
                        System.out.println("We use this loader " + cl);
                        at = new Attachment(sc);
                        sk.attach(at);
                    }
                    List<Object> objs = at.read(sk, sc);
                    putObjectsOnProcessQueue(objs, at);
                }
            }
        }
        throw new UnsupportedOperationException("Somehow our acceptor escaped!");
    }
}
