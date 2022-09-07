class BackupThread extends Thread {
    public void processCommands(InputStream commandStream, OutputStream outputStream) throws IOException {
        m_reader = new BufferedReader(new InputStreamReader(commandStream));
        m_writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        String line;
        writeLine("");
        writeLine("Welcome to jSh3ll (Amazon S3 command shell for Java) (c) 2006 SilvaSoft, Inc.");
        writeLine("Type 'help' for command list.");
        writeLine("");
        while ((line = getLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            if (st.countTokens() == 0) {
                continue;
            }
            String cmd = st.nextToken();
            if (!connectedToS3Host() && !NO_CONNECTION_COMMANDS.contains(cmd)) {
                writeLine("Error: Not yet connected to S3; set host, user, and pass to continue");
                continue;
            }
            long starttime = System.currentTimeMillis();
            try {
                if (cmd.equals("bucket")) {
                    if (st.countTokens() != 1) {
                        writeLine("Error: bucket [name]");
                        continue;
                    }
                    setBucket(st.nextToken());
                    if (getBucket() != null) {
                        writeLine("Bucket set to '" + getBucket() + "'");
                    } else {
                        writeLine("Error: bucket is not set");
                    }
                } else if (cmd.equals("copy")) {
                    if (st.countTokens() < 1 || st.countTokens() > 5) {
                        writeLine("Error: copy <id> <src_bucket> <dest_bucket> [user] [password]");
                        continue;
                    }
                    String id = st.nextToken();
                    String src_bucket = st.nextToken();
                    String dest_bucket = st.nextToken();
                    boolean newAccount = false;
                    String user = null;
                    String password = null;
                    if (st.countTokens() == 5) {
                        newAccount = true;
                        user = st.nextToken();
                        password = st.nextToken();
                    }
                    AWSAuthConnection src_conn = new AWSAuthConnection(m_user, m_pass, true, m_host);
                    AWSAuthConnection dest_conn = null;
                    if (newAccount) {
                        dest_conn = new AWSAuthConnection(user, password, true, m_host);
                    } else {
                        dest_conn = new AWSAuthConnection(m_user, m_pass, true, m_host);
                    }
                    GetStreamResponse getResponse = src_conn.getStream(src_bucket, id, null);
                    if (RESPONSE_OK != getResponse.connection.getResponseCode()) {
                        writeLine("Error: Could not find item '" + src_bucket + "/" + id + "'");
                    } else {
                        if (RESPONSE_OK == dest_conn.putStream(dest_bucket, id, new S3StreamObject(getResponse.connection.getInputStream(), null), null).connection.getResponseCode()) {
                            src_conn = null;
                            dest_conn = null;
                            writeLine("Copied '" + src_bucket + "/" + id + "' to '" + dest_bucket + "/" + id + "'");
                        } else {
                            writeLine("Error: Could not copy '" + src_bucket + "/" + id + "' to '" + dest_bucket + "/" + id + "'");
                        }
                    }
                } else if (cmd.equals("copyall")) {
                    if (st.countTokens() < 1 || st.countTokens() > 5) {
                        writeLine("Error: copyall [prefix] <src_bucket> <dest_bucket> [user] [password]");
                        continue;
                    }
                    boolean newAccount = false;
                    String prefix = null;
                    String src_bucket = null;
                    String dest_bucket = null;
                    String user = null;
                    String password = null;
                    if (st.countTokens() == 5) {
                        newAccount = true;
                        prefix = st.nextToken();
                        src_bucket = st.nextToken();
                        dest_bucket = st.nextToken();
                        user = st.nextToken();
                        password = st.nextToken();
                    } else if (st.countTokens() == 4) {
                        newAccount = true;
                        src_bucket = st.nextToken();
                        dest_bucket = st.nextToken();
                        user = st.nextToken();
                        password = st.nextToken();
                    } else if (st.countTokens() == 3) {
                        prefix = st.nextToken();
                        src_bucket = st.nextToken();
                        dest_bucket = st.nextToken();
                    } else if (st.countTokens() == 2) {
                        src_bucket = st.nextToken();
                        dest_bucket = st.nextToken();
                    } else {
                        writeLine("Error: copyall [prefix] <src_bucket> <dest_bucket> [user] [password]");
                        continue;
                    }
                    AWSAuthConnection src_conn = new AWSAuthConnection(m_user, m_pass, true, m_host);
                    AWSAuthConnection dest_conn = null;
                    if (newAccount) {
                        dest_conn = new AWSAuthConnection(user, password, true, m_host);
                    } else {
                        dest_conn = new AWSAuthConnection(m_user, m_pass, true, m_host);
                    }
                    List<ListEntry> ids = new ArrayList<ListEntry>();
                    String lastid = null;
                    int max = 0;
                    while (true) {
                        List<ListEntry> results = src_conn.listBucket(src_bucket, prefix, lastid, null, null).entries;
                        if (results == null || results.size() == 0) {
                            break;
                        }
                        lastid = ((ListEntry) results.get(results.size() - 1)).key;
                        ids.addAll(results);
                        if (ids.size() == max) {
                            break;
                        }
                    }
                    String displayName = "";
                    for (ListEntry id : ids) {
                        GetStreamResponse getResponse = src_conn.getStream(src_bucket, id.key, null);
                        if (RESPONSE_OK != getResponse.connection.getResponseCode()) {
                            writeLine("Error: Could not find item '" + src_bucket + "/" + id + "'");
                        } else {
                            if (RESPONSE_OK == dest_conn.putStream(dest_bucket, id.key, new S3StreamObject(getResponse.connection.getInputStream(), null), null).connection.getResponseCode()) {
                                writeLine("Copied '" + src_bucket + "/" + id + "' to '" + dest_bucket + "/" + id + "'");
                            } else {
                                writeLine("Error: Could not copy '" + src_bucket + "/" + id + "' to '" + dest_bucket + "/" + id + "'");
                            }
                        }
                    }
                    src_conn = null;
                    dest_conn = null;
                } else if (cmd.equals("count")) {
                    if (st.countTokens() > 1) {
                        writeLine("Error: count [prefix]");
                        continue;
                    }
                    String prefix = null;
                    if (st.countTokens() > 0) {
                        prefix = st.nextToken();
                    }
                    int count = countItems(prefix);
                    if (count >= 0) {
                        writeLine(count + " item(s)");
                    }
                } else if (cmd.equals("createbucket")) {
                    if (st.countTokens() != 0 && st.countTokens() != 1) {
                        writeLine("Error: createbucket [private|public-read|public-read-write|authenticated-read]");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String acl = "";
                    Map<String, List<String>> headers = null;
                    if (st.countTokens() == 1) {
                        acl = st.nextToken();
                        if (!ACL_TYPES.contains(acl)) {
                            writeLine("Error: invalid ACL type");
                            continue;
                        }
                        headers = new LinkedHashMap<String, List<String>>();
                        List<String> headerList = new ArrayList<String>();
                        headerList.add(acl);
                        headers.put("x-amz-acl", headerList);
                    }
                    Response response = m_authConn.createBucket(m_bucket, headers);
                    if (RESPONSE_OK == response.connection.getResponseCode()) {
                        writeLine("Created bucket '" + m_bucket + "'");
                    } else {
                        writeLine(response.connection.getResponseMessage());
                    }
                } else if (cmd.equals("delete")) {
                    if (st.countTokens() != 1) {
                        writeLine("Error: delete <id>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String id = st.nextToken();
                    Response response = m_authConn.delete(m_bucket, id, null);
                    if (NO_CONTENT.equals(response.connection.getResponseMessage())) {
                        writeLine("Deleted '" + m_bucket + "/" + id + "'");
                    } else {
                        writeLine(response.connection.getResponseMessage());
                    }
                } else if (cmd.equals("deleteall")) {
                    if (st.countTokens() > 1) {
                        writeLine("Error: deleteall [prefix]");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String prefix = null;
                    if (st.countTokens() > 0) {
                        prefix = st.nextToken();
                    }
                    List<ListEntry> items = new ArrayList<ListEntry>();
                    String lastid = null;
                    while (true) {
                        List<ListEntry> results = m_authConn.listBucket(m_bucket, prefix, lastid, null, null).entries;
                        if (results == null || results.size() == 0) {
                            break;
                        }
                        lastid = ((ListEntry) results.get(results.size() - 1)).key;
                        items.addAll(results);
                    }
                    int deletecount = 0;
                    int nodeletecount = 0;
                    if (items != null && !items.isEmpty()) {
                        for (ListEntry item : items) {
                            if (NO_CONTENT.equals(m_authConn.delete(m_bucket, item.key, null).connection.getResponseMessage())) {
                                deletecount++;
                            } else {
                                nodeletecount++;
                            }
                        }
                        writeLine("Deleted " + deletecount + " item(s), could not delete " + nodeletecount + " item(s)");
                    } else {
                        writeLine("No items in bucket '" + m_bucket + "'");
                    }
                } else if (cmd.equals("deletebucket")) {
                    if (st.countTokens() != 0) {
                        writeLine("Error: deletebucket");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    Response response = m_authConn.deleteBucket(m_bucket, null);
                    if (NO_CONTENT.equals(response.connection.getResponseMessage())) {
                        writeLine("Deleted bucket '" + m_bucket + "'");
                        m_bucket = null;
                    } else {
                        writeLine(response.connection.getResponseMessage());
                    }
                } else if (cmd.equals("exit") || cmd.equals("quit")) {
                    writeLine("Goodbye");
                    return;
                } else if (cmd.equals("get")) {
                    if (st.countTokens() != 1) {
                        writeLine("Error: get <id>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String id = st.nextToken();
                    byte[] data = m_authConn.get(m_bucket, id, null).object.data;
                    if (data == null) {
                        writeLine("Item '" + id + "' not found");
                    } else {
                        writeLine(new String(data));
                    }
                } else if (cmd.equals("getacl")) {
                    if (st.countTokens() != 2) {
                        writeLine("Error: getacl [bucket|item] <id>");
                        continue;
                    }
                    String objectType = "";
                    String id = "";
                    if (st.hasMoreElements()) {
                        objectType = st.nextToken();
                        id = st.nextToken();
                    }
                    byte[] data = null;
                    S3Object s3Object = null;
                    if (objectType.equals("bucket")) {
                        s3Object = m_authConn.getBucketACL(id, null).object;
                        if (s3Object == null) {
                            writeLine("Error: bucket '" + id + "' not found");
                            continue;
                        }
                        data = s3Object.data;
                    } else if (objectType.equals("item")) {
                        if (m_bucket == null) {
                            writeLine("Error: bucket is not set");
                            continue;
                        }
                        s3Object = m_authConn.getACL(m_bucket, id, null).object;
                        if (s3Object == null) {
                            writeLine("Error: item '" + m_bucket + "/" + id + "' not found");
                            continue;
                        }
                        data = s3Object.data;
                    } else {
                        writeLine("Error: getacl [bucket|item] <id>");
                        continue;
                    }
                    if (data == null) {
                        if (objectType.equals("item")) {
                            writeLine("Error: item '" + m_bucket + "/" + id + "' not found");
                        } else if (objectType.equals("bucket")) {
                            writeLine("Error: bucket '" + id + "' not found");
                        }
                    } else {
                        writeLine(new String(data));
                    }
                } else if (cmd.equals("getfile")) {
                    if (st.countTokens() != 2) {
                        writeLine("Error: getfile <id> <file>");
                        continue;
                    }
                    String id = st.nextToken();
                    String filename = st.nextToken();
                    S3StreamObject s3Object = null;
                    s3Object = m_authConn.getStream(m_bucket, id, null).object;
                    if (s3Object.stream == null) {
                        writeLine("Error: item '" + m_bucket + "/" + id + "' not found");
                        continue;
                    }
                    FileOutputStream datafile = new FileOutputStream(filename);
                    try {
                        byte[] buf = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = s3Object.stream.read(buf)) > 0) {
                            datafile.write(buf, 0, bytesRead);
                        }
                        s3Object.stream.close();
                    } finally {
                        datafile.close();
                    }
                    writeLine("Got item '" + m_bucket + "/" + id + "' as '" + filename + "'");
                } else if (cmd.equals("getfilez")) {
                    if (st.countTokens() != 2) {
                        writeLine("Error: getfilez <id> <file>");
                        continue;
                    }
                    String id = st.nextToken();
                    String filename = st.nextToken();
                    byte[] dataGzipped = m_authConn.get(m_bucket, id, null).object.data;
                    byte[] data = S3Helper.decompressBytes(dataGzipped);
                    if (data == null) {
                        writeLine("Error: item '" + m_bucket + "/" + id + "' not found");
                    } else {
                        FileOutputStream datafile = new FileOutputStream(filename);
                        try {
                            datafile.write(data);
                        } finally {
                            datafile.close();
                        }
                        writeLine("Got item '" + m_bucket + "/" + id + "' as '" + filename + "'");
                    }
                } else if (cmd.equals("gettorrent")) {
                    if (st.countTokens() != 1) {
                        writeLine("Error: gettorrent <id>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String id = st.nextToken();
                    byte[] data = m_authConn.getTorrent(m_bucket, id, null).object.data;
                    if (data == null) {
                        writeLine("Error: item '" + m_bucket + "/" + id + "' not found");
                    } else {
                        FileOutputStream datafile = new FileOutputStream(id + ".torrent");
                        try {
                            datafile.write(data);
                        } finally {
                            datafile.close();
                        }
                        writeLine("Got torrent '" + id + ".torrent" + "'");
                    }
                } else if (cmd.equals("head")) {
                    if (st.countTokens() != 2) {
                        writeLine("Error: head [bucket|item] <id>");
                        continue;
                    }
                    if (st.hasMoreTokens()) {
                        String objectType = st.nextToken();
                        String id = st.nextToken();
                        String metadata = null;
                        S3Object s3Object = null;
                        if (objectType.equals("item")) {
                            if (m_bucket == null) {
                                writeLine("Error: bucket is not set");
                                continue;
                            }
                            s3Object = m_authConn.get(m_bucket, id, null).object;
                            if (s3Object != null) {
                                metadata = s3Object.metadata.toString();
                            } else {
                                writeLine("Error: item '" + m_bucket + "/" + id + "' not found");
                                continue;
                            }
                            writeLine("Head '" + m_bucket + "/" + id + "' = " + metadata);
                        }
                        if (objectType.equals("bucket")) {
                            s3Object = m_authConn.get(id, null, null).object;
                            if (s3Object != null) {
                                metadata = s3Object.metadata.toString();
                            } else {
                                writeLine("Error: bucket '" + id + "' not found");
                                continue;
                            }
                            writeLine("Head bucket '" + id + "' = " + metadata);
                        }
                    }
                } else if (cmd.equals("help")) {
                    printHelp();
                } else if (cmd.equals("host")) {
                    if (st.countTokens() > 1) {
                        writeLine("Error: host [hostname]");
                        continue;
                    }
                    if (st.hasMoreTokens()) {
                        setHost(st.nextToken());
                        if (m_host != null && m_user != null & m_pass != null) initAWSAuthConnection(m_host, m_user, m_pass);
                    } else {
                        if (getHost() != null) {
                            writeLine("host = " + getHost());
                        } else {
                            writeLine("Error: host is not set");
                        }
                    }
                } else if (cmd.equals("list")) {
                    if (st.countTokens() > 2) {
                        writeLine("Error: list [prefix] [max]");
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String prefix = null;
                    int max = 0;
                    if (st.hasMoreTokens()) {
                        prefix = st.nextToken();
                    }
                    if (st.hasMoreTokens()) {
                        try {
                            max = Integer.parseInt(st.nextToken());
                        } catch (NumberFormatException e) {
                            writeLine(e.getMessage());
                            continue;
                        }
                        if (max < 0) {
                            writeLine("Error: max must be >= 0");
                            continue;
                        }
                    }
                    Integer maxKeys = null;
                    if (max > 0) {
                        maxKeys = new Integer(max);
                    }
                    if (prefix != null && prefix.equals("*")) {
                        prefix = null;
                    }
                    writeLine("Item list for bucket '" + m_bucket + "'");
                    List<ListEntry> ids = new ArrayList<ListEntry>();
                    String lastid = null;
                    while (true) {
                        List<ListEntry> results = m_authConn.listBucket(m_bucket, prefix, lastid, maxKeys, null).entries;
                        if (results == null || results.size() == 0) {
                            break;
                        }
                        lastid = ((ListEntry) results.get(results.size() - 1)).key;
                        ids.addAll(results);
                        if (ids.size() == max) {
                            break;
                        }
                    }
                    String displayName = "";
                    for (ListEntry id : ids) {
                        displayName = id.owner != null ? id.owner.displayName : "unknown";
                        writeLine("key=" + id.key + ", owner=" + displayName + ", size=" + id.size + " bytes, last modified=" + id.lastModified);
                    }
                } else if (cmd.equals("listrss")) {
                    if (st.countTokens() > 2) {
                        writeLine("Error: listrss [prefix] [max]");
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String prefix = null;
                    int max = 0;
                    if (st.hasMoreTokens()) {
                        prefix = st.nextToken();
                    }
                    if (st.hasMoreTokens()) {
                        try {
                            max = Integer.parseInt(st.nextToken());
                        } catch (NumberFormatException e) {
                            writeLine(e.getMessage());
                            continue;
                        }
                        if (max < 0) {
                            writeLine("Error: max must be >= 0");
                            continue;
                        }
                    }
                    Integer maxKeys = null;
                    if (max > 0) {
                        maxKeys = new Integer(max);
                    }
                    if (prefix != null && prefix.equals("*")) {
                        prefix = null;
                    }
                    writeLine("Item list for bucket '" + m_bucket + "'");
                    List<ListEntry> ids = new ArrayList<ListEntry>();
                    String lastid = null;
                    while (true) {
                        List<ListEntry> results = m_authConn.listBucket(m_bucket, prefix, lastid, maxKeys, null).entries;
                        if (results == null || results.size() == 0) {
                            break;
                        }
                        lastid = ((ListEntry) results.get(results.size() - 1)).key;
                        ids.addAll(results);
                        if (ids.size() == max) {
                            break;
                        }
                    }
                    String rssDoc = createRSSDocument(ids);
                    writeLine(rssDoc);
                } else if (cmd.equals("listatom")) {
                    if (st.countTokens() > 2) {
                        writeLine("Error: listatom [prefix] [max]");
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String prefix = null;
                    int max = 0;
                    if (st.hasMoreTokens()) {
                        prefix = st.nextToken();
                    }
                    if (st.hasMoreTokens()) {
                        try {
                            max = Integer.parseInt(st.nextToken());
                        } catch (NumberFormatException e) {
                            writeLine(e.getMessage());
                            continue;
                        }
                        if (max < 0) {
                            writeLine("Error: max must be >= 0");
                            continue;
                        }
                    }
                    Integer maxKeys = null;
                    if (max > 0) {
                        maxKeys = new Integer(max);
                    }
                    if (prefix != null && prefix.equals("*")) {
                        prefix = null;
                    }
                    writeLine("Item list for bucket '" + m_bucket + "'");
                    List<ListEntry> ids = new ArrayList<ListEntry>();
                    String lastid = null;
                    while (true) {
                        List<ListEntry> results = m_authConn.listBucket(m_bucket, prefix, lastid, maxKeys, null).entries;
                        if (results == null || results.size() == 0) {
                            break;
                        }
                        lastid = ((ListEntry) results.get(results.size() - 1)).key;
                        ids.addAll(results);
                        if (ids.size() == max) {
                            break;
                        }
                    }
                    String atomDoc = createAtomDocument(ids);
                    writeLine(atomDoc);
                } else if (cmd.equals("listbuckets")) {
                    if (st.countTokens() != 0) {
                        writeLine("Error: listbuckets");
                        continue;
                    }
                    List<Bucket> buckets = m_authConn.listAllMyBuckets(null).entries;
                    if (buckets != null) {
                        for (Bucket bucket : buckets) {
                            writeLine(bucket.name + " - " + bucket.creationDate);
                        }
                    }
                } else if (cmd.equals("pass")) {
                    if (st.countTokens() > 1) {
                        writeLine("Error: pass [username]");
                        continue;
                    }
                    if (st.hasMoreTokens()) {
                        setPass(st.nextToken());
                        if (m_host != null && m_user != null & m_pass != null) initAWSAuthConnection(m_host, m_user, m_pass);
                    } else {
                        if (getPass() != null) {
                            writeLine("pass = " + getPass());
                        } else {
                            writeLine("Error: pass is not set");
                        }
                    }
                } else if (cmd.equals("put")) {
                    if (st.countTokens() < 2) {
                        writeLine("Error: put <id> <data>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String id = st.nextToken();
                    String restOfLine = line.substring(line.indexOf(id) + id.length() + 1);
                    Response response = m_authConn.put(m_bucket, id, new S3Object(restOfLine.getBytes(), null), null);
                    if (RESPONSE_OK == response.connection.getResponseCode()) {
                        writeLine("Stored item '" + m_bucket + "/" + id + "'");
                    } else {
                        writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                        writeLine(response.connection.getResponseMessage());
                    }
                } else if (cmd.equals("putdir")) {
                    if (st.countTokens() < 1) {
                        writeLine("Error: putdir <dir>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    boolean ok = false;
                    String dirname = st.nextToken();
                    File dir = new File(dirname);
                    String[] files = dir.list();
                    for (String file : files) {
                        String id = file;
                        ok = putFileStream(id, dir.getAbsolutePath() + File.separatorChar + file, null);
                        if (!ok) {
                            writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                            continue;
                        } else {
                            writeLine("Stored item '" + m_bucket + "/" + id + "'");
                            continue;
                        }
                    }
                } else if (cmd.equals("putdirwacl")) {
                    if (st.countTokens() < 2) {
                        writeLine("Error: putdir <dir> [private|public-read|public-read-write|authenticated-read]");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    boolean ok = false;
                    String dirname = st.nextToken();
                    String acl = st.nextToken();
                    if (!ACL_TYPES.contains(acl)) {
                        writeLine("Error: invalid ACL type");
                        continue;
                    }
                    File dir = new File(dirname);
                    String[] files = dir.list();
                    for (String file : files) {
                        String id = file;
                        ok = putFileStream(id, dir.getAbsolutePath() + File.separatorChar + file, acl);
                        if (!ok) {
                            writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                            continue;
                        } else {
                            writeLine("Stored item '" + m_bucket + "/" + id + "'");
                            continue;
                        }
                    }
                } else if (cmd.equals("putfile")) {
                    if (st.countTokens() != 2) {
                        writeLine("Error: putfile <id> <file>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket not set");
                        continue;
                    }
                    String id = st.nextToken();
                    String file = st.nextToken();
                    boolean ok = false;
                    ok = putFileStream(id, file, null);
                    if (!ok) {
                        writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                    } else {
                        writeLine("Stored item '" + m_bucket + "/" + id + "'");
                    }
                } else if (cmd.equals("putfilez")) {
                    if (st.countTokens() != 2) {
                        writeLine("Error: putfilez <id> <file>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    String id = st.nextToken();
                    String file = st.nextToken();
                    boolean ok = false;
                    ok = putFile(id, file, null, true);
                    if (!ok) {
                        writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                    } else {
                        writeLine("Stored item '" + m_bucket + "/" + id + "'");
                    }
                } else if (cmd.equals("putfilewacl")) {
                    if (st.countTokens() != 3) {
                        writeLine("Error: putfilewacl <id> <file> [private|public-read|public-read-write|authenticated-read]");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    boolean ok = false;
                    String id = st.nextToken();
                    String file = st.nextToken();
                    String acl = st.nextToken();
                    if (!ACL_TYPES.contains(acl)) {
                        writeLine("Error: invalid ACL type");
                        continue;
                    }
                    ok = putFileStream(id, file, acl);
                    if (!ok) {
                        writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                    } else {
                        writeLine("Stored item '" + m_bucket + "/" + id + "'");
                    }
                } else if (cmd.equals("putfilezwacl")) {
                    if (st.countTokens() != 3) {
                        writeLine("Error: putfilezwacl <id> <file> [private|public-read|public-read-write|authenticated-read]");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket is not set");
                        continue;
                    }
                    boolean ok = false;
                    String id = st.nextToken();
                    String file = st.nextToken();
                    String acl = st.nextToken();
                    if (!ACL_TYPES.contains(acl)) {
                        writeLine("Error: invalid ACL type");
                        continue;
                    }
                    ok = putFile(id, file, acl, true);
                    if (!ok) {
                        writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                    } else {
                        writeLine("Stored item '" + m_bucket + "/" + id + "'");
                    }
                } else if (cmd.equals("putfilecontenttype")) {
                    if (st.countTokens() != 3) {
                        writeLine("Error: putfile <id> <file> <content-type>");
                        continue;
                    }
                    if (m_bucket == null) {
                        writeLine("Error: bucket not set");
                        continue;
                    }
                    String id = st.nextToken();
                    String file = st.nextToken();
                    String contentType = st.nextToken();
                    boolean ok = false;
                    ok = putFileStreamContentType(id, file, null, contentType);
                    if (!ok) {
                        writeLine("Error: unable to store item '" + m_bucket + "/" + id + "'");
                    } else {
                        writeLine("Stored item '" + m_bucket + "/" + id + "' with content type " + contentType);
                    }
                } else if (cmd.equals("setacl")) {
                    if (st.countTokens() != 3) {
                        writeLine("Error: setacl [bucket|item] <id> [private|public-read|public-read-write|authenticated-read]");
                        continue;
                    }
                    String objectType = "";
                    String id = "";
                    String acl = "";
                    if (st.hasMoreTokens()) {
                        objectType = st.nextToken();
                        id = st.nextToken();
                        acl = st.nextToken();
                    }
                    if (objectType.equals("bucket")) {
                        S3Object s3Object = null;
                        s3Object = m_authConn.getACL(id, null, null).object;
                        if (s3Object == null) {
                            writeLine("Error: bucket '" + id + "' not found");
                            continue;
                        }
                        String currentACL = new String(s3Object.data);
                        String currenId = currentACL.substring(currentACL.indexOf("<ID>") + 4, currentACL.indexOf("</ID>"));
                        String aclXmlDoc = "";
                        if (acl.equals("private")) {
                            aclXmlDoc = this.getACLTemplatePrivate(currenId);
                        } else if (acl.equals("public-read")) {
                            aclXmlDoc = this.getACLTemplatePublicRead(currenId);
                        } else if (acl.equals("public-read-write")) {
                            aclXmlDoc = this.getACLTemplatePublicReadWrite(currenId);
                        } else {
                            writeLine(acl + " not supported at this time.");
                            continue;
                        }
                        Response response = m_authConn.putBucketACL(id, aclXmlDoc, null);
                        if (RESPONSE_OK == response.connection.getResponseCode()) {
                            writeLine("Set ACL for bucket '" + id + "' to " + acl);
                        } else {
                            writeLine("Error: could not set ACL for bucket '" + id + "' to " + acl);
                            writeLine(response.connection.getResponseMessage());
                        }
                    } else if (objectType.equals("item")) {
                        if (m_bucket == null) {
                            writeLine("Error: bucket is not set");
                            continue;
                        }
                        S3Object s3Object = null;
                        s3Object = m_authConn.getACL(m_bucket, id, null).object;
                        if (s3Object == null) {
                            writeLine("Error: item '" + m_bucket + "/" + id + "' not found");
                            continue;
                        }
                        String currentACL = new String(s3Object.data);
                        String currentId = currentACL.substring(currentACL.indexOf("<ID>") + 4, currentACL.indexOf("</ID>"));
                        String aclXmlDoc = "";
                        if (acl.equals("private")) {
                            aclXmlDoc = this.getACLTemplatePrivate(currentId);
                        } else if (acl.equals("public-read")) {
                            aclXmlDoc = this.getACLTemplatePublicRead(currentId);
                        } else if (acl.equals("public-read-write")) {
                            aclXmlDoc = this.getACLTemplatePublicReadWrite(currentId);
                        } else {
                            writeLine(acl + " not supported at this time.");
                            continue;
                        }
                        Response response = m_authConn.putACL(m_bucket, id, aclXmlDoc, null);
                        if (RESPONSE_OK == response.connection.getResponseCode()) {
                            writeLine("Set ACL for item '" + m_bucket + "/" + id + "' to " + acl);
                        } else {
                            writeLine("Error: could not set ACL for item '" + m_bucket + "/" + id + "' to " + acl);
                            writeLine(response.connection.getResponseMessage());
                        }
                    } else {
                        writeLine("Error: setacl [bucket|item] <id> [private|public-read|public-read-write|authenticated-read]");
                        continue;
                    }
                } else if (cmd.equals("threads")) {
                    if (st.countTokens() > 1) {
                        writeLine("Error: threads [num]");
                        continue;
                    }
                    if (st.hasMoreTokens()) {
                        try {
                            setThreads(Integer.parseInt(st.nextToken()));
                        } catch (NumberFormatException e) {
                            writeLine(e.getMessage());
                        }
                    } else {
                        writeLine("threads = " + getThreads());
                    }
                } else if (cmd.equals("time")) {
                    if (st.countTokens() > 1) {
                        writeLine("Error: time [none|long|all]");
                        continue;
                    }
                    if (st.hasMoreTokens()) {
                        final String mode = st.nextToken();
                        try {
                            setTimingMode(TimingMode.valueOf(TimingMode.class, mode.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            writeLine("Error: time [none|long|all]");
                        }
                    } else {
                        writeLine("time = " + getTimingMode());
                    }
                } else if (cmd.equals("user")) {
                    if (st.countTokens() > 1) {
                        writeLine("Error: user [username]");
                        continue;
                    }
                    if (st.hasMoreTokens()) {
                        setUser(st.nextToken());
                        if (m_host != null && m_user != null & m_pass != null) initAWSAuthConnection(m_host, m_user, m_pass);
                    } else {
                        if (getUser() != null) {
                            writeLine("user = " + getUser());
                        } else {
                            writeLine("Error: user is not set");
                        }
                    }
                } else {
                    writeLine("Error: unknown command");
                }
            } catch (Exception e) {
                writeLine(e.getMessage());
            }
            long runtime = System.currentTimeMillis() - starttime;
            if (m_timingMode == TimingMode.ALL || m_timingMode == TimingMode.LONG && runtime > LONG_COMMAND) {
                writeLine("[runtime: " + formatRuntime(runtime) + "]");
            }
        }
    }
}
