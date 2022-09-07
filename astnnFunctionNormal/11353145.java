class BackupThread extends Thread {
    public void save(String strFileName) {
        int nExcelModelID = -1;
        try {
            nExcelModelID = dis.readInt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (getSaveMethod() == SAVE_METHOD_DATABASE) {
            saveToDatabase(strFileName, nExcelModelID);
            return;
        }
        FileOutputStream fosCurrent = null;
        DataOutputStream dosCurrent = null;
        try {
            upOneLevel(strFileName);
            fosCurrent = new FileOutputStream(prependPathAndReplaceSlashes(strFileName));
            dosCurrent = new DataOutputStream(fosCurrent);
            if (isSpecial) {
                int nByteCount = 4 + 4 + 4 + strFileName.length();
                while (nByteCount++ < contentLength) dosCurrent.writeByte(dis.readByte());
                if (dosCurrent != null) dosCurrent.close();
                if (fosCurrent != null) fosCurrent.close();
            } else while (true) dosCurrent.writeByte(dis.readByte());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dosCurrent != null) dosCurrent.close();
                if (fosCurrent != null) fosCurrent.close();
                dis.close();
                dos.close();
            } catch (IOException ioe) {
            }
        }
    }
}
