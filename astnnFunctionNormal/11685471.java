class BackupThread extends Thread {
    protected boolean provisionFile(String input, String output) {
        BufferedInputStream reader = null;
        BufferedOutputStream writer = null;
        long size = 0;
        int bufLen = 5000 * 1024;
        String fileName = null;
        ArrayList<S3Object> inputs = new ArrayList<S3Object>();
        ArrayList<BufferedInputStream> readerList = new ArrayList<BufferedInputStream>();
        HashMap<String, String> fileNameMap = new HashMap<String, String>();
        HashMap<String, String> filePathMap = new HashMap<String, String>();
        if (input.startsWith("s3://")) {
            Pattern p = Pattern.compile("s3://(\\S+):(\\S+)@(\\S+)");
            Matcher m = p.matcher(input);
            boolean result = m.find();
            String accessKey = null;
            String secretKey = null;
            String URL = input;
            if (result) {
                accessKey = m.group(1);
                secretKey = m.group(2);
                URL = "s3://" + m.group(3);
            } else {
                try {
                    HashMap<String, String> settings = (HashMap<String, String>) ConfigTools.getSettings();
                    accessKey = settings.get("AWS_ACCESS_KEY");
                    secretKey = settings.get("AWS_SECRET_KEY");
                } catch (Exception e) {
                    e.printStackTrace();
                    return (false);
                }
            }
            if (accessKey == null || secretKey == null) {
                return (false);
            }
            AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
            p = Pattern.compile("s3://([^/]+)/(\\S+)");
            m = p.matcher(URL);
            result = m.find();
            if (result) {
                String bucket = m.group(1);
                String key = m.group(2);
                ObjectListing objListing = s3.listObjects(bucket, key);
                List<S3ObjectSummary> s3ObjSum = objListing.getObjectSummaries();
                if (s3ObjSum.size() > 1) {
                    long downloadSize = 0L;
                    for (S3ObjectSummary s3obj : s3ObjSum) {
                        size += s3obj.getSize();
                        if (s3obj.getSize() > 0) {
                            inputs.add(s3.getObject(new GetObjectRequest(s3obj.getBucketName(), s3obj.getKey())));
                        }
                    }
                    while (objListing.isTruncated()) {
                        objListing = s3.listNextBatchOfObjects(objListing);
                        s3ObjSum = objListing.getObjectSummaries();
                        for (S3ObjectSummary s3obj : s3ObjSum) {
                            size += s3obj.getSize();
                            if (s3obj.getSize() > 0) {
                                inputs.add(s3.getObject(new GetObjectRequest(s3obj.getBucketName(), s3obj.getKey())));
                            }
                        }
                    }
                    if (!output.startsWith("s3://") && !output.startsWith("http://") && !output.startsWith("https://")) {
                        File outputDir = new File(output);
                        long freeSpace = outputDir.getFreeSpace();
                        if (size > freeSpace) {
                            System.err.println("Total download size is " + size + " but space available is " + freeSpace);
                            return (false);
                        } else {
                            System.err.println("Total download size is " + size + " and space available is " + freeSpace);
                        }
                    }
                } else if (s3ObjSum.size() == 1) {
                    S3Object object = s3.getObject(new GetObjectRequest(bucket, s3ObjSum.get(0).getKey()));
                    size = object.getObjectMetadata().getContentLength();
                    reader = new BufferedInputStream(object.getObjectContent(), bufLen);
                } else {
                    return (false);
                }
            } else {
                return (false);
            }
        } else if (input.startsWith("http://") || input.startsWith("https://")) {
            Pattern p = Pattern.compile("(https*)://(\\S+):(\\S+)@(\\S+)");
            Matcher m = p.matcher(input);
            boolean result = m.find();
            String protocol = null;
            String user = null;
            String pass = null;
            String URL = input;
            if (result) {
                protocol = m.group(1);
                user = m.group(2);
                pass = m.group(3);
                URL = protocol + "://" + m.group(4);
            }
            URL urlObj = null;
            try {
                urlObj = new URL(URL);
                if (urlObj != null) {
                    URLConnection urlConn = urlObj.openConnection();
                    if (user != null && pass != null) {
                        String userPassword = user + ":" + pass;
                        String encoding = new Base64().encodeBase64String(userPassword.getBytes());
                        urlConn.setRequestProperty("Authorization", "Basic " + encoding);
                    }
                    p = Pattern.compile("://([^/]+)/(\\S+)");
                    m = p.matcher(URL);
                    result = m.find();
                    if (result) {
                        String host = m.group(1);
                        String path = m.group(2);
                        String[] paths = path.split("/");
                        fileName = paths[paths.length - 1];
                        size = urlConn.getContentLength();
                        reader = new BufferedInputStream(urlConn.getInputStream(), bufLen);
                    }
                }
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                File inputFile = new File(input);
                String[] paths = inputFile.getAbsolutePath().split("/");
                fileName = paths[paths.length - 1];
                size = inputFile.length();
                reader = new BufferedInputStream(new FileInputStream(new File(input)));
                fileNameMap.put(reader.toString(), fileName);
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return (false);
            }
        }
        if (reader != null) {
            readerList.add(reader);
        } else if (inputs.size() > 0) {
            for (S3Object obj : inputs) {
                reader = new BufferedInputStream(obj.getObjectContent(), bufLen);
                readerList.add(reader);
                String[] paths = obj.getKey().split("/");
                fileName = paths[paths.length - 1];
                fileNameMap.put(reader.toString(), fileName);
                String relPath = obj.getKey();
                relPath = relPath.replace(fileName, "");
                filePathMap.put(reader.toString(), relPath);
            }
        }
        for (BufferedInputStream currReader : readerList) {
            fileName = fileNameMap.get(currReader.toString());
            if (output.startsWith("s3://")) {
                Pattern p = Pattern.compile("s3://(\\S+):(\\S+)@(\\S+)");
                Matcher m = p.matcher(output);
                boolean result = m.find();
                String accessKey = null;
                String secretKey = null;
                String URL = output;
                if (result) {
                    accessKey = m.group(1);
                    secretKey = m.group(2);
                    URL = "s3://" + m.group(3);
                } else {
                    try {
                        HashMap<String, String> settings = (HashMap<String, String>) ConfigTools.getSettings();
                        accessKey = settings.get("AWS_ACCESS_KEY");
                        secretKey = settings.get("AWS_SECRET_KEY");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return (false);
                    }
                }
                if (accessKey == null || secretKey == null) {
                    return (false);
                }
                p = Pattern.compile("s3://([^/]+)/(\\S+)");
                m = p.matcher(URL);
                result = m.find();
                if (result) {
                    String bucket = m.group(1);
                    String key = m.group(2) + "/" + fileName;
                    System.out.println("keys: " + accessKey);
                    TransferManager tm = new TransferManager(new BasicAWSCredentials(accessKey, secretKey));
                    ObjectMetadata omd = new ObjectMetadata();
                    omd.setContentLength(size);
                    System.out.println("VERBOSE: bucket: " + bucket + " key: " + key + " currReader: " + currReader + " omd: " + omd + " size: " + size);
                    try {
                        currReader = new BufferedInputStream(new FileInputStream(new File("/datastore/bfast_temp-20110622.tar.gz")));
                        System.out.println("Curr reader: " + currReader.available());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Transfer myUpload = tm.upload(bucket, key, currReader, omd);
                    try {
                        System.out.println("State: " + myUpload.getState().toString());
                        while (myUpload.isDone() == false) {
                            if (options.has("verbose") || options.has("v")) {
                                System.out.println("Transfer: " + myUpload.getDescription());
                                System.out.println("  - State:    " + myUpload.getState());
                                System.out.println("  - Progress: " + myUpload.getProgress().getBytesTransfered() + " of " + size);
                            }
                            if (myUpload.getState() == TransferState.Failed) {
                                tm.shutdownNow();
                                return (false);
                            }
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                        return (false);
                    }
                } else {
                    return (false);
                }
            } else if (output.startsWith("http://") || output.startsWith("https://")) {
                return (false);
            } else {
                File outputObj = new File(output + File.separator + fileName);
                if (filePathMap.get(currReader.toString()) != null) {
                    outputObj = new File(output + File.separator + filePathMap.get(currReader.toString()) + File.separator + fileName);
                }
                outputObj.getParentFile().mkdirs();
                try {
                    writer = new BufferedOutputStream(new FileOutputStream(outputObj), bufLen);
                    while (true) {
                        int data = currReader.read();
                        if (data == -1) {
                            break;
                        }
                        writer.write(data);
                    }
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            if (currReader != null) {
                try {
                    currReader.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return (true);
    }
}
