class BackupThread extends Thread {
    public static void main(String[] args) {
        String inputFileName = DoubleCheck.getParam(args, "-i");
        String outputFileName = DoubleCheck.getParam(args, "-o");
        String stopFileName = DoubleCheck.getParam(args, "-s");
        String bayesPositive = DoubleCheck.getParam(args, "-p");
        String bayesNegative = DoubleCheck.getParam(args, "-n");
        boolean quietFlag = DoubleCheck.isParam(args, "-q");
        boolean xmlFlag = DoubleCheck.isParam(args, "-x");
        List<String> stopWords = new ArrayList<String>();
        try {
            if (inputFileName == null) {
                DoubleCheck.writeConsole("Error: no input file specified.\n", quietFlag);
                return;
            } else if (outputFileName == null) {
                DoubleCheck.writeConsole("Error: no output file specified.\n", quietFlag);
                return;
            }
            File output = new File(outputFileName);
            if (output.exists() && DoubleCheck.isParam(args, "-w")) {
                output.delete();
            } else if (output.exists()) {
                DoubleCheck.writeConsole("Error: output file already exists.\n", quietFlag);
                return;
            }
            if (stopFileName != null) {
                File stop = new File(stopFileName);
                if (!stop.exists()) {
                    DoubleCheck.writeConsole("Error: stil file not found.\n", quietFlag);
                    return;
                } else {
                    stopWords = DataProcessor.getStopList(stopFileName);
                }
            }
            double positive = 1;
            try {
                if (bayesPositive != null) {
                    positive = Double.parseDouble(bayesPositive);
                }
            } catch (Exception e) {
            }
            double negative = 1;
            try {
                if (bayesNegative != null) {
                    negative = Double.parseDouble(bayesNegative);
                }
            } catch (Exception e) {
            }
            Data data = DataProcessor.loadData(inputFileName);
            if (data == null) {
                DoubleCheck.writeConsole("Error: The data was not loaded successfully.\n", quietFlag);
                return;
            }
            File main = new File("data");
            if (main.exists() && DoubleCheck.isParam(args, "-f")) {
                DataProcessor.deleteDir(main);
            } else if (main.exists()) {
                DoubleCheck.writeConsole("Error: The temporary data director found.\n", quietFlag);
                return;
            } else {
                main.mkdir();
            }
            StringBuilder sb = new StringBuilder();
            Suggestions suggestions = new Suggestions();
            DoubleCheck.writeConsole("Processing...\n", quietFlag);
            double goal = data.getCategories().size();
            for (int i = 0; i < data.getCategories().size(); i++) {
                Category cat = data.getCategories().get(i);
                File loc = DataProcessor.saveFiles(main, stopWords, data, cat);
                List<String> misclassified = ClassificationValidation.run(loc, positive, negative);
                for (int j = 0; j < misclassified.size(); j++) {
                    String result = misclassified.get(j);
                    String[] info = result.split(":");
                    ItemSuggestion s = data.misclassifiedValue(info[0], cat, Double.parseDouble(info[1]));
                    suggestions.getSuggestions().add(s);
                    sb.append(s + "\n");
                }
                if (misclassified.size() > 0) {
                    sb.append("\n");
                }
                double percent = Math.round((i / goal) * 10000.0) / 100.0;
                DoubleCheck.writeConsole("\r" + percent + "%", quietFlag);
            }
            DoubleCheck.writeConsole("\r100.0%\n", quietFlag);
            DoubleCheck.writeConsole("Cleaning up...\n", quietFlag);
            DataProcessor.deleteDir(main);
            if (xmlFlag) {
                Serializer serializer = new Persister();
                serializer.write(suggestions, output);
            } else {
                DataProcessor.writeFile(output, sb.toString());
            }
            DoubleCheck.writeConsole("Success!\n", quietFlag);
        } catch (Exception e) {
            DoubleCheck.writeConsole("Error: " + e.getMessage() + "\n", quietFlag);
        }
    }
}
