class BackupThread extends Thread {
    static List<Attachment> parseParametersAndAttachments(HttpServletRequest request, Map parameters) throws IOException, MessagingException {
        DataSource ds = new HttpRequestDataSource(request);
        MimeMultipart mmp = new MimeMultipart(ds);
        int handledBodyCount = mmp.getCount();
        List<Attachment> attachments = new ArrayList<Attachment>();
        for (int i = 0; i < handledBodyCount; i++) {
            BodyPart bodyPart = mmp.getBodyPart(i);
            String filename = bodyPart.getFileName();
            if ((filename != null) && (filename.length() > 0)) {
                File temporaryFile = File.createTempFile("svupload", null);
                String[] disposition = bodyPart.getHeader(HEADER_CONTENT_DISPOSITION);
                String name = null;
                if (disposition.length >= 1) {
                    name = getParameterValue(disposition[0], "name");
                    String temp = getParameterValue(disposition[0], "filename");
                    if (temp != null) {
                        filename = temp;
                    }
                }
                filename = new String(filename.getBytes("ISO-8859-1"), "UTF8");
                Attachment attachment = new Attachment(name, filename, temporaryFile);
                for (Enumeration e = bodyPart.getAllHeaders(); e.hasMoreElements(); ) {
                    Header header = (Header) e.nextElement();
                    String headerName = header.getName();
                    String headerValue = header.getValue();
                    attachment.addHeader(headerName, headerValue);
                }
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    int readBytes;
                    byte[] buffer;
                    is = bodyPart.getInputStream();
                    fos = new FileOutputStream(temporaryFile);
                    buffer = new byte[1024];
                    while ((readBytes = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, readBytes);
                    }
                    attachments.add(attachment);
                } catch (IOException ioe) {
                    if (fos != null) {
                        fos.close();
                        temporaryFile.delete();
                        fos = null;
                    }
                    throw ioe;
                } finally {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                }
            } else if (filename == null) {
                final String NAME_STRING = " name=";
                for (Enumeration e = bodyPart.getAllHeaders(); e.hasMoreElements(); ) {
                    Header header = (Header) e.nextElement();
                    String headerValue = header.getValue();
                    int nameIndex = headerValue.toLowerCase().indexOf(NAME_STRING);
                    if (nameIndex != -1) {
                        int quoteIndex = headerValue.lastIndexOf('\"');
                        if (quoteIndex > nameIndex) {
                            String name = headerValue.substring(nameIndex + NAME_STRING.length() + 1, quoteIndex);
                            ByteArrayOutputStream value = new ByteArrayOutputStream();
                            InputStream inputStream = bodyPart.getInputStream();
                            try {
                                int b;
                                while ((b = inputStream.read()) != -1) {
                                    value.write(b);
                                }
                            } finally {
                                try {
                                    inputStream.close();
                                } catch (IOException ee) {
                                }
                            }
                            parameters.put(name, value.toString("UTF-8").trim());
                        } else {
                            throw new MessagingException("MIME multi-part message format error");
                        }
                    }
                }
            }
        }
        return attachments;
    }
}
