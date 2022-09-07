class BackupThread extends Thread {
    private void transform(URL urlToSource, String pathToDest) {
        PrintWriter outStream = null;
        try {
            String xslURL = getProperty("xslurl");
            TransformerFactory xformFactory = TransformerFactory.newInstance();
            StreamSource xslSource = new StreamSource(xslURL);
            Transformer transformer = xformFactory.newTransformer(xslSource);
            StreamSource xmlSource = new StreamSource(urlToSource.openStream());
            outStream = new PrintWriter(new FileOutputStream(pathToDest));
            StreamResult fileResult = new StreamResult(outStream);
            transformer.transform(xmlSource, fileResult);
        } catch (TransformerException transEx) {
            System.err.println("\nTransformation error");
            System.err.println(transEx.getMessage());
            Throwable ex = transEx;
            if (transEx.getException() != null) {
                ex = transEx.getException();
                System.err.println(ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            outStream.close();
        }
    }
}
