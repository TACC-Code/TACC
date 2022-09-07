class BackupThread extends Thread {
    public static void load(String rootDir) throws SimulationException {
        try {
            Factory factory = new Factory(rootDir + File.separator + "simulation.xml", rootDir + File.separator + "simulation-rules.xml");
            if (factory == null) throw new SimulationException("No pudo crearse la factor�a para " + "levantar la simulaci�n.");
            simulation = (Simulation) factory.digest();
            if (simulation == null) throw new SimulationException("No pudo cargarse la simulaci�n.");
        } catch (IOException e) {
            throw new SimulationException(e.getMessage());
        } catch (SAXException e) {
            throw new SimulationException(e.getMessage());
        }
    }
}
