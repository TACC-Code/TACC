class BackupThread extends Thread {
    protected Part readTillFound(final String[] id) throws java.io.IOException {
        if (dimeDelimitedStream == null) {
            return null;
        }
        Part ret = null;
        try {
            if (soapStream != null) {
                if (!eos) {
                    java.io.ByteArrayOutputStream soapdata = new java.io.ByteArrayOutputStream(1024 * 8);
                    byte[] buf = new byte[1024 * 16];
                    int byteread = 0;
                    do {
                        byteread = soapStream.read(buf);
                        if (byteread > 0) {
                            soapdata.write(buf, 0, byteread);
                        }
                    } while (byteread > -1);
                    soapdata.close();
                    soapStream.close();
                    soapStream = new java.io.ByteArrayInputStream(soapdata.toByteArray());
                }
                dimeDelimitedStream = dimeDelimitedStream.getNextStream();
            }
            if (null != dimeDelimitedStream) {
                do {
                    String contentId = dimeDelimitedStream.getContentId();
                    String type = dimeDelimitedStream.getType();
                    if (type != null && !dimeDelimitedStream.getDimeTypeNameFormat().equals(DimeTypeNameFormat.MIME)) {
                        type = "application/uri; uri=\"" + type + "\"";
                    }
                    ManagedMemoryDataSource source = new ManagedMemoryDataSource(dimeDelimitedStream, ManagedMemoryDataSource.MAX_MEMORY_DISK_CACHED, type, true);
                    DataHandler dh = new DataHandler(source);
                    AttachmentPart ap = new AttachmentPart(dh);
                    if (contentId != null) {
                        ap.setMimeHeader(HTTPConstants.HEADER_CONTENT_ID, contentId);
                    }
                    addPart(contentId, "", ap);
                    for (int i = id.length - 1; ret == null && i > -1; --i) {
                        if (contentId != null && id[i].equals(contentId)) {
                            ret = ap;
                        }
                    }
                    dimeDelimitedStream = dimeDelimitedStream.getNextStream();
                } while (null == ret && null != dimeDelimitedStream);
            }
        } catch (Exception e) {
            throw org.apache.axis.AxisFault.makeFault(e);
        }
        return ret;
    }
}
