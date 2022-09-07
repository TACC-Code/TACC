class BackupThread extends Thread {
    public static void main(String[] args) {
        Id fromNodeId;
        Id toNodeId;
        log.info("starting RouterVis demo");
        String testConfigFile = "./examples/siouxfalls/config.xml";
        if (args.length == 3) {
            Gbl.createConfig(new String[] { args[0], "config_v1.dtd" });
            fromNodeId = new IdImpl(args[1]);
            toNodeId = new IdImpl(args[2]);
        } else {
            log.info(" reading default config file: " + testConfigFile);
            Gbl.createConfig(new String[] { testConfigFile });
            fromNodeId = new IdImpl("13");
            toNodeId = new IdImpl("7");
        }
        log.info(" done.");
        log.info("  reading the network...");
        NetworkLayer network = null;
        network = (NetworkLayer) Gbl.getWorld().createLayer(NetworkLayer.LAYER_TYPE, null);
        new MatsimNetworkReader(network).readFile(Gbl.getConfig().network().getInputFile());
        log.info("  done.");
        log.info("  creating output dir if needed");
        File outputDir = new File(Gbl.getConfig().controler().getOutputDirectory());
        if (!outputDir.exists()) {
            outputDir.mkdir();
        } else if (outputDir.list().length > 0) {
            log.error("The output directory " + outputDir + " exists already but has files in it! Please delete its content or the directory and start again. We will not delete or overwrite any existing files.");
            System.exit(-1);
        }
        log.info("done");
        log.info("  creating RouterVis object.");
        TravelTimeI costCalc = new FreespeedTravelTimeCost();
        RouterVis vis = new RouterVis(network, (TravelCostI) costCalc, costCalc);
        log.info("  done.");
        log.info("  running RouterVis.");
        Node fromNode = network.getNode(fromNodeId.toString());
        Node toNode = network.getNode(toNodeId.toString());
        vis.runRouter(fromNode, toNode, 0.0);
        log.info("  done.");
        log.info("  starting NetVis.");
        String[] visargs = { Gbl.getConfig().controler().getOutputDirectory() + "/Snapshot" };
        NetVis.main(visargs);
        log.info("  done.");
    }
}
