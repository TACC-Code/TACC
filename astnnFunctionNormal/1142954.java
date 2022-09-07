class BackupThread extends Thread {
    public void execute(final List<Request> tasks, String testCase, final String server, final Properties rcptInfo, final Properties configurationProperties, PasswordAuthenticator senderCredentials, int threadCount, boolean addedSmtpProcessor) throws Exception {
        domainAndPort = server + ":41002";
        String baseDir = System.getProperty("basedir");
        int count = 0;
        File tempJESDirTemp = null;
        do {
            tempJESDirTemp = new File(System.getProperty("java.io.tmpdir"), "jes" + count);
            if (!tempJESDirTemp.exists()) {
                tempJESDirTemp.mkdir();
                break;
            }
            count++;
            if (count == 1000) {
                System.exit(1000);
            }
        } while (true);
        final File tempJESDir = tempJESDirTemp;
        File testJESDir = new File(baseDir, "target" + File.separator + "test-classes" + File.separator + testCase);
        Utils.copyFiles(testJESDir, tempJESDir);
        File lib = new File(tempJESDir, "lib");
        lib.mkdir();
        File forTest = new File(baseDir, "forTest");
        forTest.mkdir();
        String[] surefirePathElements = System.getProperty("surefire.test.class.path").split(File.pathSeparator);
        File aFile;
        for (int i = 0; i < surefirePathElements.length; i++) {
            aFile = new File(surefirePathElements[i]);
            if (surefirePathElements[i].contains("commons-codec") || surefirePathElements[i].contains("commons-logging") || surefirePathElements[i].contains("dnsjava") || surefirePathElements[i].contains("log4j") || surefirePathElements[i].contains("javax.mail") || surefirePathElements[i].contains("pop3") || surefirePathElements[i].contains("smtp") || surefirePathElements[i].contains("activation")) {
                FileUtils.copyFile(aFile, new File(forTest, aFile.getName()));
            }
        }
        final List<File> userDirectories = new ArrayList((int) (tasks.size() / .75));
        for (Request request : tasks) {
            File userTempDir = new File(tempJESDir, request.getUsername());
            userDirectories.add(userTempDir);
            if (!userTempDir.exists()) {
                userTempDir.mkdir();
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
        final String smtpHost = server;
        final String smtpPort = "17025";
        final String pop3Host = server;
        final String pop3Port = "17110";
        final Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", smtpHost);
        properties.setProperty("mail.smtp.port", smtpPort);
        properties.setProperty("mail.smtp.localaddress", server);
        properties.setProperty("mail.pop3.host", pop3Host);
        properties.setProperty("mail.pop3.port", pop3Port);
        properties.setProperty("mail.pop3.localaddress", server);
        if (configurationProperties.getProperty("SASL") != null) {
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.auth.mechanisms", configurationProperties.getProperty("SASL"));
            if (configurationProperties.getProperty("REALM") != null) {
                properties.setProperty("mail.smtp.sasl.realm", configurationProperties.getProperty("REALM"));
            }
        }
        if (configurationProperties.getProperty("STARTTLS") != null) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(null, null);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            File truststore = new File(tempJESDir, "truststore.jks");
            if (truststore.exists()) {
                KeyStore ks = KeyStore.getInstance("JKS", "SUN");
                FileInputStream fis = new FileInputStream(truststore);
                ks.load(fis, null);
                tmf.init(ks);
                fis.close();
                fis = null;
            } else {
                tmf.init((KeyStore) null);
            }
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            properties.put("mail.smtp.ssl.socketFactory", sslContext.getSocketFactory());
        }
        System.setProperty("java.security.policy", tempJESDir.getPath() + File.separator + "jes.policy");
        Properties log4jProperties = new Properties();
        log4jProperties.setProperty("defaultthreshold", "info");
        log4jProperties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        log4jProperties.setProperty("log4j.appender.stdout.threshold", "info");
        log4jProperties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d{ISO8601} - [%t] %C{1} - %m%n");
        log4jProperties.setProperty("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
        log4jProperties.setProperty("log4j.appender.file.File", tempJESDir.getPath() + File.separator + "logs" + File.separator + "jes.log");
        log4jProperties.setProperty("log4j.appender.file.MaxFileSize", "10000KB");
        log4jProperties.setProperty("log4j.appender.file.MaxBackupIndex", "20");
        log4jProperties.setProperty("log4j.appender.file.threshold", "debug");
        log4jProperties.setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.file.layout.ConversionPattern", "%d{ISO8601} - [%t] %C{1} - %m%n");
        log4jProperties.setProperty("log4j.rootLogger", "debug,stdout,file");
        org.apache.log4j.PropertyConfigurator.configure(log4jProperties);
        com.ericdaugherty.mail.server.Mail.main(new String[] { tempJESDir.getPath(), addedSmtpProcessor ? "testing" : "" });
        addDomain(server);
        addRealm(1, "users");
        List<String> realms = new ArrayList(1);
        realms.add("users");
        String username = senderCredentials.getPasswordAuthentication().getUserName();
        if (username.indexOf('@') != -1) {
            username = username.substring(0, username.indexOf('@'));
        }
        addUser(username, senderCredentials.getPasswordAuthentication().getPassword().toCharArray(), 1, realms);
        Iterator iterator = rcptInfo.keySet().iterator();
        String user;
        while (iterator.hasNext()) {
            user = (String) iterator.next();
            addUser(user, rcptInfo.getProperty(user).toCharArray(), 1, realms);
        }
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException ex) {
        }
        final Map<String, List<String>> userMessages = new HashMap(4, 0.75f);
        userMessages.put("ernest", new CopyOnWriteArrayList());
        userMessages.put("kara", new CopyOnWriteArrayList());
        userMessages.put("perl", new CopyOnWriteArrayList());
        userMessages.put("wizard", new CopyOnWriteArrayList());
        List<Runnable> runnables = new ArrayList(threadCount);
        List<Request> requests;
        Runnable runnable;
        Random random = new Random();
        for (int i = 0; i < threadCount; i++) {
            requests = new ArrayList();
            for (int j = 0; j < tasks.size(); j++) {
                Request request = new Request(tasks.get(j).getUsername(), tasks.get(j).getMessage());
                requests.add(request);
            }
            int shufflingCount = Math.max(threadCount * 4, 8);
            for (int j = 0; j < shufflingCount; j++) {
                requests.add(requests.remove(random.nextInt(requests.size())));
            }
            runnable = getJavaMailRunnable(requests, tempJESDir, userMessages, threadCount, server, tasks.size() / 4 * threadCount, properties, configurationProperties, senderCredentials);
            runnables.add(runnable);
        }
        for (int i = 0; i < threadCount; i++) {
            new Thread(runnables.get(i)).start();
        }
        runnables.clear();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException ex) {
        }
        com.ericdaugherty.mail.server.Mail.getInstance().shutdown();
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException ex) {
        }
        File usersDir = new File(tempJESDir, "users");
        FileFilter ff = new FileFilter() {

            public boolean accept(File file) {
                if (file.isDirectory()) {
                    Iterator iter = rcptInfo.keySet().iterator();
                    String filename;
                    while (iter.hasNext()) {
                        filename = file.getName();
                        if (filename.startsWith((String) iter.next()) && filename.endsWith(server)) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
        };
        File[] users = usersDir.listFiles(ff);
        ff = new FileFilter() {

            public boolean accept(File file) {
                if (!file.isDirectory() && file.getPath().toLowerCase().endsWith(".loc")) {
                    return true;
                }
                return false;
            }
        };
        File[] messages;
        for (int i = 0; i < users.length; i++) {
            messages = users[i].listFiles(ff);
            assertEquals(messages.length, tasks.size() / 4 * threadCount);
        }
        org.apache.log4j.Logger.getRootLogger().getAppender("file").close();
        org.apache.log4j.LogManager.shutdown();
        Utils.deleteFiles(tempJESDir);
        tempJESDir.delete();
        Utils.deleteFiles(forTest);
        forTest.delete();
    }
}
