class BackupThread extends Thread {
    public static void main(String[] args) {
        String input01 = "/Users/johanwjoubert/MATSim/workspace/MATSimData/Gauteng/Activities/GautengMinorClusterOutput_20_30.txt";
        String input02 = "/Users/johanwjoubert/R-Source/Code/bt.txt";
        String input03 = "/Users/johanwjoubert/R-Source/Code/dc.txt";
        String input04 = "/Users/johanwjoubert/R-Source/Code/ec.txt";
        String outputFilename = "/Users/johanwjoubert/MATSim/workspace/MATSimData/Gauteng/Activities/GautengMinorClusterSNA_20_30.txt";
        try {
            Scanner inputCluster = new Scanner(new BufferedReader(new FileReader(new File(input01))));
            Scanner inputBetween = new Scanner(new BufferedReader(new FileReader(new File(input02))));
            Scanner inputDegree = new Scanner(new BufferedReader(new FileReader(new File(input03))));
            Scanner inputEigen = new Scanner(new BufferedReader(new FileReader(new File(input04))));
            BufferedWriter output = new BufferedWriter(new FileWriter(new File(outputFilename)));
            int lineCounter = 0;
            try {
                output.write("ClusterId,Long,Lat,Activities,Between,Degree,Eigen");
                output.newLine();
                inputCluster.nextLine();
                while (inputCluster.hasNextLine()) {
                    String lineString = inputCluster.nextLine();
                    String[] line = lineString.split(",");
                    if (line.length == 4) {
                        output.write(lineString);
                        output.write(",");
                        output.write(inputBetween.nextLine());
                        output.write(",");
                        output.write(inputDegree.nextLine());
                        output.write(",");
                        output.write(inputEigen.nextLine());
                        output.newLine();
                        lineCounter++;
                    }
                }
            } finally {
                output.close();
            }
            log.info("Number of lines processed: " + lineCounter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
