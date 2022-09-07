class BackupThread extends Thread {
    public void init() {
        super.newPlot();
        super.init();
        DataSequenceSet dss = null;
        String dataurlspec = getParameter("dataseturl");
        if (dataurlspec != null) {
            try {
                showStatus("Reading data");
                URL dataurl = new URL(getDocumentBase(), dataurlspec);
                dss = readDataSet(dataurl.openStream());
                showStatus("Ploting data");
                plotOnPanel(plot(), dss);
                showStatus("Done");
            } catch (MalformedURLException e) {
                System.err.println(e.toString());
            } catch (FileNotFoundException e) {
                System.err.println("PlotDSS: file not found: " + e);
            } catch (ClassNotFoundException e) {
                System.err.println("PlotDSS: class not found: " + e);
            } catch (StreamCorruptedException e) {
                System.err.println("PlotDSS: error reading GZIP data: " + e);
            } catch (IOException e) {
                System.err.println("PlotDSS: error reading input file: " + e);
            }
        }
    }
}
