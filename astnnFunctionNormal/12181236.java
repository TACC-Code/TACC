    public PetriNetSimulation() {
        GraphInstance graphInstance = new GraphInstance();
        GraphContainer con = ContainerSingelton.getInstance();
        MainWindow w = MainWindowSingelton.getInstance();
        w.setLockedPane(true);
        try {
            JOptionPane.showMessageDialog(w, "To simulate your Petri Net you have to meet some requirements:" + '\n' + "You need a valid Dymola installation (license inclusive). Please specify the path to the Dymola installation folder in the next dialog. Make sure that there also is a PNlib_ver1_4.mo, a myrandom.c and a dsmodel.c file!" + '\n' + '\n' + "Simulation can take some time...", "Dymola installation folder required...", JOptionPane.QUESTION_MESSAGE);
            JFileChooser chooser = new JFileChooser(path);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(w) == JFileChooser.APPROVE_OPTION) {
                path = chooser.getSelectedFile().getAbsolutePath();
            } else return;
            if (path.charAt(path.length() - 1) != '\\') path += "\\";
            new File(path + "simulate.mo").delete();
            new File(path + "simulate.mat").delete();
            new File(path + "simulate.csv").delete();
            new File(path + "simulation.mos").delete();
            new MOoutput(new File(path + "simulation.mo"), graphInstance.getPathway());
            FileWriter fstream = new FileWriter(path + "simulation.mos");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("cd(\"" + path.substring(0, path.length() - 1) + "\");\r\n");
            out.write("import PNlib_ver1_4.mo;\r\n");
class BackupThread extends Thread {
            out.write("import simulation.mo;\r\n");
            out.write("simulateModel(\"simulation\", stopTime=30, method=\"dassl\", resultFile=\"simulate\");\r\n");
            out.write("fileName=\"simulate.mat\";\r\n");
            out.write("CSVfile=\"simulate.csv\";\r\n");
            out.write("n=readTrajectorySize(fileName);\r\n");
            out.write("names = readTrajectoryNames(fileName);\r\n");
            out.write("traj=readTrajectory(fileName,names,n);\r\n");
            out.write("traj_transposed=transpose(traj);\r\n");
            out.write("DataFiles.writeCSVmatrix(CSVfile, names, traj_transposed);\r\n");
            out.write("exit();\r\n");
            out.close();
            final Process p = new ProcessBuilder(path + "bin\\Dymola.exe", "/nowindow", path + "simulation.mos").start();
            Thread t = new Thread() {

                public void run() {
                    long totalTime = 120000;
                    try {
                        for (long t = 0; t < totalTime; t += 1000) {
                            sleep(1000);
                        }
                        p.destroy();
                    } catch (Exception e) {
                    }
                }
            };
            t.start();
            p.waitFor();
            try {
                t.stop();
            } catch (Exception e) {
            }
            ;
            if (con.containsPathway() && graphInstance.getPathway().hasGotAtLeastOneElement()) {
                graphInstance.getPathway().setPetriNet(true);
                PetriNet petrinet = graphInstance.getPathway().getPetriNet();
                petrinet.setPetriNetSimulationFile(path + "simulate.csv");
                petrinet.initializePetriNet();
            } else throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainWindowSingelton.getInstance(), "Something went wrong. The model couldn't be simulated!", "Error occured...", JOptionPane.ERROR_MESSAGE);
        }
        w.setLockedPane(false);
    }
}
