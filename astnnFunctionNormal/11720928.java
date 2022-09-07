class BackupThread extends Thread {
    public void digestGPS(String xmlFile, String xmlRulesFile) throws SimulationException {
        try {
            Factory factory = new Factory(xmlFile, xmlRulesFile);
            if (factory == null) throw new SimulationException("No pudo crearse la factor�a para " + "levantar el GPS.");
            GPS gps = (GPS) factory.digest();
            if (gps == null) throw new SimulationException("No pudo cargarse el GPS.");
            simulation.setGps(gps);
        } catch (IOException e) {
            throw new SimulationException(e.getMessage());
        } catch (SAXException e) {
            throw new SimulationException(e.getMessage());
        }
    }
}
