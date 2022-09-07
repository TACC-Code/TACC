class BackupThread extends Thread {
    public String substituirArquivo() {
        Usuario usuario = getUsuarioLogado();
        try {
            if (btnCancelar == null) {
                String extensao = retornarExtensao(noArquivo);
                Sitio sitio = getSitioAtual();
                String nomeRelativo = "arquivos/" + extensao + "/" + noArquivo;
                String nomeRelativoFisico = sitio.getNoPastaArquivos() + "/arquivos/" + extensao + "/" + noArquivo;
                Arquivo arquivoDB = arquivoDao.select(nomeRelativo, getSitioAtual());
                if (arquivoDB != null && descricao != null && !descricao.equals("") && !descricao.equals("Digite a descri��o")) {
                    arquivoDB.setDeArquivo(descricao);
                    arquivoDao.update(arquivoDB);
                }
                if (getArquivosPath() != null) {
                    File tempFile = new File(getArquivosPath() + "arquivos/temp/" + noArquivo);
                    File destFile = new File(getArquivosPath() + nomeRelativoFisico);
                    FileUtils.copyFile(tempFile, destFile);
                } else {
                    File tempFile = new File(getServletContext().getRealPath("arquivos/temp/" + noArquivo));
                    File destFile = new File(getServletContext().getRealPath(nomeRelativoFisico));
                    FileUtils.copyFile(tempFile, destFile);
                }
                usuario.addActionMessage("Arquivo substituido.");
                logDao.addLog(getUsuarioLogado(), "Substituiu o arquivo '" + nomeRelativo + "'.");
            }
        } catch (Exception e) {
            usuario.addActionError(e.getMessage());
        }
        return SUCCESS;
    }
}
