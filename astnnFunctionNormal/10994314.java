class BackupThread extends Thread {
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        String arg = args[0].itemAt(0).getStringValue();
        StringWriter sw;
        try {
            URL url = new URL(arg);
            InputStreamReader isr;
            if (args.length > 1) isr = new InputStreamReader(url.openStream(), arg = args[1].itemAt(0).getStringValue()); else isr = new InputStreamReader(url.openStream());
            sw = new StringWriter();
            char[] buf = new char[1024];
            int len;
            while ((len = isr.read(buf)) > 0) {
                sw.write(buf, 0, len);
            }
            isr.close();
            sw.close();
        } catch (MalformedURLException e) {
            throw new XPathException(getASTNode(), e.getMessage());
        } catch (IOException e) {
            throw new XPathException(getASTNode(), e.getMessage());
        }
        return new StringValue(sw.toString());
    }
}
