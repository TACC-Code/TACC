class BackupThread extends Thread {
    public static void main(String[] args) {
        EventPrinter printer = new EventPrinter();
        ChannelWatcher watcher = new ChannelWatcher(printer);
        args = watcher.readArgs(args);
        verbose = 0;
        for (int jj = 0; jj < args.length; jj++) {
            if (args[jj].equalsIgnoreCase("--pretty")) {
                printer.setPrettyPrint(true);
            } else if (args[jj].equalsIgnoreCase("--raw")) {
                printer.setPrettyPrint(false);
            } else if (args[jj].equalsIgnoreCase("--verbose")) {
                verbose++;
            } else if (args[jj].equalsIgnoreCase("--version")) {
                System.out.println("WatchChannel " + version());
                System.out.println("Copyright (c) 2005 Intel Corporation - All rights reserved.");
                System.exit(1);
            } else if (args[jj].equalsIgnoreCase("--help")) {
                Invocation();
                System.exit(0);
            } else {
                System.out.println("Parameter '" + args[jj] + "' not recognized.");
                Invocation();
                System.exit(0);
            }
        }
        if (watcher.getChannel() == null) {
            System.out.println("A channel to watch must be specified.");
            Invocation();
            return;
        }
        if (verbose > 0) {
            System.out.println("Watching for events of type " + watcher.getType() + " on channel " + watcher.getChannel());
        }
        keepWaiting = true;
        try {
            watcher.connect();
            while (keepWaiting) {
                Thread.sleep(30000);
                if (!watcher.isConnected()) {
                    System.out.println("WatchChannel: lease is not active.  Exiting");
                    keepWaiting = false;
                }
            }
        } catch (Exception e) {
            System.out.println("WatchChannel: exception while waiting:" + e.toString());
            keepWaiting = false;
        }
        watcher.disconnect();
    }
}
