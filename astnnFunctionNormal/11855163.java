class BackupThread extends Thread {
    private void openFile(URL url) {
        try {
            this.matrix = MatrixFactory.getMatrix(ftype);
            this.matrix.read(url.openStream());
            this.setTitle("Matrix Data - " + url.toString());
            this.rowRangeTxtField.setText("0-" + this.matrix.getrowcount());
            this.colRangeTxtField.setText("0-" + this.matrix.getcolumncount());
            ((MatrixView) this.matrix_display_panel).setMatrix(matrix);
            KeyDisplay kd = new KeyDisplay(matrix);
            kd.setVisible(true);
            kd.setTitle("Color Key - " + url.toString());
        } catch (IOException ex) {
            Logger.getLogger(MatrixViewerDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
