class BackupThread extends Thread {
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        try {
            String dirTemp = getTargetDir() + "/temp";
            if (new File(dirTemp).exists()) FileUtils.forceDelete(new File(dirTemp));
            File ear = new File(getAppInTargetDir() + ".ear");
            if (!ear.exists()) {
                throw new MojoExecutionException("N�o foi poss�vel encontrar arquivo. Rode a tarefa de cria��o do Ear e tente novamente.");
            }
            PlcZip zip = new PlcZip(getAppInTargetDir() + ".ear");
            zip.descompactar(dirTemp);
            zip.close();
            String nomeProjeto = project.getArtifactId().replace("_ear", "");
            Document jdomDocumentOriginal = null;
            try {
                SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
                saxBuilder.setValidation(false);
                saxBuilder.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                saxBuilder.setFeature("http://xml.org/sax/features/namespaces", false);
                saxBuilder.setFeature("http://xml.org/sax/features/validation", false);
                saxBuilder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                Document jdomDocument = saxBuilder.build(new File(dirTemp + "/META-INF/application.xml"));
                jdomDocumentOriginal = (Document) jdomDocument.clone();
                List nodeList = XPath.selectNodes(jdomDocument, "/application/module");
                for (Iterator iter = nodeList.iterator(); iter.hasNext(); ) {
                    Element element = (Element) iter.next();
                    if (element.getChild("web") != null) element.detach();
                }
                OutputStream output = new FileOutputStream(new File(dirTemp + "/META-INF/application.xml"));
                XMLOutputter outputter = new XMLOutputter();
                outputter.output(jdomDocument, output);
            } catch (JDOMException e) {
                e.printStackTrace();
            }
            String fileApplication = "";
            fileApplication = FileUtils.readFileToString(new File(dirTemp + "/META-INF/application.xml"), "UTF-8");
            fileApplication = fileApplication.replace("jcompany_model", "jcompany_commons");
            fileApplication = fileApplication.replace("<ejb>", "<java>");
            fileApplication = fileApplication.replace("</ejb>", "</java>");
            fileApplication = fileApplication.replace("<java>" + nomeProjeto + "_modelo.jar</java>", "<ejb>" + nomeProjeto + "_modelo.jar</ejb>");
            FileUtils.writeStringToFile(new File(dirTemp + "/META-INF/application.xml"), fileApplication, "UTF-8");
            File jbossApp = new File(dirTemp + "/META-INF/jboss-app.xml");
            if (!jbossApp.exists()) {
                String conteudo = " <!DOCTYPE jboss-app\n" + "PUBLIC \"-//JBoss//DTD J2EE Application 1.4//EN\"\n" + "\"http://www.jboss.org/j2ee/dtd/jboss-app_4_0.dtd\">\n" + "<jboss-app>\n" + "	<loader-repository>" + nomeProjeto + ":app=ejb3</loader-repository>\n" + "</jboss-app>\n";
                FileUtils.writeStringToFile(new File(dirTemp + "/META-INF/jboss-app.xml"), conteudo, "UTF-8");
            }
            String modelo = "";
            String comuns = "";
            String app_comuns = "";
            String app_modelo = "";
            File f = new File(getTargetDir() + "/temp");
            String s[] = f.list();
            for (int i = 0; i < s.length; i++) {
                String string = s[i];
                if (!string.startsWith("jcompany_commons") && !string.startsWith("jcompany_model") && !string.startsWith(nomeProjeto) && !string.equals("META-INF")) FileUtils.forceDelete(new File(getTargetDir() + "/temp/" + string));
                if (string.startsWith("jcompany_model")) modelo = string;
                if (string.startsWith("jcompany_commons")) comuns = string;
                if (string.startsWith(nomeProjeto + "_comuns")) app_comuns = string;
                if (string.startsWith(nomeProjeto + "_modelo")) app_modelo = string;
            }
            zip = new PlcZip(dirTemp + "/" + modelo);
            zip.descompactar(dirTemp + "/temp");
            zip.close();
            FileUtils.forceDelete(new File(dirTemp + "/" + modelo));
            zip = new PlcZip(dirTemp + "/" + app_comuns);
            zip.descompactar(dirTemp + "/temp");
            zip.close();
            FileUtils.forceDelete(new File(dirTemp + "/" + app_comuns));
            zip = new PlcZip(dirTemp + "/" + app_modelo);
            zip.descompactar(dirTemp + "/temp");
            zip.close();
            FileUtils.forceDelete(new File(dirTemp + "/" + app_modelo));
            String filePersistence = "";
            filePersistence = FileUtils.readFileToString(new File(dirTemp + "/temp/META-INF/persistence.xml"), "UTF-8");
            filePersistence = filePersistence.replace("jdbc/", "java:/");
            FileUtils.writeStringToFile(new File(dirTemp + "/temp/META-INF/persistence.xml"), filePersistence, "UTF-8");
            PlcJar.jar(app_modelo, new File(dirTemp), new File[] { new File(dirTemp + "/temp") });
            zip = new PlcZip(dirTemp + "/" + nomeProjeto + ".war");
            zip.descompactar(dirTemp + "/war");
            zip.close();
            FileUtils.forceDelete(new File(dirTemp + "/" + nomeProjeto + ".war"));
            File jbossWeb = new File(dirTemp + "/war/WEB-INF/jboss-web.xml");
            if (!jbossWeb.exists()) {
                String conteudo = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<jboss-web>\n" + "<!--security-domain>java:/jaas/jsecurity</security-domain-->\n" + "<resource-ref>\n" + "<res-ref-name>jdbc/" + nomeProjeto + "</res-ref-name>\n" + "<res-type>javax.sql.DataSource</res-type>\n" + "<jndi-name>java:" + nomeProjeto + "</jndi-name>\n" + "</resource-ref>\n" + "</jboss-web>\n";
                FileUtils.writeStringToFile(new File(dirTemp + "/war/WEB-INF/jboss-web.xml"), conteudo, "UTF-8");
            }
            f = new File(dirTemp + "/war/WEB-INF/lib");
            s = f.list();
            for (int i = 0; i < s.length; i++) {
                String string = s[i];
                if (string.startsWith("jsf-api") || string.startsWith("jsf-impl") || string.startsWith("javaee-5") || string.startsWith("dom4j") || string.startsWith("ejb3-persistence") || string.startsWith("hibernate") || string.startsWith(nomeProjeto + "_modelo") || string.startsWith("jcompany_model") || string.startsWith("jbossall-client") || string.startsWith("javassit")) FileUtils.forceDelete(new File(dirTemp + "/war/WEB-INF/lib/" + string));
            }
            if (System.getProperty("ejb.distribuido") == null || System.getProperty("ejb.distribuido").equals("")) {
                String comuns_war = "";
                String comuns_app = "";
                s = new File(dirTemp + "/war/WEB-INF/lib/").list();
                for (int i = 0; i < s.length; i++) {
                    String string = s[i];
                    if (string.startsWith("jcompany_commons")) comuns_war = string;
                    if (string.startsWith(nomeProjeto + "_comuns")) comuns_app = string;
                }
                zip = new PlcZip(dirTemp + "/war/WEB-INF/lib/" + comuns_app);
                zip.descompactar(dirTemp + "/temp/" + nomeProjeto + "_comuns");
                zip.close();
                FileUtils.forceDelete(new File(dirTemp + "/war/WEB-INF/lib/" + comuns_app));
                try {
                    SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
                    saxBuilder.setValidation(false);
                    saxBuilder.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                    saxBuilder.setFeature("http://xml.org/sax/features/namespaces", false);
                    saxBuilder.setFeature("http://xml.org/sax/features/validation", false);
                    saxBuilder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                    Document jdomDocument = saxBuilder.build(new File(dirTemp + "/temp/" + nomeProjeto + "_comuns/hibernate.cfg.xml"));
                    List nodeList = XPath.selectNodes(jdomDocument, "/hibernate-configuration/session-factory/mapping");
                    for (Iterator iter = nodeList.iterator(); iter.hasNext(); ) {
                        Element element = (Element) iter.next();
                        if (element.getAttribute("class") != null) {
                            File fDel = new File(dirTemp + "/temp/" + nomeProjeto + "_comuns/" + element.getAttribute("class").getValue().replace('.', File.separatorChar) + ".class");
                            if (fDel.exists()) FileUtils.forceDelete(fDel);
                            fDel = new File(dirTemp + "/temp/" + nomeProjeto + "_comuns/" + element.getAttribute("class").getValue().replace('.', File.separatorChar).replace("Entity", "").replace("VO", "") + ".class");
                            if (fDel.exists()) FileUtils.forceDelete(fDel);
                        }
                    }
                } catch (JDOMException e) {
                    e.printStackTrace();
                }
                Iterator ite = FileUtils.iterateFiles(new File(dirTemp + "/temp/" + nomeProjeto + "_comuns"), null, true);
                while (ite.hasNext()) {
                    File file = (File) ite.next();
                    if (file.getPath().contains(File.separator + "facade" + File.separator)) FileUtils.forceDelete(file);
                }
                PlcJar.jar(comuns_app, new File(dirTemp + "/war/WEB-INF/lib/"), new File[] { new File(dirTemp + "/temp/" + nomeProjeto + "_comuns") });
                zip = new PlcZip(dirTemp + "/war/WEB-INF/lib/" + comuns_war);
                zip.descompactar(dirTemp + "/temp/jcompany_commons");
                zip.close();
                FileUtils.forceDelete(new File(dirTemp + "/war/WEB-INF/lib/" + comuns_war));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/IPlcArquivoVO.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/PlcArgVO.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/PlcArquivoVO.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/PlcBaseContextVO.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/PlcBaseUsuarioPerfilVO.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/PlcBaseEntity.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/facade/IPlcFacade.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/facade/IPlcFacadeRemote.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/dominio/valida/PlcValidacaoUnificada.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/dominio/valida/PlcValidacaoUnificada$AccessType.class"));
                FileUtils.forceDelete(new File(dirTemp + "/temp/jcompany_commons/com/powerlogic/jcompany/comuns/anotacao"));
                PlcJar.jar(comuns_war, new File(dirTemp + "/war/WEB-INF/lib/"), new File[] { new File(dirTemp + "/temp/jcompany_commons") });
            }
            FileUtils.copyFileToDirectory(new File(this.localRepository.getBasedir() + "/log4j/log4j/1.2.13/log4j-1.2.13.jar"), new File(dirTemp + "/war/WEB-INF/lib/"));
            PlcJar.jar(nomeProjeto + ".war", new File(dirTemp), new File[] { new File(dirTemp + "/war") });
            PlcJar.jar(nomeProjeto + "_model.ear", getTargetDir(), new File[] { new File(dirTemp + "/" + comuns), new File(dirTemp + "/" + app_modelo), new File(dirTemp + "/META-INF") }, dirTemp);
            try {
                List nodeList = XPath.selectNodes(jdomDocumentOriginal, "/application/module");
                for (Iterator iter = nodeList.iterator(); iter.hasNext(); ) {
                    Element element = (Element) iter.next();
                    if (element.getChild("ejb") != null || element.getChild("jar") != null) element.detach();
                }
                OutputStream output = new FileOutputStream(new File(dirTemp + "/META-INF/application.xml"));
                XMLOutputter outputter = new XMLOutputter();
                outputter.output(jdomDocumentOriginal, output);
            } catch (JDOMException e) {
                e.printStackTrace();
            }
            PlcJar.jar(nomeProjeto + "_war.ear", getTargetDir(), new File[] { new File(dirTemp + "/" + nomeProjeto + ".war"), new File(dirTemp + "/META-INF") }, dirTemp);
            FileUtils.forceDelete(new File(getTargetDir() + "/temp/temp"));
            FileUtils.forceDelete(new File(getTargetDir() + "/temp/war"));
            FileUtils.forceDelete(new File(getTargetDir() + "/temp"));
            String jbossHome = System.getenv("JBOSS_HOME");
            String plcHome = System.getenv("HOME_PLC");
            String autoDeployPath = null;
            if (jbossHome == null) {
                autoDeployPath = plcHome + File.separator + "servers/jboss/server/default/deploy";
            } else {
                autoDeployPath = jbossHome + File.separator + "server/default/deploy";
            }
            FileUtils.copyFileToDirectory(new File(getTargetDir() + File.separator + nomeProjeto + "_model.ear"), new File(autoDeployPath));
            FileUtils.copyFileToDirectory(new File(getTargetDir() + File.separator + nomeProjeto + "_war.ear"), new File(autoDeployPath));
            getLog().info("Novos arquivos ear foram gerados em " + autoDeployPath);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
