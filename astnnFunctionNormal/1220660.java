class BackupThread extends Thread {
        public void run() {
            if (data.length() > 0) {
                try {
                    URLConnection connection;
                    JEditorPane c = (JEditorPane) getContainer();
                    HTMLEditorKit kit = (HTMLEditorKit) c.getEditorKit();
                    if (kit.isAutoFormSubmission()) {
                        if ("post".equals(method)) {
                            url = actionURL;
                            connection = url.openConnection();
                            postData(connection, data);
                        } else {
                            url = new URL(actionURL + "?" + data);
                        }
                        Runnable callLoadDocument = new Runnable() {

                            public void run() {
                                JEditorPane c = (JEditorPane) getContainer();
                                if (hdoc.isFrameDocument()) {
                                    c.fireHyperlinkUpdate(createFormSubmitEvent());
                                } else {
                                    try {
                                        c.setPage(url);
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        };
                        SwingUtilities.invokeLater(callLoadDocument);
                    } else {
                        c.fireHyperlinkUpdate(createFormSubmitEvent());
                    }
                } catch (MalformedURLException m) {
                } catch (IOException e) {
                }
            }
        }
}
