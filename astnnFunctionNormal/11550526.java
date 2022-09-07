class BackupThread extends Thread {
    private boolean executeParent() {
        if (hasOption("load")) {
            if (hasOption("verbose")) {
                Logger.getRootLogger().setLevel(Level.ALL);
            } else Logger.getRootLogger().setLevel(Level.ERROR);
            if (hasOption("f")) {
                String fName = getOption("file").getValue().trim();
                try {
                    context.getIo().in.setInputStream(new FileInputStream(Path.CURDIR + "etc" + Path.fs + new File(fName)));
                    context.setScript(true);
                    read();
                    context.setScript(false);
                    context.getIo().in.restore();
                } catch (FileNotFoundException e) {
                    log.error(fName + ": No such file or directory");
                }
                return true;
            } else {
                log.error("Missing '-f' file option");
                return true;
            }
        }
        if (hasOption("exit")) {
            System.exit(0);
        }
        if (hasOption("alias")) {
            Map<String, String> optAlias;
            Option opt;
            String str[] = getOption("alias").getValues();
            optAlias = createMappings(str);
            for (String s : optAlias.keySet()) {
                if (options.hasOption(s)) {
                    if (!this.alias.containsKey(s)) {
                        alias.put(s, new HashSet<String>());
                    }
                    opt = (Option) options.getOption(s).clone();
                    opt.setLongOpt(optAlias.get(s));
                    if (!hasAlias(opt)) {
                        options.addOption(opt);
                        alias.get(s).add(optAlias.get(s));
                    } else {
                        log.error("Alias: " + opt.getLongOpt() + " already exists");
                    }
                }
            }
            return true;
        }
        if (hasOption("setloglevel")) {
            Logger.getRootLogger().setLevel(Level.toLevel(getOptionValue("setloglevel")));
            log.error("Level changed to : " + Logger.getRootLogger().getLevel());
            return true;
        }
        if (hasOption("save")) {
            if (hasOption("f")) {
                String fileName = getPath() + "../../" + getOption("file").getValue();
                try {
                    BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(fileName)));
                    for (String str : alias.keySet()) {
                        StringBuffer sbuf = new StringBuffer();
                        sbuf.append(this.getClass().getName() + ".alias." + str + "=");
                        for (String optAlias : alias.get(str)) {
                            sbuf.append(optAlias + ",");
                        }
                        sbuf.append("\n");
                        bwr.write(sbuf.toString());
                    }
                    bwr.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            return true;
        } else if (hasOption("help")) {
            Map<String, CommandPlugin> cmds = ShellManager.getPlugins();
            System.out.println("These shell commands are defined internally.Type `help' to see this list." + "Type `man name' to find out more about the function `name'.\n");
            for (String s : cmds.keySet()) {
                System.out.println(s.toLowerCase() + " [arguments....]");
            }
            Collection<Option> opts = internalOpt.getOptions();
            for (Option opt : opts) {
                System.out.println(opt.getOpt() + opt.getDescription());
            }
            return true;
        } else if (hasOption("man")) {
            ManCLI man = new ManCLI();
            String[] cmdArr = this.getOption("man").getValues();
            int section = 0;
            int num = 20;
            String cmd = null;
            try {
                if (cmdArr == null) {
                    StandardIO.__out.println("Usage: man < command >");
                } else {
                    cmd = cmdArr[0];
                    if (hasOption("less")) {
                        man.setIfPages(true);
                        if (this.getOption("less").getValue() != null) num = Integer.parseInt(getOption("less").getValue());
                    }
                    if (cmd.equals("all")) {
                        man.listAll();
                    } else {
                        try {
                            if (cmd != null) section = Integer.parseInt(cmd);
                        } catch (Exception e) {
                        }
                        try {
                            man.listMan(cmd, section);
                        } catch (FileNotFoundException e) {
                            try {
                                man.listType(cmd, section);
                            } catch (FileNotFoundException ee) {
                                log.error("Command doest not exist");
                            }
                        }
                    }
                    if (man.getIfPages()) {
                        Less.output(man.getBuffers(), num);
                        man.setBuffers("");
                        man.setIfPages(false);
                    }
                }
            } catch (Exception e) {
                log.error("Command doest not exist");
            }
            return true;
        } else if (hasOption("more")) {
            String fName = getOptionValue("more");
            try {
                Scanner sc = new Scanner(new File(fName));
                while (sc.hasNextLine()) {
                    System.out.println(sc.nextLine());
                }
            } catch (FileNotFoundException e) {
                log.error(fName + ": No such file or directory");
            }
            return true;
        } else if (hasOption("history")) {
            return true;
        } else if (hasOption("copy")) {
            String[] urls = getOptionValues("copy");
            if (urls.length != 2) {
                log.error("Usage: copy <source> <destination>");
            } else {
                new org.cyberaide.gridftp.GridFTP().copy(urls[0], urls[1]);
            }
            return true;
        }
        return false;
    }
}
