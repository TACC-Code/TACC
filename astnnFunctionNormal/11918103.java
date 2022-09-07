class BackupThread extends Thread {
    private String fixMsplinks(String s) throws MySpaceBlogExporterException {
        String link = null;
        try {
            int i = s.indexOf("a href=\"http://www.msplinks.com");
            while (i > -1) {
                int j = s.indexOf("\"", i + 8);
                link = s.substring(i + 8, j);
                URL url;
                addLogMsg("...resolving encoded link " + link + "...");
                url = new URL(link);
                URLConnection con = url.openConnection();
                con.getContentType();
                link = con.getURL().toString();
                String before = s.substring(0, i + 8);
                String after = s.substring(j);
                String firstPart = before + link;
                s = firstPart + after;
                i = s.indexOf("a href=\"http://www.msplinks.com", firstPart.length());
                if (this.isInterrupted()) {
                    throw new InterruptedException();
                }
            }
        } catch (MalformedURLException e) {
            throw new MySpaceBlogExporterException("Unable to resolve encoded link " + link + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new MySpaceBlogExporterException("Unable to connect to " + link + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new MySpaceBlogExporterException(e.getMessage(), e);
        }
        return s;
    }
}
