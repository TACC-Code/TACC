class BackupThread extends Thread {
    private static boolean runQuestion(Properties props, String question, String type) {
        Socket conn = null;
        BufferedReader in = null;
        PrintWriter out = null;
        boolean result = false;
        try {
            boolean done = false;
            LOGGER.debug("Connecting to " + props.getProperty("server") + " on port " + props.getProperty("port"));
            conn = new Socket(props.getProperty("server"), Integer.valueOf(props.getProperty("port")));
            LOGGER.debug("Connected.  Setting up input/output streams");
            out = new PrintWriter(conn.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            LOGGER.debug("Sending Question Name: " + question);
            out.print(question + "\r\n");
            out.flush();
            String line;
            try {
                line = in.readLine();
            } catch (NoSuchElementException exc) {
                line = null;
            }
            if (line == null) done = true; else if (line.equals("INVALID COMMAND OR ASSIGNMENT")) {
                System.err.println(line);
                LOGGER.error("ERROR: " + line);
                closeConnection(conn, in, out);
                System.exit(1);
            } else {
                System.out.println("Number of Test Cases:\t" + line);
                LOGGER.info("Number of Test Cases:\t" + line);
                int numTestCases = 0;
                try {
                    numTestCases = Integer.valueOf(line.trim());
                } catch (NumberFormatException numEx) {
                    LOGGER.error("ERROR: Invalid number: " + line);
                }
                for (int i = 1; i <= numTestCases; i++) {
                    List<String> testInputs = new ArrayList<String>();
                    while (!done) {
                        try {
                            line = in.readLine();
                        } catch (NoSuchElementException exc) {
                            line = null;
                        }
                        if (line == null) done = true; else {
                            if (!line.equals("EOF")) {
                                testInputs.add(line);
                                LOGGER.debug("Input: " + line);
                            } else {
                                LOGGER.debug("Line: " + line);
                                done = true;
                            }
                        }
                    }
                    List<String> cmd = new ArrayList<String>();
                    File tempIn = File.createTempFile("rage", ".tmp");
                    File tempOut = File.createTempFile("rage", ".tmp", new File(System.getProperty("user.dir")));
                    tempOut.setWritable(true);
                    if (type.equals("processing")) {
                        cmd.add(props.getProperty("runme"));
                        cmd.add(question);
                        for (int j = 0; j < testInputs.size(); j++) {
                            cmd.add(testInputs.get(j));
                        }
                        cmd.add(" >" + tempOut.getName());
                    } else if (type.equals("raptor")) {
                        LOGGER.debug("Writing test inputs to temp input file");
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
                        FileOutputStream inFileStream = new FileOutputStream(tempIn);
                        FileChannel inChannel = inFileStream.getChannel();
                        inChannel.lock();
                        for (int j = 0; j < testInputs.size(); j++) {
                            buffer.put(testInputs.get(j).getBytes());
                            buffer.put(System.getProperty("line.separator").getBytes());
                            buffer.flip();
                            inChannel.write(buffer);
                            buffer.clear();
                        }
                        inFileStream.close();
                        inChannel.close();
                        LOGGER.debug("Building RAPTOR command");
                        cmd.add(props.getProperty("raptor"));
                        cmd.add("\"" + System.getProperty("user.dir") + System.getProperty("file.separator") + question + ".rap" + "\"");
                        cmd.add("/run");
                        cmd.add("\"" + tempIn.getCanonicalPath() + "\"");
                        cmd.add("\"" + tempOut.getCanonicalPath() + "\"");
                    } else {
                        LOGGER.error("ERROR:  Unsupported Option");
                    }
                    String callCommand = new String();
                    for (int j = 0; j < cmd.size(); j++) {
                        callCommand = callCommand.concat(cmd.get(j) + " ");
                    }
                    LOGGER.debug("Command: " + callCommand);
                    LOGGER.debug("Command Length: " + callCommand.length());
                    ProcessBuilder launcher = new ProcessBuilder();
                    Map<String, String> environment = launcher.environment();
                    launcher.redirectErrorStream(true);
                    launcher.directory(new File(System.getProperty("user.dir")));
                    launcher.command(cmd);
                    Process p = launcher.start();
                    Long startTimeInNanoSec = System.nanoTime();
                    Long delayInNanoSec;
                    try {
                        if (props.getProperty("infDetection") != null && props.getProperty("threshold") != null && props.getProperty("infDetection").equals("true")) {
                            try {
                                delayInNanoSec = Long.parseLong(props.getProperty("threshold")) * 1000000000;
                            } catch (NumberFormatException e) {
                                LOGGER.error("ERROR: Invalid Threshold " + "value.  Defaulting to 10");
                                delayInNanoSec = new Long(10 * 1000000000);
                            }
                            boolean timeFlag = true;
                            while (timeFlag) {
                                try {
                                    int val = p.exitValue();
                                    timeFlag = false;
                                    LOGGER.debug("Exit Value: " + val);
                                } catch (IllegalThreadStateException e) {
                                    Long elapsedTime = System.nanoTime() - startTimeInNanoSec;
                                    if (elapsedTime > delayInNanoSec) {
                                        LOGGER.warn("ERROR: Threshold time " + "exceeded.");
                                        p.destroy();
                                        timeFlag = false;
                                    }
                                    Thread.sleep(50);
                                }
                            }
                        } else {
                            p.waitFor();
                        }
                    } catch (InterruptedException ex) {
                        LOGGER.warn("Thread interrupted");
                    }
                    File newTemp = null;
                    BufferedReader inFile;
                    try {
                        LOGGER.debug("Output File: " + tempOut.getCanonicalPath());
                        LOGGER.debug("Output File Length: " + tempOut.length());
                        inFile = new BufferedReader(new FileReader(tempOut));
                    } catch (FileNotFoundException ex) {
                        LOGGER.warn("Warning: The file is in use by another " + "process");
                        newTemp = File.createTempFile("rage", ".tmp");
                        LOGGER.debug("New Temp: " + newTemp.getCanonicalPath());
                        inFile = new BufferedReader(new FileReader(newTemp));
                    }
                    String outputLine = null;
                    LOGGER.debug("Sending output back to server");
                    while ((outputLine = inFile.readLine()) != null) {
                        LOGGER.debug("Output: " + outputLine);
                        out.print(outputLine + "\r\n");
                    }
                    out.print("EOF\r\n");
                    LOGGER.debug("Sent: EOF");
                    out.flush();
                    inFile.close();
                    LOGGER.debug("Reading Response from Server");
                    line = in.readLine();
                    if (line.equals("CORRECT")) {
                        System.out.println("Test (" + i + "):\tCORRECT");
                        LOGGER.info("Test " + i + ":\tCORRECT");
                    } else {
                        System.out.println("Test (" + i + "):\tINCORRECT");
                        LOGGER.info("Test " + i + ":\tINCORRECT");
                        result = true;
                    }
                    LOGGER.debug("Deleting temp files");
                    tempIn.delete();
                    tempOut.delete();
                    if (newTemp != null) newTemp.delete();
                    done = false;
                }
            }
        } catch (IOException ioEx) {
            LOGGER.error("ERROR: Error connecting to server: " + ioEx.getLocalizedMessage());
            System.err.println("ERROR: Error connecting to server");
            System.exit(1);
        } catch (NumberFormatException numEx) {
            LOGGER.error("ERROR: Invalid port number in config file");
            System.err.println("ERROR: Invalid port number in config file");
            System.exit(1);
        } finally {
            LOGGER.debug("Closing IO resources");
            closeConnection(conn, in, out);
        }
        return result;
    }
}
