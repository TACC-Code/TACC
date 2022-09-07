class BackupThread extends Thread {
    public void digestRobots(String xmlFile, String xmlRulesFile) throws SimulationException {
        try {
            Factory factory = new Factory(xmlFile, xmlRulesFile);
            if (factory == null) throw new SimulationException("No pudo crearse la factor�a para " + "levantar los robots.");
            Robot robot = (Robot) factory.digest();
            if (robot == null) throw new SimulationException("No pudo cargarse el robot.");
            simulation.addRobot(robot);
        } catch (IOException e) {
            throw new SimulationException(e.getMessage());
        } catch (SAXException e) {
            throw new SimulationException(e.getMessage());
        }
    }
}
