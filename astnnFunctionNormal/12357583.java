class BackupThread extends Thread {
    public void setHeader(int headerID, Object headerValue) {
        int i;
        for (i = 0; h_id[i] != headerID && i < h_val.size(); i++) ;
        if (h_val.size() == max_id_no) {
            Debug.println(BCC.DEBUG_LEVEL_WARN, "Max number of header sets reached - change " + "the value of OBEXHeaderSet.max_id_no if needed");
        } else {
            if (headerValue == null) {
                for (int j = i; j < (h_val.size() - 1); j++) {
                    h_id[j] = h_id[j + 1];
                }
                if (i < h_val.size()) {
                    h_val.remove(i);
                    Debug.println(BCC.DEBUG_LEVEL_INFO, "Removed header with " + "header ID " + OBEXHeaderSet.hex((byte) headerID));
                } else {
                    Debug.println(BCC.DEBUG_LEVEL_WARN, "Header with header ID " + OBEXHeaderSet.hex((byte) headerID) + " does not exist in this header set - nothing to remove");
                }
            } else {
                if (i == h_val.size()) h_id[i] = headerID;
                if ((headerID & 0xc0) != 0x00 && headerValue instanceof String) {
                    int pos = 0;
                    int hvLen = ((String) headerValue).length();
                    byte[] typeHV = new byte[hvLen];
                    for (pos = 0; pos < hvLen; pos++) {
                        typeHV[pos] = (byte) (((String) headerValue).charAt(pos) & 0xff);
                    }
                    h_val.add(typeHV);
                } else {
                    h_val.add(headerValue);
                }
            }
        }
    }
}
