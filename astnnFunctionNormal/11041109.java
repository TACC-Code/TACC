class BackupThread extends Thread {
    public static void main(String argv[]) {
        int iterations = 10000;
        int threadCount = 2;
        int rowCount = 10;
        countInterval = rowCount / 10;
        ArgvIterator argvIterator = new ArgvIterator(argv);
        String curArg = null;
        try {
            while (argvIterator.hasNext()) {
                curArg = argvIterator.next();
                if ("-iterations".equals(curArg)) {
                    iterations = Integer.getInteger(argvIterator.next());
                    if (iterations == 0) {
                        iterations = 100;
                    }
                } else if ("-user".equals(curArg)) {
                    userName = argvIterator.next();
                } else if ("-password".equals(curArg)) {
                    password = argvIterator.next();
                } else if ("-slow".equals(curArg)) {
                    slowTest = true;
                } else if ("-help".equals(curArg) || "-h".equals(curArg)) {
                    printHelp();
                    System.exit(0);
                } else {
                    fatal("Unrecognized global option: " + curArg);
                }
            }
        } catch (NumberFormatException e) {
            fatal("Bad numeric argument for " + curArg);
        } catch (ArrayIndexOutOfBoundsException e) {
            fatal("Missing value for " + curArg);
        }
        try {
            Class.forName("com.continuent.tungsten.router.jdbc.TSRDriver");
        } catch (Exception e) {
            println("Exception while initializing RouterManager:" + e);
            System.exit(1);
        }
        if (slowTest) {
            threadCount = 1;
            rowCount = 10;
        }
        RouterHandler handler = null;
        Thread handlerThread = null;
        if (localRouterHandler == true) {
            try {
                RouterManager.getInstance().configureRouter();
            } catch (RouterException e) {
                println("Exception while configuring RouterManager:" + e);
                return;
            }
            handler = new RouterHandler();
            handlerThread = new Thread(handler);
            handlerThread.start();
        }
        TestRouterManager test = new TestRouterManager();
        if (slowTest) {
            threadCount = 2;
            rowCount = 1;
        }
        if (threadCount < 2) {
            threadCount = 2;
        }
        readers = new Thread[threadCount / 2];
        writers = new Thread[threadCount / 2];
        for (int i = 0; i < threadCount / 2; i++) {
            if (slowTest) {
                readOnlyWriter = true;
            }
            CycleRunner writer = test.new CycleRunner(i, rowCount, iterations, test, readOnlyWriter);
            writers[i] = new Thread(writer);
            test.threadPlus();
            if (!slowTest) {
                CycleRunner reader = test.new CycleRunner(i, rowCount, iterations, test, true);
                readers[i] = new Thread(reader);
                reader.setCompanion(writer);
                test.threadPlus();
                readers[i].start();
            }
            writers[i].start();
        }
        println("[APP: WAITING FOR ALL THREADS TO EXIT]");
        test.waitForAllThreads();
        println("[APP: ALL THREADS COMPLETED. EXITING...]");
        if (localRouterHandler == true) {
            handlerThread.interrupt();
        }
        System.exit(0);
    }
}
