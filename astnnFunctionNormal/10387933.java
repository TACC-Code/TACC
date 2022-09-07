class BackupThread extends Thread {
    private void getChannel(String ip) throws IOException {
        if (!hmFileChannel.containsKey(ip)) {
            String ipAddr = ip.substring(0, ip.indexOf(":"));
            if (handler.sSYSSavePath.indexOf("\\") != -1) {
                handler.sSYSSavePath = handler.sSYSSavePath.replace('\\', '/');
            }
            if (handler.sSYSSavePath.endsWith("/")) sPath = handler.sSYSSavePath + handler.sName + ipAddr + "/" + sf.format(new Date()) + "_" + handler.sName + ".log"; else sPath = handler.sSYSSavePath + "/" + handler.sName + ipAddr + "/" + sf.format(new Date()) + "_" + handler.sName + ".log";
            File file = new File(sPath);
            if (!new File(file.getParent()).exists()) new File(file.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(file, true);
            FileChannel fc = fos.getChannel();
            hmFileChannel.put(ip, fc);
        }
    }
}
