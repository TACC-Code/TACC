class BackupThread extends Thread {
    public Collection transfer(DataSourceObject datasource) throws RemoteException, LogFileExtractionException {
        initValues();
        Vector logfiles = new Vector();
        LogFileObject curlogfile = null;
        websiteid = datasource.getRw_webSiteID();
        datasourceid = datasource.getRw_id();
        server = datasource.getRw_serverAddress();
        sfilename = datasource.getRw_filename();
        apacheformat = datasource.getRw_apacheFormat();
        logfileformat = datasource.getLogfileFormat().getRw_name();
        long curTime = 0;
        Vector filenames = new Vector();
        int transferredfilescount = 0;
        try {
            sfilename = sfilename.replace('\\', '/');
            if (sfilename.indexOf('|') != -1) {
                int j = sfilename.indexOf('|');
                StringTokenizer tok = new StringTokenizer(sfilename, "|");
                while (tok.hasMoreTokens()) filenames.add(tok.nextToken().trim());
            }
            filenames.add(sfilename);
            if (!server.toLowerCase().startsWith("http://")) server = "http://" + server;
            if (server.endsWith("/")) server = server.substring(0, server.length() - 1);
            Enumeration filenamesEnum = filenames.elements();
            while (filenamesEnum.hasMoreElements()) {
                String curfilename = (String) filenamesEnum.nextElement();
                if (!curfilename.startsWith("/")) curfilename = "/" + curfilename;
                HttpURLConnection httpconn = null;
                URL url = new URL(server + curfilename);
                URLConnection conn = url.openConnection();
                if (conn instanceof HttpURLConnection) httpconn = (HttpURLConnection) conn;
                httpconn.setRequestMethod("GET");
                long sfiledate = httpconn.getDate();
                long sfilelastmodified = httpconn.getLastModified();
                LogFileStreamWrapper wrapper = new LogFileStreamWrapper(conn.getInputStream(), curfilename, sfilelastmodified);
                boolean morefilesavailable = true;
                ByteArrayOutputStream bout = null;
                while (morefilesavailable) {
                    bout = new ByteArrayOutputStream();
                    Deflater compressor = new Deflater();
                    compressor.setLevel(Deflater.BEST_SPEED);
                    DeflaterOutputStream zip = new DeflaterOutputStream(bout, compressor);
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    long bytecounter = 0;
                    byte read;
                    int limitcounter = 1;
                    ByteBuffer bf = ByteBuffer.allocate(1024 * 1024);
                    while ((read = (byte) wrapper.read()) != -1) {
                        md5.update(read);
                        bytecounter++;
                        if (!bf.hasRemaining()) {
                            zip.write(bf.array());
                            bf = ByteBuffer.allocate(1024 * 1024);
                            System.out.println("Transferred " + limitcounter + " MB of file(s)");
                            limitcounter++;
                        }
                        bf.put(read);
                    }
                    zip.write(bf.array());
                    zip.close();
                    bf.clear();
                    bf = null;
                    zip = null;
                    String checksum = getMD5Hash(md5);
                    Context jndiContext = new InitialContext();
                    Object logfileRef = jndiContext.lookup(LogFileHome.COMP_NAME);
                    LogFileHome logfilehome = (LogFileHome) PortableRemoteObject.narrow(logfileRef, LogFileHome.class);
                    Collection transferredfiles = logfilehome.findByWebsiteChecksum(websiteid, checksum);
                    String currentfilename = wrapper.getFilename();
                    if (transferredfiles.size() < 1) {
                        curlogfile = logfilehome.create(primkeygen.getUniqueId_long(), currentfilename);
                        curTime = System.currentTimeMillis();
                        datetransferred = new java.util.Date(curTime);
                        long filedatelong = wrapper.getFiledate();
                        if (filedatelong > 0) {
                            filedate = new Date(filedatelong);
                        } else {
                            filedate = new java.util.Date(curTime);
                        }
                        curlogfile.setRw_checksum(checksum);
                        curlogfile.setRw_filesize(bytecounter);
                        curlogfile.setRw_websiteID(websiteid);
                        curlogfile.setRw_datasourceID(datasourceid);
                        curlogfile.setRw_serverAddress(server);
                        curlogfile.setRw_processState("TRANSFERRED");
                        curlogfile.setRw_dateTransferred(datetransferred);
                        curlogfile.setRw_fileDate(filedate);
                        curlogfile.setRw_file(bout.toByteArray());
                        bout.close();
                        bout = null;
                        curlogfile.setRw_logFileFormat(logfileformat);
                        if (apacheformat != null && logfileformat.toUpperCase().equals("APACHE")) {
                            if (!apacheformat.equals("")) {
                                curlogfile.setRw_apacheFormatString(apacheformat);
                            }
                        }
                        logfiles.add(curlogfile);
                        transferredfilescount++;
                    } else {
                        messenger.sendMonitorError("File " + currentfilename + " already in Database (website #" + websiteid + ")");
                    }
                    morefilesavailable = wrapper.nextEntry();
                }
                wrapper.close();
                wrapper = null;
            }
            if (transferredfilescount == 0) throw new LogFileExtractionException("No logfiles transferred for website #" + websiteid);
            return logfiles;
        } catch (IOException e) {
            e.printStackTrace();
            throw new LogFileExtractionException("Could not find any matching file on server or file is unreadable (IO Error)");
        } catch (OutOfMemoryError e) {
            throw new LogFileExtractionException("Not enough memory (RAM) to store the logfile -> extend JVM memory via start up switches");
        } catch (Exception e) {
            e.printStackTrace();
            throw new LogFileExtractionException(e.getMessage());
        }
    }
}
