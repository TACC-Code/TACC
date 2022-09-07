class BackupThread extends Thread {
    public static void main(String argv[]) {
        Integer EOF = Integer.MIN_VALUE;
        DataFlowNetwork<Object> network = new DataFlowNetwork<Object>();
        network.addChannel(new Channel<Integer>("random-stream", EOF));
        network.addChannel(new Channel<Integer>("even-numbers", EOF));
        network.addChannel(new Channel<Integer>("odd-numbers", EOF));
        network.addChannel(new Channel<Integer>("even-sum", EOF));
        network.addChannel(new Channel<Integer>("odd-sum", EOF));
        network.addChannel(new Channel<Integer>("total-sum", EOF));
        network.addNode(new SplitParity(), new String[] { "random-stream" }, new String[] { "even-numbers", "odd-numbers" });
        network.addNode(new Accumulator("even-numbers", "even-sum"), new String[] { "even-numbers" }, new String[] { "even-sum" });
        network.addNode(new Accumulator("odd-numbers", "odd-sum"), new String[] { "odd-numbers" }, new String[] { "odd-sum" });
        network.addNode(new Sum("even-sum", "odd-sum", "total-sum"), new String[] { "even-sum", "odd-sum" }, new String[] { "total-sum" });
        network.addNode(new Printer("Even sum=", "even-sum"), new String[] { "even-sum" }, new String[0]);
        network.addNode(new Printer("Odd sum=", "odd-sum"), new String[] { "odd-sum" }, new String[0]);
        network.addNode(new Printer("Total sum=", "total-sum"), new String[] { "total-sum" }, new String[0]);
        try {
            Future<Boolean> f = network.start();
            Channel<Integer> ch = network.getChannel("random-stream");
            for (int i = 0; i < 1000; i++) {
                ch.put((int) (Math.random() * 10000));
            }
            ch.put(EOF);
            System.out.println("Network completed successfully: " + f.get());
            f = network.start();
            ch = network.getChannel("random-stream");
            for (int i = 0; i < 1000; i++) {
                ch.put((int) (Math.random() * 10000));
            }
            ch.put(EOF);
            System.out.println("Network completed successfully: " + f.get());
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        } finally {
            network.shutdown();
        }
    }
}
