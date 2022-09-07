class BackupThread extends Thread {
    public String adicionarArquivo() {
        Usuario usuarioLogado = getUsuarioLogado();
        if (descricao != null && (fileUpload == null || fileUploadFileName == null || fileUploadFileName.equals(""))) {
            usuarioLogado.addActionError("Por favor, preencha o campo arquivo.");
        }
        if (fileUpload != null && !fileUploadFileName.equals("")) {
            try {
                fileUploadFileName = StringUtils.removeAcentos(fileUploadFileName).replace(" ", "-");
                String extensao = retornarExtensao(fileUploadFileName);
                if (!extensaoPermitida(extensao)) {
                    throw new Exception("Tipo de arquivo n&atilde;o permitido.");
                }
                if (descricao == null || descricao.equals("") || descricao.equals(getText("predefinido.descricao"))) {
                    throw new Exception("Por favor, preencha o campo descri&ccedil;&atilde;o.");
                }
                if (longdesc == null || longdesc.equals("") || longdesc.equals(getText("predefinido.descricao")) || longdesc.equals("<p>" + getText("predefinido.descricao") + "</p>")) {
                    longdesc = null;
                }
                Sitio sitio = getSitioAtual();
                String nomeRelativo = "arquivos/" + extensao + "/" + fileUploadFileName;
                String nomeRelativoFisico = sitio.getNoPastaArquivos() + "/arquivos/" + extensao + "/" + fileUploadFileName;
                Arquivo arquivoDB = arquivoDao.select(nomeRelativo, getSitioAtual());
                if (arquivoDB != null) {
                    if (SecurityManager.podeAlterarArquivo(usuarioLogado, arquivoDB)) {
                        File theFile = null;
                        if (getArquivosPath() != null) theFile = new File(getArquivosPath() + "arquivos/temp/" + fileUploadFileName); else theFile = new File(getServletContext().getRealPath("arquivos/temp/" + fileUploadFileName));
                        FileUtils.copyFile(fileUpload, theFile);
                        usuarioLogado.addActionMessage(SUBSTITUIR.replace("{fileUploadFileName}", fileUploadFileName));
                        noArquivo = fileUploadFileName;
                        return "substituir";
                    } else {
                        if (arquivoDB.getNuPermissao() == SETOR) {
                            usuarioLogado.addActionError("J&aacute; existe um arquivo com este nome. Somente usu&aacute;rios do mesmo setor podem alterar este arquivo.");
                        } else {
                            usuarioLogado.addActionError("J&aacute; existe um arquivo com este nome. N&atilde;o &eacute; poss&iacute;vel substituir este arquivo.");
                        }
                        return INPUT;
                    }
                }
                String fullFileName = null;
                if (getArquivosPath() != null) {
                    fullFileName = getArquivosPath() + nomeRelativoFisico;
                } else {
                    fullFileName = getServletContext().getRealPath(nomeRelativoFisico);
                }
                File theFile = new File(fullFileName);
                FileUtils.copyFile(fileUpload, theFile);
                logger.debug("fullFileName=" + fullFileName);
                arquivo = new Arquivo();
                arquivo.setNoArquivo(nomeRelativo);
                arquivo.setDeArquivo(descricao);
                arquivo.setLongdesc(longdesc);
                arquivo.setNuUsuario(usuarioLogado.getNuUsuario());
                arquivo.setNuSetor(usuarioLogado.getSetor().getNuSetor());
                arquivo.setNuPermissao(nuPermissao);
                arquivo.setSitio(getSitioAtual());
                if (arquivoDB == null) {
                    arquivoDao.create(arquivo);
                    usuarioLogado.addActionMessage("Arquivo cadastrado corretamente.");
                    logDao.addLog(getUsuarioLogado(), "Cadastrou o arquivo '" + nomeRelativo + "'.");
                } else {
                    arquivoDB.setDeArquivo(descricao);
                    arquivoDao.update(arquivoDB);
                    usuarioLogado.addActionMessage("Arquivo atualizado corretamente.");
                    logDao.addLog(getUsuarioLogado(), "Substituiu o arquivo '" + nomeRelativo + "'.");
                }
            } catch (Exception e) {
                usuarioLogado.addActionError(e.getMessage());
                return INPUT;
            }
        }
        return SUCCESS;
    }
}
