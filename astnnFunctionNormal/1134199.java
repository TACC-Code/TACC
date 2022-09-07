class BackupThread extends Thread {
    private final int pushBytes(final Operation op, final String vcardString) {
        if (vcardString == null) {
            Log.w(TAG, "vcardString is null!");
            return ResponseCodes.OBEX_HTTP_OK;
        }
        int vcardStringLen = vcardString.length();
        if (D) Log.d(TAG, "Send Data: len=" + vcardStringLen);
        OutputStream outputStream = null;
        int pushResult = ResponseCodes.OBEX_HTTP_OK;
        try {
            outputStream = op.openOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "open outputstrem failed" + e.toString());
            return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        int position = 0;
        long timestamp = 0;
        int outputBufferSize = op.getMaxPacketSize();
        if (V) Log.v(TAG, "outputBufferSize = " + outputBufferSize);
        while (position != vcardStringLen) {
            if (V) timestamp = System.currentTimeMillis();
            int readLength = outputBufferSize;
            if (vcardStringLen - position < outputBufferSize) {
                readLength = vcardStringLen - position;
            }
            String subStr = vcardString.substring(position, position + readLength);
            try {
                outputStream.write(subStr.getBytes(), 0, readLength);
            } catch (IOException e) {
                Log.e(TAG, "write outputstrem failed" + e.toString());
                pushResult = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
                break;
            }
            if (V) {
                Log.v(TAG, "Sending vcard String position = " + position + " readLength " + readLength + " bytes took " + (System.currentTimeMillis() - timestamp) + " ms");
            }
            position += readLength;
        }
        if (V) Log.v(TAG, "Send Data complete!");
        if (!closeStream(outputStream, op)) {
            pushResult = ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
        }
        return pushResult;
    }
}
