class BackupThread extends Thread {
    public void run(Socket controlconnection, ServerSocket dataSocket, Server s) {
        String username = null;
        String authLevel = null;
        String password = null;
        int auth = 0;
        FileOutputStream file = null;
        BufferedOutputStream buff = null;
        DataOutputStream data = null;
        InputStream in = null;
        BufferedInputStream bis = null;
        DataInputStream dataIn = null;
        BufferedReader inCtrl = null;
        String ctrlStr = null;
        PrintWriter outCtrl = null;
        try {
            inCtrl = new BufferedReader(new InputStreamReader(controlconnection.getInputStream()));
            outCtrl = new PrintWriter(controlconnection.getOutputStream(), true);
            System.out.println("Socket Connection Made");
            byte str = 0;
            String command = null;
            stop = 0;
            Socket dataconnection = dataSocket.accept();
            while (stop != 1) {
                if (!dataSocket.isBound()) {
                    dataSocket = new ServerSocket(9999);
                    dataSocket.setSoTimeout(30000);
                }
                ctrlStr = inCtrl.readLine();
                System.out.println("Client: " + ctrlStr);
                StringTokenizer st1 = new StringTokenizer(ctrlStr, " ");
                command = new String(st1.nextToken());
                if (command.matches("QUIT")) {
                    stop = 1;
                } else if (command.trim().equals("LOGIN")) {
                    String tempUser = new String(st1.nextToken());
                    DataTable dt = db.getTable("users");
                    DataTable select = dt.select("name = \"" + tempUser + "\";");
                    if (!select.isEmpty()) {
                        outCtrl.println("VALID");
                        Iterator b = select.dataRows.iterator();
                        DataRow dr = (DataRow) b.next();
                        Iterator j = dr.fields.iterator();
                        tempUser = j.next().toString();
                        tempUser = tempUser.substring(1, tempUser.length() - 1);
                        password = j.next().toString();
                        password = password.substring(1, password.length() - 1);
                        authLevel = j.next().toString();
                        authLevel = authLevel.substring(1, authLevel.length() - 1);
                        Date now = new Date();
                        Random r = new Random(now.getTime());
                        StringBuffer sb = new StringBuffer();
                        StringBuffer sbRandom = new StringBuffer();
                        char c;
                        int i;
                        for (c = 'a'; c <= 'z'; c++) sb.append(c);
                        for (c = 'A'; c <= 'Z'; c++) sb.append(c);
                        for (c = '0'; c <= '9'; c++) sb.append(c);
                        for (i = 0; i < 20; i++) sbRandom.append(sb.charAt(r.nextInt(62)));
                        String salt1 = new String(sbRandom);
                        Random r1 = new Random(now.getTime() * 2);
                        StringBuffer sb1 = new StringBuffer();
                        StringBuffer sbRandom1 = new StringBuffer();
                        for (c = 'a'; c <= 'z'; c++) sb1.append(c);
                        for (c = 'A'; c <= 'Z'; c++) sb1.append(c);
                        for (c = '0'; c <= '9'; c++) sb1.append(c);
                        for (i = 0; i < 20; i++) sbRandom1.append(sb1.charAt(r1.nextInt(62)));
                        String salt2 = new String(sbRandom1);
                        String salt = new String(salt1 + "." + salt2);
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        String hashData = new String(salt1 + password + salt2);
                        byte buf[] = hashData.getBytes();
                        md.update(buf);
                        byte hash[] = md.digest();
                        outCtrl.println(salt);
                        String hashString = new String();
                        for (i = 0; i < hash.length; i++) {
                            hashString = hashString + (int) hash[i];
                        }
                        String hashResult = new String(inCtrl.readLine());
                        if (hashResult.equals(hashString)) {
                            auth = 1;
                            username = tempUser;
                            outCtrl.println("Access Granted");
                        } else {
                            auth = 0;
                            outCtrl.println("Access Denied");
                            Thread.sleep(5000);
                        }
                    } else {
                        auth = 0;
                        outCtrl.println("INVALID");
                        Thread.sleep(5000);
                    }
                } else if (command.trim().equals("CHPASS") && auth != 0) {
                    password = st1.nextToken();
                    DataTable dt = db.getTable("users");
                    DataTable select = dt.select("name = \"" + username + "\";");
                    if (!select.isEmpty()) {
                        String[] updateRows = { "password" };
                        String[] newVals = { password };
                        if (dt.update(select, updateRows, newVals)) {
                            outCtrl.println("SUCCESS");
                        } else {
                            outCtrl.println("FAILURE");
                        }
                    } else {
                        outCtrl.println("FAILURE");
                    }
                } else if (command.trim().equals("ADDFRIEND") && auth != 0) {
                    String album = new String(st1.nextToken());
                    String friend = new String(st1.nextToken());
                    DataTable dt = db.getTable("friends");
                    DataTable select = dt.select("albumname = \"" + username + "." + album + "\";");
                    if (!select.isEmpty()) {
                        Iterator b = select.dataRows.iterator();
                        DataRow dr = (DataRow) b.next();
                        Iterator j = dr.fields.iterator();
                        String garbage = j.next().toString();
                        String tempFriends = j.next().toString();
                        tempFriends = tempFriends.substring(1, tempFriends.length() - 1);
                        if (tempFriends.indexOf(friend.trim()) == -1) {
                            if (!tempFriends.equals("")) {
                                tempFriends = tempFriends.trim() + "," + friend.trim();
                            } else {
                                tempFriends = friend.trim();
                            }
                            String[] updateRows = { "friends" };
                            String[] newVals = { tempFriends };
                            if (dt.update(select, updateRows, newVals)) {
                                outCtrl.println("SUCCESS");
                            } else {
                                outCtrl.println("FAILURE");
                            }
                        } else {
                            outCtrl.println("FAILURE");
                        }
                    } else {
                        outCtrl.println("FAILURE");
                    }
                } else if (command.trim().equals("RMFRIEND") && auth != 0) {
                    String album = new String(st1.nextToken());
                    String friend = new String(st1.nextToken());
                    DataTable dt = db.getTable("friends");
                    DataTable select = dt.select("albumname = \"" + username + "." + album + "\";");
                    if (!select.isEmpty()) {
                        Iterator b = select.dataRows.iterator();
                        DataRow dr = (DataRow) b.next();
                        Iterator j = dr.fields.iterator();
                        String garbage = j.next().toString();
                        String tempFriends = j.next().toString();
                        tempFriends = tempFriends.substring(1, tempFriends.length() - 1);
                        if (tempFriends.indexOf(friend.trim()) != -1) {
                            if (!tempFriends.equals("")) {
                                tempFriends = tempFriends.replaceAll(friend + ",", "");
                                tempFriends = tempFriends.replaceAll("," + friend, "");
                                tempFriends = tempFriends.replaceAll(friend, "");
                            }
                            String[] updateRows = { "friends" };
                            String[] newVals = { tempFriends };
                            if (dt.update(select, updateRows, newVals)) {
                                outCtrl.println("SUCCESS");
                            } else {
                                outCtrl.println("FAILURE");
                            }
                        } else {
                            outCtrl.println("FAILURE");
                        }
                    } else {
                        outCtrl.println("FAILURE");
                    }
                } else if (command.trim().equals("DELETE") && auth != 0) {
                    String filename = new String(st1.nextToken());
                    while (st1.hasMoreTokens()) {
                        filename = filename + " " + st1.nextToken();
                    }
                    File path = new File("serverImages\\" + filename);
                    StringTokenizer st2 = new StringTokenizer(filename, "\\");
                    String userPath = new String(st2.nextToken());
                    if (path.exists()) {
                        if ((userPath.trim().equals(username) || authLevel.trim().equals("2"))) {
                            if (st2.countTokens() == 1) {
                                String album = new String(st2.nextToken());
                                DataTable dt = db.getTable("albums");
                                DataTable select = dt.select("name = \"" + album + "\"" + "&&" + "owner = \"" + userPath + "\"" + ";");
                                if (!select.isEmpty()) {
                                    dt.delete(select);
                                }
                                DataTable dt1 = db.getTable("friends");
                                DataTable select1 = dt1.select("albumname = \"" + userPath + "." + album + "\";");
                                if (!select1.isEmpty()) {
                                    dt1.delete(select1);
                                }
                            }
                            if (path.isFile()) {
                                path.delete();
                            } else {
                                recursiveRemoveDir(path);
                            }
                            outCtrl.println("SUCCESS");
                        } else {
                            outCtrl.println("FAILURE");
                        }
                    } else {
                        outCtrl.println("FAILURE");
                    }
                } else if (command.trim().equals("GET") && auth != 0) {
                    try {
                        if (dataconnection.isClosed()) {
                            dataconnection = dataSocket.accept();
                        }
                        in = dataconnection.getInputStream();
                        bis = new BufferedInputStream(in);
                        dataIn = new DataInputStream(bis);
                        String filename = "";
                        if (st1.countTokens() > 0) {
                            filename = new String(st1.nextToken());
                        }
                        while (st1.hasMoreTokens()) {
                            filename = filename + " " + st1.nextToken();
                        }
                        File path = new File("serverImages\\" + filename);
                        StringTokenizer st2 = new StringTokenizer(filename, "\\");
                        String albumPath = null;
                        String userPath = null;
                        String tempFriends = null;
                        if (st2.countTokens() > 1) {
                            userPath = new String(st2.nextToken());
                            albumPath = new String(st2.nextToken());
                        }
                        DataTable dt;
                        DataTable select;
                        if (albumPath != null) {
                            dt = db.getTable("friends");
                            select = dt.select("albumname = \"" + userPath + "." + albumPath + "\";");
                            if (!select.isEmpty()) {
                                Iterator b = select.dataRows.iterator();
                                DataRow dr = (DataRow) b.next();
                                Iterator j = dr.fields.iterator();
                                String garbage = j.next().toString();
                                tempFriends = j.next().toString();
                                tempFriends = tempFriends.substring(1, tempFriends.length() - 1);
                            }
                        }
                        if (path.exists() && (albumPath == null || userPath.trim().equals(username.trim()) || tempFriends.indexOf(username.trim()) != -1) || authLevel.trim().equals("2")) {
                            String[] contents = path.list();
                            OutputStream out = dataconnection.getOutputStream();
                            BufferedOutputStream bos = new BufferedOutputStream(out);
                            DataOutputStream dataOut = new DataOutputStream(bos);
                            int fileReadInt = 0;
                            for (int i = 0; i < contents.length; i++) {
                                File tempPath = new File("serverImages\\" + filename + contents[i]);
                                outCtrl.print(contents[i]);
                                if (tempPath.isDirectory()) {
                                    outCtrl.print("\\");
                                }
                                outCtrl.print("\n");
                                outCtrl.flush();
                                if (tempPath.isFile()) {
                                    fileReadInt = 0;
                                    FileInputStream fileIn = new FileInputStream(tempPath);
                                    BufferedInputStream buffIn = new BufferedInputStream(fileIn);
                                    if (dataconnection.isClosed()) {
                                        dataconnection = dataSocket.accept();
                                    }
                                    out = dataconnection.getOutputStream();
                                    bos = new BufferedOutputStream(out);
                                    dataOut = new DataOutputStream(bos);
                                    while (true && fileReadInt != -1) {
                                        fileReadInt = buffIn.read();
                                        if (fileReadInt != -1) {
                                            dataOut.write(fileReadInt);
                                        }
                                    }
                                    dataOut.flush();
                                    fileIn.close();
                                    buffIn.close();
                                    dataOut.close();
                                }
                            }
                        }
                        outCtrl.println("!DONE");
                    } catch (Exception e) {
                        System.out.println("Error GET: " + e);
                    }
                } else if (command.trim().equals("PUT") && auth != 0) {
                    try {
                        if (dataconnection.isClosed()) {
                            dataconnection = dataSocket.accept();
                        }
                        in = dataconnection.getInputStream();
                        bis = new BufferedInputStream(in);
                        dataIn = new DataInputStream(bis);
                        in = dataconnection.getInputStream();
                        bis = new BufferedInputStream(in);
                        dataIn = new DataInputStream(bis);
                        String filename = new String(st1.nextToken());
                        File path = new File("serverImages\\" + filename);
                        StringTokenizer st2 = new StringTokenizer(filename, "\\");
                        String directory = new String();
                        int numTokens = st2.countTokens();
                        File tempPath;
                        String userPath = new String(st2.nextToken());
                        String tempDir = new String("serverImages\\" + userPath);
                        String albumPath = null;
                        if (st2.hasMoreTokens()) {
                            albumPath = new String(st2.nextToken());
                            tempDir = tempDir + "\\" + albumPath;
                            if (userPath.trim().equals(username)) {
                                DataTable dt = db.getTable("albums");
                                DataTable select = null;
                                if (!dt.isEmpty()) {
                                    select = dt.select("name = \"" + albumPath + "\"" + "&&" + "owner = \"" + userPath + "\"" + ";");
                                }
                                if (dt.isEmpty() || select.isEmpty()) {
                                    DataRow dr = new DataRow();
                                    dr.appendField("name");
                                    dr.appendField("owner");
                                    dr.update(0, albumPath);
                                    dr.update(1, userPath);
                                    dt.insert(dr);
                                }
                                DataTable dt1 = db.getTable("friends");
                                DataTable select1 = dt1.select("albumname = \"" + userPath + "." + albumPath + "\";");
                                System.out.println("BLAH");
                                if (select1.isEmpty()) {
                                    DataRow dr = new DataRow();
                                    dr.appendField("albumname");
                                    dr.appendField("friends");
                                    dr.update(1, "");
                                    dr.update(0, userPath + "." + albumPath);
                                    dt1.insert(dr);
                                }
                            }
                        }
                        if (userPath.trim().equals(username)) {
                            for (int i = 1; i < numTokens - 2; i++) {
                                tempDir = tempDir + "\\" + st2.nextToken();
                            }
                            tempPath = new File(tempDir);
                            tempPath.mkdirs();
                            if (filename != null) {
                                outCtrl.println("Putting " + filename + " on server");
                                file = new FileOutputStream("serverImages\\" + filename);
                                buff = new BufferedOutputStream(file);
                                data = new DataOutputStream(buff);
                            } else {
                                outCtrl.println("Error");
                            }
                            while (true) {
                                str = dataIn.readByte();
                                data.write(str);
                            }
                        } else {
                            outCtrl.println("Failure");
                        }
                    } catch (EOFException eof) {
                        buff.flush();
                        data.flush();
                        data.close();
                        buff.close();
                        dataIn.close();
                    }
                } else if (auth == 0) {
                    outCtrl.println("Error not authenticated!");
                } else {
                    outCtrl.println("Error " + command + " is not a valid command for " + username);
                }
            }
        } catch (Exception e) {
            System.out.println("Oops Error: " + e);
        }
    }
}
