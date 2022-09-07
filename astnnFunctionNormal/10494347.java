class BackupThread extends Thread {
    public static int write_command_to_file(String cmd) {
        try {
            FileChannel wChannel = new FileOutputStream(new File(cgiutils.command_file), true).getChannel();
            FileLock lock = wChannel.tryLock();
            while (lock == null) lock = wChannel.tryLock();
            wChannel.write(ByteBuffer.wrap(cmd.getBytes()));
            lock.release();
            wChannel.close();
        } catch (IOException ioE) {
            ioE.printStackTrace();
            if (content_type == WML_CONTENT) System.out.printf("<p>Error: Could not open command file for update!</p>\n"); else {
                System.out.printf("<P><DIV CLASS='errorMessage'>Error: Could not open command file '%s' for update!</DIV></P>\n", cgiutils.command_file);
                System.out.printf("<P><DIV CLASS='errorDescription'>");
                System.out.printf("The permissions on the external command file and/or directory may be incorrect.  Read the FAQs on how to setup proper permissions.\n");
                System.out.printf("</DIV></P>\n");
            }
            return common_h.ERROR;
        }
        return common_h.OK;
    }
}
