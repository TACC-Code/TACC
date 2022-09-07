class BackupThread extends Thread {
    public synchronized boolean bindSecretToiButton(int page, byte[] bind_data, byte[] bind_code, int secret_number) throws OneWireIOException, OneWireException {
        if (bind_data.length != 32) {
            System.arraycopy(bind_data, 0, bind_data_temp, 0, (bind_data.length > 32 ? 32 : bind_data.length));
            bind_data = bind_data_temp;
        }
        if (bind_code.length != 15) {
            if (bind_code.length == 7) {
                System.arraycopy(bind_code, 0, bind_code_alt_temp, 0, 4);
                bind_code_alt_temp[4] = (byte) page;
                System.arraycopy(address, 0, bind_code_alt_temp, 5, 7);
                System.arraycopy(bind_code, 4, bind_code_alt_temp, 12, 3);
            } else {
                System.arraycopy(bind_code, 0, bind_code_alt_temp, 0, (bind_code.length > 15 ? 15 : bind_code.length));
            }
            bind_code = bind_code_alt_temp;
        }
        System.arraycopy(bind_code, 0, bind_code_temp, 8, 15);
        bind_code = bind_code_temp;
        if (DEBUG) {
            IOHelper.writeLine("------------------------------------");
            IOHelper.writeLine("Binding Secret to iButton");
            IOHelper.writeLine("address");
            IOHelper.writeBytesHex(address);
            IOHelper.writeLine("page: " + page);
            IOHelper.writeLine("secret_number: " + (secret_number & 7));
            IOHelper.writeLine("bind_data");
            IOHelper.writeBytesHex(bind_data);
            IOHelper.writeLine("bind_code");
            IOHelper.writeBytesHex(bind_code);
            IOHelper.writeLine("------------------------------------");
        }
        if (!writeDataPage(page, bind_data)) return false;
        resume = true;
        if (!writeScratchPad(page, 0, bind_code, 0, 32)) {
            resume = false;
            return false;
        }
        if (!SHAFunction(COMPUTE_NEXT_SECRET)) {
            resume = false;
            return false;
        }
        resume = false;
        secret_number = secret_number & 7;
        int secret_page = (secret_number > 3) ? 17 : 16;
        int secret_offset = (secret_number & 3) << 3;
        return (write_read_copy_quick(secret_page, secret_offset));
    }
}
