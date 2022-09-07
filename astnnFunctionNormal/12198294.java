class BackupThread extends Thread {
    public void execute(final List<Request> tasks, String testCase, String accountsXML, boolean shutdown, final String server) throws Exception {
        String localRepo = System.getProperty("localRepository");
        String baseDir = System.getProperty("basedir");
        int count = 0;
        File tempJESDirTemp = null;
        do {
            tempJESDirTemp = new File(System.getProperty("java.io.tmpdir"), "jes" + count);
            if (tempJESDirTemp.mkdir()) {
                break;
            }
            count++;
            if (count == 1000) System.exit(1000);
        } while (true);
        final File tempJESDir = tempJESDirTemp;
        File testJESDir = new File(baseDir, "target" + File.separator + "test-classes" + File.separator + testCase);
        final File accountsDir = new File(baseDir, "target" + File.separator + "test-classes");
        TarUtils.untar(accountsDir + File.separator + "account.tar.gz", accountsDir);
        FileUtils.copyFile(new File(accountsDir, accountsXML), new File(accountsDir, "accounts" + File.separator + "mail" + File.separator + "account.xml"));
        Utils.ensureAllAcountMailDirsExist(new File(accountsDir + File.separator + "accounts" + File.separator + "mail"));
        TarUtils.untar(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "101.tar.gz", new File(accountsDir + File.separator + "accounts" + File.separator + "mail"));
        Utils.copyFiles(testJESDir, tempJESDir);
        File lib = new File(tempJESDir, "lib");
        lib.mkdir();
        File forTest = new File(baseDir, "forTest");
        forTest.mkdir();
        String[] surefirePathElements = System.getProperty("surefire.test.class.path").split(File.pathSeparator);
        File aFile;
        for (int i = 0; i < surefirePathElements.length; i++) {
            aFile = new File(surefirePathElements[i]);
            if (surefirePathElements[i].contains("commons-codec-1.4-SNAPSHOT") || surefirePathElements[i].contains("commons-logging-1.1.1") || surefirePathElements[i].contains("dnsjava-2.0.6") || surefirePathElements[i].contains("log4j-1.2.15")) {
                FileUtils.copyFile(aFile, new File(forTest, aFile.getName()));
            }
        }
        Utils.copyFiles(forTest, lib);
        File testJESFile = new File(baseDir, "pom.xml");
        BufferedReader br = null;
        String line = null, name = null, version = null, pckg = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(testJESFile), System.getProperty("file.encoding")));
            count = 0;
            do {
                line = br.readLine().trim();
                if (line.startsWith("<version>")) {
                    version = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                } else if (line.startsWith("<name>")) {
                    name = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                } else if (line.startsWith("<packaging>")) {
                    pckg = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                }
                if (version != null && name != null && pckg != null) {
                    break;
                } else if (count == 30) {
                    throw new IOException();
                }
                count++;
            } while (true);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                }
            }
        }
        testJESFile = new File(baseDir, "target" + File.separator + name + "-" + version + "." + pckg);
        FileUtils.copyFile(testJESFile, new File(tempJESDir, testJESFile.getName()));
        URL[] urls = new URL[] { new URL("jar:file:" + localRepo + "/org/columba/1.4/columba-1.4.jar!/"), new URL("jar:file:" + localRepo + "/org/ristretto/1.2-all/ristretto-1.2-all.jar!/"), new URL("jar:file:" + localRepo + "/lucene/lucene/1.4.3/lucene-1.4.3.jar!/"), new URL("file:" + baseDir + "/target/test-classes/com/ericdaugherty/mail/CreateAndSendColumbaSMTPMessage.class") };
        final URLClassLoader cl = new JESTestClassLoader(urls);
        System.setProperty("java.security.policy", tempJESDir.getPath() + File.separator + "jes.policy");
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Log4JLogger");
        System.setProperty("log4j.configuration", "file:" + tempJESDir.getPath() + File.separator + "conf" + File.separator + "log4j.properties");
        System.setProperty("log4j.debug", "");
        com.ericdaugherty.mail.server.Mail.main(new String[] { tempJESDir.getPath(), "" });
        final Map<String, List<String>> userMessages = new HashMap(4, 0.75f);
        userMessages.put("ernest", new ArrayList(tasks.size() / 4));
        userMessages.put("kara", new ArrayList(tasks.size() / 4));
        userMessages.put("perl", new ArrayList(tasks.size() / 4));
        userMessages.put("wizard", new ArrayList(tasks.size() / 4));
        Class clax = cl.loadClass("org.columba.core.xml.XmlIO");
        Object X = clax.getConstructor().newInstance();
        Method xMethod = clax.getMethod("load", URL.class);
        xMethod.invoke(X, new URL("file:" + accountsDir.getPath() + File.separator + "accounts" + File.separator + "mail" + File.separator + "account.xml"));
        Method xMethodR = clax.getMethod("getRoot");
        Object root = xMethodR.invoke(X);
        clax = cl.loadClass("org.columba.core.xml.XmlElement");
        xMethodR = clax.getMethod("getElement", String.class);
        root = xMethodR.invoke(root, "accountlist");
        xMethodR = clax.getMethod("getElement", int.class);
        root = xMethodR.invoke(root, 0);
        final Object account = root;
        final Class clazz = cl.loadClass("com.ericdaugherty.mail.CreateAndSendColumbaSMTPMessage");
        final Object object = clazz.getConstructor(File.class).newInstance(accountsDir);
        final List<byte[]> hashes = new ArrayList(tasks.size());
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    Method method = clazz.getMethod("execute", String.class, cl.loadClass("org.columba.ristretto.io.Source"), cl.loadClass("org.columba.mail.config.AccountItem"), String.class);
                    Method method1 = clazz.getMethod("execute1", File.class, String.class);
                    while (tasks.size() > 0) {
                        final Request request = tasks.remove(0);
                        System.out.println("Checking out " + request.getUsername() + "'s message");
                        Object message = cl.loadClass("org.columba.ristretto.io.FileSource").getConstructor(File.class).newInstance(((FileSource) request.getMessage()).getFile());
                        method.invoke(object, request.getUsername(), message, cl.loadClass("org.columba.mail.config.AccountItem").getConstructor(cl.loadClass("org.columba.core.xml.XmlElement")).newInstance(account), server);
                        File userDir = new File(tempJESDir, "users" + File.separator + request.getUsername() + "@" + server);
                        int count = 0;
                        while (!userDir.exists()) {
                            System.out.println(userDir + " not yet created, sleeping...");
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException ex) {
                            }
                            count++;
                            if (count == 7) assertTrue(false);
                        }
                        FilenameFilter ff = new FilenameFilter() {

                            public boolean accept(File directory, String filename) {
                                if (filename.toLowerCase().endsWith(".loc")) {
                                    return userMessages.get(request.getUsername()).add(filename);
                                }
                                return false;
                            }
                        };
                        File[] results;
                        count = 0;
                        while ((results = userDir.listFiles(ff)).length == 0) {
                            System.out.println(request.getUsername() + "'s mail not yet received, sleeping...");
                            try {
                                Thread.sleep(5 * 1000);
                            } catch (InterruptedException ex) {
                            }
                            count++;
                            if (count == 7) assertTrue(false);
                        }
                        byte[] derivedMD5 = Utils.getDerivedMD5(results[0]);
                        byte[] originalMD5 = Utils.getOriginalMD5(new File(((FileSource) request.getMessage()).getFile().getPath() + ".md5"));
                        boolean assertionCandidate = Arrays.equals(originalMD5, derivedMD5);
                        System.out.println(toHex(derivedMD5, 16));
                        System.out.println(toHex(originalMD5, 16));
                        assertTrue(assertionCandidate);
                        hashes.add(derivedMD5);
                        method1.invoke(object, accountsDir, request.getUsername());
                        System.out.println("Going to sleep again");
                        try {
                            Thread.sleep(7 * 1000);
                        } catch (InterruptedException ex) {
                        }
                    }
                    method = clazz.getMethod("conclude");
                    method.invoke(object);
                    System.out.println("Going to sleep once more");
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    lock.notify();
                }
            }
        });
        thread.setContextClassLoader(cl);
        thread.start();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (shutdown) {
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException ex) {
            }
            com.ericdaugherty.mail.server.Mail.getInstance().shutdown();
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException ex) {
            }
        }
        File mailbox = new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "101");
        List<byte[]> pop3Hashes = new AcquirePop3Hashes().getHashes(new File(mailbox, "101"));
        Iterator<byte[]> iter = pop3Hashes.iterator();
        byte[] result;
        Iterator<byte[]> iter2;
        while (iter.hasNext()) {
            result = iter.next();
            iter2 = hashes.iterator();
            while (iter2.hasNext()) {
                if (Arrays.equals(result, iter2.next())) ;
                iter2.remove();
            }
        }
        assertEquals(hashes.size(), 0);
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "101"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "102"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "103"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "104"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "105"));
        deleteAccountFiles(new File(accountsDir + File.separator + "accounts" + File.separator + "mail" + File.separator + "107"));
        File toDelete = new File(accountsDir + File.separator + "accounts");
        Utils.deleteFiles(toDelete);
        toDelete.delete();
        Utils.deleteFiles(tempJESDir);
        tempJESDir.delete();
        Utils.deleteFiles(forTest);
        forTest.delete();
        org.columba.mail.pop3.POP3ServerCollection.shutdown();
        org.columba.mail.config.MailConfig.shutdown();
    }
}
