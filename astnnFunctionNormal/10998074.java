class BackupThread extends Thread {
    public void write(List<JUTestHelper> tests) throws Exception {
        if (tests != null) {
            try {
                this.setInsert(!this.hasTest());
                for (JUTestHelper test : tests) {
                    if (!test.isReadOnly()) {
                        ClassHelper clazzImpl = generateImplementation(test);
                        FileUtil.writeClassFile(test.getAbsolutePath(), clazzImpl, true, true, true);
                    }
                }
                Configurator reader = new Configurator();
                if (!reader.writeTests(tests, xml, insert)) {
                    throw new Exception("erro ao gravar em arquivo de configuração do Demoiselle!");
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }
}
