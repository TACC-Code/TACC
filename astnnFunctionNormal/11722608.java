class BackupThread extends Thread {
    public static void modifyImageIndexs(Context context) {
        try {
            String filePath = Constant.getExtSdPath() + File.separator + Constant.foldName + "_" + Constant.imageIndexFileName;
            RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
            raf.setLength(0);
            raf.close();
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            Iterator it = imageIndexs.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                int[] indexArgs = imageIndexs.get(key);
                key = key + "=" + indexArgs[0] + ":" + indexArgs[1] + "\n";
                fos.write(key.getBytes());
            }
            fos.getChannel().force(true);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
