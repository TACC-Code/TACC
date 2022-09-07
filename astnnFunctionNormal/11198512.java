class BackupThread extends Thread {
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getActionCommand().equals("new")) {
            try {
                FileReader reader = new FileReader("resources/defaultMessage.xml");
                StringWriter writer = new StringWriter();
                while (reader.ready()) {
                    writer.write(reader.read());
                }
                StringBuffer strBuffer = writer.getBuffer();
                inPane.setText(strBuffer.toString());
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        } else if (e.getActionCommand().equals("sign")) {
            StringReader strReader = new StringReader(inPane.getText());
            StringWriter strWriter = new StringWriter();
            DOMParser domp = new DOMParser();
            Document doc = null;
            Document signedDocument = null;
            try {
                domp.parse(new InputSource(strReader));
                doc = domp.getDocument();
                signedDocument = qore.sign(doc);
                OutputFormat format = new OutputFormat(signedDocument, "UTF-8", false);
                format.setPreserveSpace(true);
                Serializer ser = new XMLSerializer(format);
                ser.setOutputCharStream(strWriter);
                DOMSerializer domser = ser.asDOMSerializer();
                domser.serialize(signedDocument);
                inPane.setText(strWriter.toString());
                strWriter.close();
            } catch (IOException ex) {
                System.err.println("Exception: Browser::actionPerformed() : " + ex);
            } catch (SAXException ex) {
                System.err.println("Exception: Browser::actionPerformed() : " + ex);
            }
        } else if (e.getActionCommand().equals("enqueue")) {
        }
    }
}
