class BackupThread extends Thread {
    public int execute(String[] args, Reader in, PrintWriter out, Session session) {
        SessionImpl si = (SessionImpl) session;
        String usage = null;
        if (args.length > 0) {
            if ("help".startsWith(args[0])) {
                if (args.length == 1) {
                    if ("".equals(si.currentGroup)) {
                        return help(out, si);
                    }
                    return helpAbout(si.currentGroup, out, si);
                } else if (args.length == 2) {
                    if (args[1].equals("all")) {
                        return help(out, si);
                    }
                    return helpAbout(args[1], out, si);
                }
                usage = help_help;
            } else if ("alias".startsWith(args[0])) {
                if (si == null) {
                    out.println("Alias not available from runCommand method");
                    return 1;
                }
                if (args.length == 1) {
                    for (Enumeration e = si.aliases.keys(); e.hasMoreElements(); ) {
                        String a = (String) e.nextElement();
                        out.println(a + " = " + si.aliases.getString(a));
                    }
                } else if (args.length == 2) {
                    String a = si.aliases.getString(args[1]);
                    if (a != null) {
                        out.println(args[1] + " = " + a);
                    } else {
                        out.println("No alias for: " + args[1]);
                    }
                } else {
                    String[] na = new String[args.length - 2];
                    System.arraycopy(args, 2, na, 0, na.length);
                    si.aliases.put(args[1], na);
                }
                return 0;
            } else if ("enter".startsWith(args[0])) {
                if (args.length == 2) {
                    try {
                        ServiceReference ref = Command.matchCommandGroup(bc, args[1]);
                        if (ref != null) {
                            si.currentGroup = (String) ref.getProperty("groupName");
                        } else {
                            si.currentGroup = NAME;
                        }
                        return 0;
                    } catch (IOException e) {
                        out.println(e.getMessage());
                        return 1;
                    }
                }
                usage = help_enter;
                return 1;
            } else if ("echo".startsWith(args[0])) {
                int pos = 1;
                boolean nl = true;
                if (args.length >= 2 && "-n".equals(args[1])) {
                    nl = false;
                    pos = 2;
                }
                while (pos < args.length) {
                    out.print(args[pos]);
                    if (++pos < args.length) {
                        out.print(" ");
                    }
                }
                if (nl) {
                    out.println();
                }
                return 0;
            } else if ("leave".startsWith(args[0])) {
                if (args.length == 1) {
                    si.currentGroup = "";
                    return 0;
                }
                usage = help_leave;
            } else if ("prompt".startsWith(args[0])) {
                if (args.length == 2) {
                    si.prompt = args[1];
                    return 0;
                }
                usage = help_prompt;
            } else if ("quit".startsWith(args[0])) {
                if (args.length == 1) {
                    si.close();
                    return 0;
                }
                usage = help_quit;
            } else if ("save".startsWith(args[0])) {
                File file = null;
                if (args.length == 1) {
                    file = bc.getDataFile(SessionImpl.ALIAS_SAVE);
                } else if (args.length == 2) {
                    file = new File(args[1]);
                }
                if (file != null) {
                    try {
                        OutputStream p = new FileOutputStream(file);
                        si.aliases.save(p);
                    } catch (IOException e) {
                        out.println("Failed to save aliases: " + e);
                        return 1;
                    }
                    return 0;
                }
                usage = help_save;
            } else if ("restore".startsWith(args[0])) {
                if (args.length == 1) {
                    si.aliases.setDefault();
                    return 0;
                } else if (args.length == 2) {
                    try {
                        InputStream r = new FileInputStream(new File(args[1]));
                        si.aliases.clear();
                        si.aliases.restore(r);
                    } catch (IOException e) {
                        out.println("Failed to restore aliases from " + args[1] + ": " + e);
                        return 1;
                    }
                    return 0;
                }
                usage = help_restore;
            } else if ("source".startsWith(args[0])) {
                if (args.length == 2) {
                    InputStreamReader sin = null;
                    try {
                        URL surl = new URL(args[1]);
                        sin = new InputStreamReader(surl.openStream());
                        SessionImpl ss = new SessionImpl(bc, "source: " + args[1], sin, out, null);
                        ss.prompt = null;
                        ss.start();
                        try {
                            ss.join();
                        } catch (InterruptedException ignore) {
                        }
                        ss.close();
                    } catch (IOException e) {
                        out.println("Failed to source URL: " + e.getMessage());
                        return 1;
                    } finally {
                        if (sin != null) {
                            try {
                                sin.close();
                            } catch (IOException ignore) {
                            }
                        }
                    }
                    return 0;
                }
                usage = help_source;
            } else if ("unalias".startsWith(args[0])) {
                if (si == null) {
                    out.println("Unalias not available from runCommand method");
                    return 1;
                }
                if (args.length == 2) {
                    if (si.aliases.remove(args[1]) != null) {
                        return 0;
                    }
                    return 1;
                }
                usage = help_unalias;
            }
        }
        if (usage != null) {
            usage = "Usage: " + usage;
        } else {
            usage = getLongHelp();
        }
        out.println(usage);
        return -1;
    }
}
