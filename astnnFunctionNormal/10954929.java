class BackupThread extends Thread {
    public static void onNewFile(String fileName) {
        createStatusFile("NEW_FILE", "Processing new file " + fileName);
        StringBuilder result = new StringBuilder();
        String data = null;
        if (Utils.fileOrFolderExists(writeFolder + fileName)) {
            System.out.println("INFO: file exists - reading file next");
            data = Utils.readFile(writeFolder + fileName);
            System.out.println("INFO: file read " + data + "  now moving file to archive");
            Utils.moveFile(fileName, writeFolder, archiveFolder);
            if (Utils.fileOrFolderExists(writeFolder + fileName)) {
                System.out.println("tried to move file but it is still in the write folder");
            } else {
                System.out.println("INFO: file is gone from writeFolder");
            }
            try {
                if ((data != null) && (data.length() > 0)) {
                    System.out.println("INFO: despatching data");
                    switch(sockMode) {
                        case 1:
                            String params = URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
                            result.append(Utils.httpUrlConnection_post(host, params));
                            break;
                        case 2:
                            result.append(Utils.inputStreamReader_readline(host, port, data));
                            break;
                        case 4:
                            if (!streamEndsWith.isEmpty()) {
                                result.append(Utils.inputStreamReader_readUntilEndsWith(host, port, data, streamEndsWith));
                            } else {
                                System.out.println("FATAL: configured to use inputStreamReader_readUntilEndsWith but streamEndsWith is empty");
                                System.exit(2);
                            }
                            break;
                        case 5:
                            result.append(Utils.inputStreamReader_timeout(host, port, data, sockTimeout));
                    }
                    if (!result.toString().isEmpty()) {
                        System.out.println("INFO: result = " + result.toString() + ".  writing file to read folder");
                        Utils.createFile(readFolder, fileName, result.toString());
                    } else {
                        System.out.println("WARNING: result is empty - nothing to write to read folder");
                    }
                } else {
                    createStatusFile("EMPTY_FILE", fileName + " is an empty file");
                }
            } catch (Exception e) {
                System.out.print(e);
            }
        } else {
            createStatusFile("FILE_READ_ERROR", writeFolder + fileName + " not found");
        }
    }
}
