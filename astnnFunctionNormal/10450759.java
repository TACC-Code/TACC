class BackupThread extends Thread {
    public synchronized boolean installMasterSecret(int page, byte[] secret, int secret_number) throws OneWireIOException, OneWireException {
        if (secret.length == 0) return false;
        byte[] input_secret = null;
        byte[] buffer = byte_buffer;
        int secret_mod_length = secret.length % 47;
        if (secret_mod_length == 0) input_secret = secret; else {
            input_secret = new byte[secret.length + (47 - secret_mod_length)];
            System.arraycopy(secret, 0, input_secret, 0, secret.length);
        }
        if (DEBUG) {
            IOHelper.writeLine("------------------------------------");
            IOHelper.writeLine("Installing Secret on iButton");
            IOHelper.writeLine("address");
            IOHelper.writeBytesHex(address);
            IOHelper.writeLine("page: " + page);
            IOHelper.writeLine("secret");
            IOHelper.writeBytesHex(secret);
            IOHelper.writeLine("input_secret");
            IOHelper.writeBytesHex(input_secret);
            IOHelper.writeLine("secret_number: " + (secret_number & 7));
            IOHelper.writeLine("------------------------------------");
        }
        secret_number = secret_number & 7;
        int secret_page = (secret_number > 3) ? 17 : 16;
        int secret_offset = (secret_number & 3) << 3;
        int offset = 0;
        byte[] sp_buffer = new byte[32];
        while (offset < input_secret.length) {
            if (!eraseScratchPad(page)) return false;
            if (!writeScratchPad(page, 0, input_secret, offset, 32)) return false;
            if (readScratchPad(buffer, 0) < 0) return false;
            if (!copyScratchPad()) return false;
            System.arraycopy(input_secret, offset + 32, sp_buffer, 8, 15);
            if (!writeScratchPad(page, 0, sp_buffer, 0, 32)) return false;
            if (!SHAFunction((offset == 0) ? COMPUTE_FIRST_SECRET : COMPUTE_NEXT_SECRET)) return false;
            if (!write_read_copy_quick(secret_page, secret_offset)) return false;
            offset += 47;
        }
        writeDataPage(page, FF);
        return true;
    }
}
