class BackupThread extends Thread {
    public void sendFileToStream(OutputStream out, String filePathStr) throws Exception {
        File sendFile = new File(filePathStr);
        if (sendFile.exists() && (sendFile.isFile())) {
            byte dataBuff[] = new byte[bufferSize];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePathStr), bufferSize);
            int readLen;
            while ((readLen = in.read(dataBuff)) > 0) {
                out.write(dataBuff, 0, readLen);
            }
            in.close();
        } else {
            throw new Exception("Not valid parameters, file not exist !!! fileFullPathStr = (" + filePathStr + ")");
        }
    }
}
