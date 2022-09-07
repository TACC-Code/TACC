class BackupThread extends Thread {
        @Override
        public void run() {
            try {
                if (this.term == null || beanInfoThread != this) return;
                StringBuilder builder = new StringBuilder(NCBO.BIOPORTAL_URL);
                builder.append("/concepts/");
                builder.append(term.getOntologyVersionId());
                builder.append("/");
                builder.append(URLEncoder.encode(term.getConceptIdShort(), "UTF-8"));
                URL url = new URL(builder.toString());
                InputStream in = url.openStream();
                TransformerFactory f = TransformerFactory.newInstance();
                StreamSource stylesheet = new StreamSource(ResourceUtils.getResourceAsStream(NCBOSearchPane.class, "bioportal2html.xsl"));
                Transformer transformer = f.newTransformer(stylesheet);
                StringWriter strw = new StringWriter();
                StreamResult result = new StreamResult(strw);
                transformer.transform(new StreamSource(in), result);
                in.close();
                if (beanInfoThread != this) return;
                SwingUtilities.invokeLater(new RunnableObject<String>(strw.toString()) {

                    @Override
                    public void run() {
                        beanInfo.setText(getObject().toString().trim());
                        beanInfo.setCaretPosition(0);
                    }
                });
            } catch (Throwable err) {
                beanInfo.setText("" + err.getMessage());
                java.awt.Toolkit.getDefaultToolkit().beep();
                err.printStackTrace();
            } finally {
                beanInfoThread = null;
                beanInfo.setCaretPosition(0);
            }
        }
}
