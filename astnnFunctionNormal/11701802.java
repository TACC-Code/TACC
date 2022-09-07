class BackupThread extends Thread {
    public static boolean writeLibrary(Object obj, File output) {
        XMLObjectWriter writer = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            writer = XMLObjectWriter.newInstance(new BufferedOutputStream(new DigestOutputStream(new FileOutputStream(output), md)), ENCODING);
            writer.setIndentation("\t");
            XMLBinding binding = new XMLBinding();
            binding.setAlias(Student.class, "Student");
            binding.setAlias(Event.class, "Event");
            writer.setBinding(binding);
            writer.write(obj);
        } catch (Exception e) {
            try {
                System.out.println("Write operation for " + output.getCanonicalPath() + " failed");
            } catch (IOException ioe) {
            }
            e.printStackTrace();
            return false;
        } finally {
            close(writer);
        }
        File checksumFile = makeChecksumFile(output);
        FileOutputStream mdStream = null;
        try {
            mdStream = new FileOutputStream(checksumFile);
            mdStream.write(md.digest());
        } catch (Exception e) {
            System.out.println("Write operation for associated checksum file failed");
            e.printStackTrace();
            return false;
        } finally {
            close(mdStream);
        }
        File backupLibrary = makeLibraryBackupFile(output);
        try {
            File backupDirectory = backupLibrary.getParentFile();
            if (!backupDirectory.exists()) backupDirectory.mkdir();
            copyFile(output, backupLibrary);
            copyFile(checksumFile, makeChecksumFile(backupLibrary));
        } catch (Exception e) {
            System.out.println("Write operation for backup files failed");
            e.printStackTrace();
            return false;
        }
        try {
            System.out.println("Write operation for " + output.getCanonicalPath() + " succeeded");
        } catch (IOException ioe) {
        }
        return true;
    }
}
