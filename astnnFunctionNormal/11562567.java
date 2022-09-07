class BackupThread extends Thread {
    private void processLog(HttpServletResponse response, JSONObject result, File pathProject) {
        General.showDebug("Retrieving cing log tail.");
        File lastLogSendFile = new File(pathProject, Settings.LAST_LOG_SEND_FILE);
        String lastLog = Settings.RESPONSE_LOG_VALUE_NONE;
        File cingRunLogFile = new File(pathProject, Settings.CING_RUN_LOG_FILE);
        if (!cingRunLogFile.exists()) {
            jsonResultPut(result, Settings.RESPONSE_RESULT, lastLog);
            writeJson(response, result);
        }
        long cingrunLogFileSize = cingRunLogFile.length();
        General.showDebug("cingrunLogFileSize: " + cingrunLogFileSize);
        long cingrunLogFileSizeLast = 0;
        if (lastLogSendFile.exists()) {
            General.showDebug("Checking lastLogSendFile: " + lastLogSendFile);
            StringArrayList sal = new StringArrayList();
            boolean statusRead = sal.read(lastLogSendFile.toString());
            lastLogSendFile.delete();
            if (!statusRead) {
                writeJsonError(response, result, "Failed to read the lastLogSendFile: " + lastLogSendFile);
                return;
            }
            if (sal.size() < 1) {
                writeJsonError(response, result, "Failed to read at least one line from the present lastLogSendFile: " + lastLogSendFile);
                return;
            }
            String cingrunLogFileSizeLastStr = sal.getString(0);
            General.showDebug("cingrunLogFileSizeLast (string): " + cingrunLogFileSizeLastStr);
            cingrunLogFileSizeLast = Long.parseLong(cingrunLogFileSizeLastStr);
            General.showDebug("cingrunLogFileSizeLast (long): " + cingrunLogFileSizeLast);
        } else {
            General.showDebug("no LAST_LOG_SEND_FILE: " + Settings.LAST_LOG_SEND_FILE);
        }
        StringArrayList sal = new StringArrayList();
        sal.add(Long.toString(cingrunLogFileSize));
        General.showDebug("writing to LAST_LOG_SEND_FILE: " + Settings.LAST_LOG_SEND_FILE);
        if (!sal.write(lastLogSendFile.toString())) {
            writeJsonError(response, result, "Failed to write to new lastLogSendFile: " + lastLogSendFile);
            return;
        }
        if (cingrunLogFileSize > cingrunLogFileSizeLast) {
            long newLogSize = cingrunLogFileSize - cingrunLogFileSizeLast;
            General.showDebug("New log size: " + newLogSize);
            try {
                RandomAccessFile raf = new RandomAccessFile(cingRunLogFile, "r");
                raf.seek(cingrunLogFileSizeLast);
                byte[] b = new byte[(int) newLogSize];
                raf.readFully(b);
                raf.close();
                lastLog = new String(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                writeJsonError(response, result, "Failed to find cingRunLogFile: " + cingRunLogFile);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                writeJsonError(response, result, "Detected IOException see tomcat log");
                return;
            }
        } else {
            General.showDebug("No new log");
        }
        jsonResultPut(result, Settings.RESPONSE_EXIT_CODE, Settings.RESPONSE_EXIT_CODE_SUCCESS);
        jsonResultPut(result, Settings.RESPONSE_RESULT, lastLog);
        writeJson(response, result);
    }
}
