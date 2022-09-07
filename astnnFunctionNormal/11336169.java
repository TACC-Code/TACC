class BackupThread extends Thread {
    private void load() {
        try {
            URLConnection urlConnection = url.openConnection();
            InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(isr);
            ArrayList results = new ArrayList();
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                results.add(currentLine.trim());
                currentLine = bufferedReader.readLine();
            }
            this.vocabulary = (String[]) results.toArray(new String[0]);
            Arrays.sort(vocabulary);
            String statusOKMessage = PedroResources.getMessage("ontology.statusOK", url.getFile());
            status = new StringBuffer();
            status.append(statusOKMessage);
            isSourceWorking = true;
        } catch (Exception err) {
            isSourceWorking = false;
            status = new StringBuffer();
            String statusErrorMessage = PedroResources.getMessage("ontology.statusError", err.toString());
            status.append(statusErrorMessage);
        }
    }
}
