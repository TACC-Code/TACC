class BackupThread extends Thread {
    private static void usage(String m) {
        System.out.println("This is the BORJ driver for the BOXER toolkit (version " + Version.version + ")");
        System.out.println("Usage: java [options] borj.Driver command:file [command:file] ...");
        System.out.println("For example:");
        System.out.println("  java [options] borj.Driver [train:]train.xml [test:]test.xml");
        System.out.println("  java [options] borj.Driver [read-suite:suite-in.xml] train:train1.xml train:train2.xml test:test_a.xml train:train3.xml test:test_b.xml [write:model-out.xml]");
        System.out.println(" ... etc.");
        System.out.println("Optons:");
        System.out.println(" [-Dmodel=eg|tg|trivial] [-Drunid=RUN_ID] [-Dverbose=true|false | -Dverbosity=0|1|2|3]");
        System.out.println("See Javadoc for borj.Driver for the full list of commands.");
        if (m != null) {
            System.out.println(m);
        }
        System.exit(1);
    }
}
