class BackupThread extends Thread {
    protected static void substitute(String path, Map<String, String> parameterMap) {
        FileReader xmlContentReader = null;
        BufferedReader bufferedReader = null;
        PrintWriter outputWriter = null;
        try {
            File inputFile = new File(path);
            if (inputFile.exists() && inputFile.isFile() && inputFile.canRead() && inputFile.canWrite()) {
                File xmlTempFile = File.createTempFile(inputFile.getName(), ".xml");
                xmlTempFile.deleteOnExit();
                FileUtils.copyFile(inputFile, xmlTempFile);
                xmlContentReader = new FileReader(xmlTempFile);
                bufferedReader = new BufferedReader(xmlContentReader);
                StringBuilder contents = new StringBuilder();
                String currentLine = "";
                while ((currentLine = bufferedReader.readLine()) != null) {
                    contents = contents.append(currentLine);
                }
                String contentToSubstitute = contents.toString();
                if (contentToSubstitute.indexOf("http://schemas.microsoft.com") != -1) {
                    for (String key : parameterMap.keySet()) {
                        contentToSubstitute = contentToSubstitute.replaceAll("\\$" + key + "\\$", parameterMap.get(key));
                    }
                    outputWriter = new PrintWriter(new FileOutputStream(path));
                    outputWriter.write(contentToSubstitute);
                    logger.info("Document at :" + path + " substituted");
                } else {
                    logger.warning("Document at :" + path + " not substituted : Content un-readable");
                }
            } else {
                logger.warning("Document at : " + path + " not substituted : Not a valid file");
            }
        } catch (FileNotFoundException e) {
            logger.warning("Document at : " + path + " not substituted : " + e);
        } catch (IOException e) {
            logger.warning("Document at : " + path + " not substituted : " + e);
        } finally {
            try {
                if (xmlContentReader != null) {
                    xmlContentReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (outputWriter != null) {
                    outputWriter.close();
                }
            } catch (IOException e) {
                logger.warning("Document at : " + path + "not substituted : " + e);
            }
        }
    }
}
