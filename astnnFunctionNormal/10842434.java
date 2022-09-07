class BackupThread extends Thread {
    public static File s_getVersionDir() throws Exception {
        File fleParent = new File(System.getProperty("user.home"));
        if (fleParent == null) {
            String str = "fleParent == null";
            str += "\n" + "System.getProperty(\"user.home\")=" + System.getProperty("user.home");
            System.err.println(str);
            throw new Exception(str);
        }
        if (!fleParent.isDirectory()) {
            String str = "Not a directory: " + fleParent.getAbsolutePath();
            System.err.println(str);
            throw new Exception(str);
        }
        if (!fleParent.canRead() || !fleParent.canWrite()) {
            String str = "Cannot read or/and write: " + fleParent.getAbsolutePath();
            System.err.println(str);
            throw new Exception(str);
        }
        File fleCur = null;
        for (int i = 0; i < FileHandlerLogger.STRS_LOGS_PARENT_FOLDERS.length - 1; i++) {
            fleCur = new File(fleParent, FileHandlerLogger.STRS_LOGS_PARENT_FOLDERS[i]);
            if (fleCur == null) {
                String str = "Got nil file: " + "PropNamingEtk.STRS_LOGS_PARENT_FOLDERS[" + i + "]";
                System.err.println(str);
                throw new Exception(str);
            }
            if (!fleCur.exists()) {
                String str = "file does not exist: " + fleCur.getAbsolutePath();
                System.err.println(str);
                throw new Exception(str);
            }
            if (!fleCur.isDirectory()) {
                String str = "file is not a directory: " + fleCur.getAbsolutePath();
                System.err.println(str);
                throw new Exception(str);
            }
            fleParent = fleCur;
        }
        fleCur = new File(fleParent, FileHandlerLogger.STR_APPLI_VERSION_TRANSFORMED);
        if (fleCur == null) {
            String str = "Got nil file: " + "PropNamingEtk.STR_APPLI_VERSION_TRANSFORMED";
            System.err.println(str);
            throw new Exception(str);
        }
        if (!fleCur.exists()) {
            String str = "file does not exist: " + fleCur.getAbsolutePath();
            System.err.println(str);
            throw new Exception(str);
        }
        if (!fleCur.isDirectory()) {
            String str = "file dis not a directory: " + fleCur.getAbsolutePath();
            System.err.println(str);
            throw new Exception(str);
        }
        return fleCur;
    }
}
