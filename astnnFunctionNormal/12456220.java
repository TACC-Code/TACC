class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public MultipartRequestWrapper(ServletRequest request) throws IOException {
        super((HttpServletRequest) request);
        String contentType = request.getContentType();
        if (!contentType.toLowerCase().startsWith("multipart/form-data")) {
            throw new IOException("The MIME Content-Type of the Request must be " + '"' + "multipart/form-data" + '"' + ", not " + '"' + contentType + '"' + ".");
        } else if (request.getAttribute(MPH_ATTRIBUTE) != null) {
            MultipartRequestWrapper oldMPH = (MultipartRequestWrapper) request.getAttribute(MPH_ATTRIBUTE);
            this.stringParameters = oldMPH.stringParameters;
            this.mimeTypes = oldMPH.mimeTypes;
            this.tempFileNames = oldMPH.tempFileNames;
            this.uploadFileNames = oldMPH.uploadFileNames;
            return;
        }
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            InputStream inputServlet = request.getInputStream();
            byte buffer[] = new byte[2048];
            int readBytes = inputServlet.read(buffer);
            while (readBytes != -1) {
                byteArray.write(buffer, 0, readBytes);
                readBytes = inputServlet.read(buffer);
            }
            inputServlet.close();
            MimeMultipart parts = new MimeMultipart(new MultipartRequestWrapperDataSource(contentType, byteArray.toByteArray()));
            byteArray.close();
            Map stringParameters = new HashMap();
            Map mimeTypes = new HashMap();
            Map tempFileNames = new HashMap();
            Map uploadFileNames = new HashMap();
            String encoding = request.getCharacterEncoding();
            if (encoding == null) {
                encoding = "8859_1";
            }
            for (int loopCount = 0; loopCount < parts.getCount(); loopCount++) {
                MimeBodyPart current = (MimeBodyPart) parts.getBodyPart(loopCount);
                String headers = current.getHeader("Content-Disposition", "; ");
                if (headers.indexOf(" name=" + '"') == -1) {
                    throw new MessagingException("No name header found in " + "Content-Disposition field.");
                } else {
                    String namePart = headers.substring(headers.indexOf(" name=\"") + 7);
                    namePart = namePart.substring(0, namePart.indexOf('"'));
                    String nameField = javax.mail.internet.MimeUtility.decodeText(namePart);
                    InputStream inRaw = current.getInputStream();
                    if (headers.indexOf(" filename=" + '"') != -1) {
                        String fileName = headers.substring(headers.indexOf(" filename=" + '"') + 11);
                        fileName = fileName.substring(0, fileName.indexOf('"'));
                        if (fileName.lastIndexOf('/') != -1) {
                            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                        }
                        if (fileName.lastIndexOf('\\') != -1) {
                            fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                        }
                        uploadFileNames.put(nameField, fileName);
                        if (tempFileNames.containsKey(nameField)) {
                            throw new IOException("Multiple parameters named " + nameField);
                        }
                        if (current.getContentType() == null) {
                            mimeTypes.put(nameField, "text/plain");
                        } else {
                            mimeTypes.put(nameField, current.getContentType());
                        }
                        File tempFile = File.createTempFile("mph", ".tmp");
                        OutputStream outStream = new FileOutputStream(tempFile, true);
                        while ((readBytes = inRaw.read(buffer)) != -1) {
                            outStream.write(buffer, 0, readBytes);
                        }
                        inRaw.close();
                        outStream.close();
                        tempFileNames.put(nameField, tempFile.getAbsoluteFile());
                    } else {
                        byte[] stash = new byte[inRaw.available()];
                        inRaw.read(stash);
                        inRaw.close();
                        Object oldParam = stringParameters.get(nameField);
                        if (oldParam == null) {
                            stringParameters.put(nameField, new String[] { new String(stash, encoding) });
                        } else {
                            String oldParams[] = (String[]) oldParam;
                            String newParams[] = new String[oldParams.length + 1];
                            System.arraycopy(oldParams, 0, newParams, 0, oldParams.length);
                            newParams[oldParams.length] = new String(stash, encoding);
                            stringParameters.put(nameField, newParams);
                        }
                    }
                }
            }
            parts = null;
            this.stringParameters = Collections.unmodifiableMap(stringParameters);
            this.mimeTypes = Collections.unmodifiableMap(mimeTypes);
            this.tempFileNames = Collections.unmodifiableMap(tempFileNames);
            this.uploadFileNames = Collections.unmodifiableMap(uploadFileNames);
            request.setAttribute(MPH_ATTRIBUTE, this);
        } catch (MessagingException errMime) {
            throw new IOException(errMime);
        }
    }
}
