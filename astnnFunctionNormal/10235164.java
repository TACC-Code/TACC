class BackupThread extends Thread {
    private URL lookForDefaultThemeFile() {
        IEditorInput input;
        input = editor.getEditorInput();
        String filename = Util.getFileLocation((IResource) input.getAdapter(IResource.class));
        try {
            XPathFactory fabrique = XPathFactory.newInstance();
            XPath environnement = fabrique.newXPath();
            URL url = new URL("file:" + filename);
            InputSource source = new InputSource(url.openStream());
            XPathExpression expression;
            expression = environnement.compile("/alloy/instance/@filename");
            String resultat = expression.evaluate(source);
            AlsActivator.getDefault().logInfo("Solution coming from " + resultat);
            IPath path = new Path(resultat);
            IPath themePath = path.removeFileExtension().addFileExtension("thm");
            File themeFile = themePath.toFile();
            if (themeFile.exists()) {
                AlsActivator.getDefault().logInfo("Found default theme " + themeFile);
                return themeFile.toURI().toURL();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
