class BackupThread extends Thread {
    public String incluirCss() {
        Usuario usuario = getUsuarioLogado();
        if (fileCss != null && !fileCssFileName.equals("")) {
            try {
                int i = fileCssFileName.lastIndexOf(".");
                String extensao = fileCssFileName.substring(i).toLowerCase();
                if (!extensao.equals(".css")) {
                    throw new Exception("Somente arquivos do tipo css.");
                }
                File theFile = ArquivoAction.getFile(getSitioAtual(), "css" + File.separatorChar + fileCssFileName);
                ControleCss controleCss = new ControleCss(this);
                String codCss = new G_File(fileCss.getAbsolutePath()).read();
                controleCss.doAval(codCss);
                if (avaliacaoCSS.getErros().getErrorCount() > 0) {
                    throw new Exception("Existem erros no css e ele n&atilde;o pode ser cadastrado.");
                } else {
                    FileUtils.copyFile(fileCss, theFile);
                    usuario.addActionMessage("Arquivo de CSS cadastrado corretamente.");
                }
                if (avaliacaoCSS.getErros().getErrorCount() > 0 || avaliacaoCSS.getAvisos().getWarningCount() > 0) {
                    getRequest().setAttribute("avaliacaoCSS", avaliacaoCSS);
                } else {
                    getRequest().setAttribute("avaliacaoCSS", null);
                }
            } catch (Exception e) {
                usuario.addActionError(e.getMessage());
                return INPUT;
            }
        }
        return SUCCESS;
    }
}
