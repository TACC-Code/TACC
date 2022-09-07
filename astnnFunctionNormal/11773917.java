class BackupThread extends Thread {
    public String getTemplateFile(String sFolder) {
        String sName = null;
        FileNameExtensionFilter fltr = new FileNameExtensionFilter("Файлы шаблонов", "svg", "pdf");
        JFileChooser jf = new JFileChooser(sFolder);
        jf.setAcceptAllFileFilterUsed(false);
        jf.setFileFilter(fltr);
        if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            FileInputStream fi = null;
            try {
                if (m_fileData != null) {
                    m_fileData.close();
                    m_fileData = null;
                    m_byteFileData = null;
                }
                sName = jf.getSelectedFile().getPath();
                fi = new FileInputStream(sName);
                m_byteFileData = new byte[(int) fi.getChannel().size()];
                fi.read(m_byteFileData);
                m_fileData = new ByteArrayInputStream(m_byteFileData);
                String sExt = sName.substring(sName.lastIndexOf('.') + 1, sName.length());
                if (sExt.equalsIgnoreCase("SVG")) {
                    m_contentType = ContentType.SVG;
                } else if (sExt.equalsIgnoreCase("PDF")) {
                    m_contentType = ContentType.PDF;
                }
            } catch (Exception ex) {
                Errors.showError(ex);
            } finally {
                try {
                    fi.close();
                } catch (IOException ex) {
                    Errors.showError(ex);
                }
            }
        }
        return sName;
    }
}
