class BackupThread extends Thread {
    public void saveSimulationConfig() throws EasyBotAppException {
        try {
            File xmlFile = new File(Simulation.getCurrent().getBaseSimulationDirectory() + "/simulation.xml");
            File rulesFile = new File("src/core/simulation/simulation-rules.xml");
            File simDir = new File(Simulation.getCurrent().getBaseSimulationDirectory());
            PrintWriter xmlWriter = new PrintWriter(new FileOutputStream(xmlFile));
            xmlWriter.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            xmlWriter.println("<simulation name=\"" + this.getName() + "\" >");
            xmlWriter.println("\t<path>" + this.getBaseSimulationDirectory() + "</path>");
            xmlWriter.println("\t<type>" + this.getType() + "</type>");
            xmlWriter.println("\t<comment>" + this.getComment() + "</comment>");
            xmlWriter.println("\t<steptime>" + this.getStepTime() + "</steptime>");
            for (int i = 0; i < this.getRobotArray().size(); i++) {
                String file = ((Robot) this.getRobotArray().get(i)).getXMLConfigFile();
                xmlWriter.println("\t<robot>");
                xmlWriter.println("\t\t<xmlfile>" + this.getBaseSimulationDirectory() + "\\components\\" + file + "</xmlfile>");
                Plugin robot = ClassManager.getInstance().getPluginByName(((Robot) this.getRobotArray().get(i)).getClass().getCanonicalName());
                xmlWriter.println("\t\t<xml-rules>" + robot.getContext().getPath() + robot.getXmlRulesFile() + "</xml-rules>");
                xmlWriter.println("\t</robot>");
            }
            xmlWriter.println("\t<gps>");
            xmlWriter.println("\t\t<xmlfile>" + this.getBaseSimulationDirectory() + "\\components\\gps.xml</xmlfile>");
            Plugin gps = ClassManager.getInstance().getPluginByName(Simulation.getCurrent().getGps().getClass().getCanonicalName());
            xmlWriter.println("\t\t<xml-rules>" + gps.getContext().getPath() + gps.getXmlRulesFile() + "</xml-rules>");
            xmlWriter.println("\t</gps>");
            xmlWriter.println("\t<places>");
            for (int i = 0; i < places.length; i++) {
                String row = new String();
                for (int j = 0; j < places[i].length; j++) row = row.concat(places[i][j] + Places.COLUMN_DELIMITER);
                xmlWriter.println("\t\t<place>" + row + "</place>");
            }
            xmlWriter.println("\t</places>");
            xmlWriter.println("</simulation>");
            xmlWriter.close();
            FileUtils.copyFileToDirectory(rulesFile, simDir);
        } catch (Exception e) {
            throw new EasyBotAppException(e.getMessage());
        }
    }
}
