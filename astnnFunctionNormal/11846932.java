class BackupThread extends Thread {
    public Collection parse(InputSource inputSource, boolean readOnly) throws UmlException {
        if (inputSource == null) {
            throw new NullPointerException("The input source must be non-null.");
        }
        InputStream is = null;
        boolean needsClosing = false;
        if (inputSource.getByteStream() != null) {
            is = inputSource.getByteStream();
        } else if (inputSource.getSystemId() != null) {
            try {
                URL url = new URL(inputSource.getSystemId());
                if (url != null) {
                    is = url.openStream();
                    if (is != null) {
                        is = new BufferedInputStream(is);
                        needsClosing = true;
                    }
                }
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
        }
        if (is == null) {
            throw new UnsupportedOperationException();
        }
        modelImpl.clearEditingDomain();
        Resource r = UMLUtil.getResource(modelImpl, UMLUtil.DEFAULT_URI, readOnly);
        try {
            modelImpl.getModelEventPump().stopPumpingEvents();
            r.load(is, null);
        } catch (IOException e) {
            throw new UmlException(e);
        } finally {
            modelImpl.getModelEventPump().startPumpingEvents();
            if (needsClosing) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        resource = r;
        return r.getContents();
    }
}
