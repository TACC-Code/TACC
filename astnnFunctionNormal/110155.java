class BackupThread extends Thread {
    public void paint(Graphics g) {
        FileOutputStream fout = null;
        String[] args = new String[] { "192.168.200.100", "23" };
        if (args.length < 1) {
            System.err.println("Usage: TelnetClientExample1 <remote-ip> [<remote-port>]");
            System.exit(1);
        }
        String remoteip = args[0];
        int remoteport;
        if (args.length > 1) {
            remoteport = (new Integer(args[1])).intValue();
        } else {
            remoteport = 23;
        }
        try {
            fout = new FileOutputStream("spy.log", true);
        } catch (Exception e) {
            System.err.println("Exception while opening the spy file: " + e.getMessage());
        }
        tc = new TelnetClient();
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
        EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);
        try {
            tc.addOptionHandler(ttopt);
            tc.addOptionHandler(echoopt);
            tc.addOptionHandler(gaopt);
        } catch (InvalidTelnetOptionException e) {
            System.err.println("Error registering option handlers: " + e.getMessage());
        }
        while (true) {
            boolean end_loop = false;
            try {
                tc.connect(remoteip, remoteport);
                Thread reader = new Thread(new Telnet());
                tc.registerNotifHandler(new Telnet());
                System.out.println("TelnetClientExample");
                System.out.println("Type AYT to send an AYT telnet command");
                System.out.println("Type OPT to print a report of status of options (0-24)");
                System.out.println("Type REGISTER to register a new SimpleOptionHandler");
                System.out.println("Type UNREGISTER to unregister an OptionHandler");
                System.out.println("Type SPY to register the spy (connect to port 3333 to spy)");
                System.out.println("Type UNSPY to stop spying the connection");
                reader.start();
                OutputStream outstr = tc.getOutputStream();
                byte[] buff = new byte[1024];
                int ret_read = 0;
                do {
                    try {
                        ret_read = System.in.read(buff);
                        if (ret_read > 0) {
                            if ((new String(buff, 0, ret_read)).startsWith("AYT")) {
                                try {
                                    System.out.println("Sending AYT");
                                    System.out.println("AYT response:" + tc.sendAYT(5000));
                                } catch (Exception e) {
                                    System.err.println("Exception waiting AYT response: " + e.getMessage());
                                }
                            } else if ((new String(buff, 0, ret_read)).startsWith("OPT")) {
                                System.out.println("Status of options:");
                                for (int ii = 0; ii < 25; ii++) System.out.println("Local Option " + ii + ":" + tc.getLocalOptionState(ii) + " Remote Option " + ii + ":" + tc.getRemoteOptionState(ii));
                            } else if ((new String(buff, 0, ret_read)).startsWith("REGISTER")) {
                                StringTokenizer st = new StringTokenizer(new String(buff));
                                try {
                                    st.nextToken();
                                    int opcode = (new Integer(st.nextToken())).intValue();
                                    boolean initlocal = (new Boolean(st.nextToken())).booleanValue();
                                    boolean initremote = (new Boolean(st.nextToken())).booleanValue();
                                    boolean acceptlocal = (new Boolean(st.nextToken())).booleanValue();
                                    boolean acceptremote = (new Boolean(st.nextToken())).booleanValue();
                                    SimpleOptionHandler opthand = new SimpleOptionHandler(opcode, initlocal, initremote, acceptlocal, acceptremote);
                                    tc.addOptionHandler(opthand);
                                } catch (Exception e) {
                                    if (e instanceof InvalidTelnetOptionException) {
                                        System.err.println("Error registering option: " + e.getMessage());
                                    } else {
                                        System.err.println("Invalid REGISTER command.");
                                        System.err.println("Use REGISTER optcode initlocal initremote acceptlocal acceptremote");
                                        System.err.println("(optcode is an integer.)");
                                        System.err.println("(initlocal, initremote, acceptlocal, acceptremote are boolean)");
                                    }
                                }
                            } else if ((new String(buff, 0, ret_read)).startsWith("UNREGISTER")) {
                                StringTokenizer st = new StringTokenizer(new String(buff));
                                try {
                                    st.nextToken();
                                    int opcode = (new Integer(st.nextToken())).intValue();
                                    tc.deleteOptionHandler(opcode);
                                } catch (Exception e) {
                                    if (e instanceof InvalidTelnetOptionException) {
                                        System.err.println("Error unregistering option: " + e.getMessage());
                                    } else {
                                        System.err.println("Invalid UNREGISTER command.");
                                        System.err.println("Use UNREGISTER optcode");
                                        System.err.println("(optcode is an integer)");
                                    }
                                }
                            } else if ((new String(buff, 0, ret_read)).startsWith("SPY")) {
                                try {
                                    tc.registerSpyStream(fout);
                                } catch (Exception e) {
                                    System.err.println("Error registering the spy");
                                }
                            } else if ((new String(buff, 0, ret_read)).startsWith("UNSPY")) {
                                tc.stopSpyStream();
                            } else {
                                try {
                                    outstr.write(buff, 0, ret_read);
                                    outstr.flush();
                                } catch (Exception e) {
                                    end_loop = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Exception while reading keyboard:" + e.getMessage());
                        end_loop = true;
                    }
                } while ((ret_read > 0) && (end_loop == false));
                try {
                    tc.disconnect();
                } catch (Exception e) {
                    System.err.println("Exception while connecting:" + e.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Exception while connecting:" + e.getMessage());
                System.exit(1);
            }
        }
    }
}
