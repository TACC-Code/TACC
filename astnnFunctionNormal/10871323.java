class BackupThread extends Thread {
    public static void speciate(String input, String output, int num_skipped) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(input));
            Vector uris = new Vector();
            String rline = "";
            int counter = 1;
            while ((rline = reader.readLine()) != null) {
                if (counter < num_skipped) {
                    counter++;
                    continue;
                }
                rline = rline.trim();
                uris.addElement(rline);
            }
            BufferedWriter logger = new BufferedWriter(new FileWriter("PelletSpeciatorLog.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            for (int i = 0; i < uris.size(); i++) {
                long startTime = System.currentTimeMillis();
                String uri = (String) uris.elementAt(i);
                String originalURI = uri.toString();
                logger.write("Pellet Checking: [" + (i + counter) + "] " + originalURI);
                logger.newLine();
                System.out.println("Pellet Checking: [" + (i + counter) + "] " + originalURI);
                uri = uri.replaceAll(":", "%3A");
                uri = uri.replaceAll("/", "%2F");
                uri = uri.replaceAll("\\?", "%3F");
                uri = uri.replaceAll(",", "%2C");
                uri = uri.replaceAll("=", "%3D");
                uri = uri.replaceAll("\\+", "%2B");
                String status = ERROR;
                try {
                    URL url = new URL(PELLET_CHECKER_PREFIX + uri + PELLET_CHECKER_OPTIONS);
                    System.out.println(url);
                    URLConnection myConnection = url.openConnection();
                    BufferedReader myReader = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                    String line = "";
                    String species = ERROR;
                    while ((line = myReader.readLine()) != null) {
                        if (line.startsWith("<b>OWL Species:")) {
                            int sindex = line.indexOf("</b>");
                            int eindex = line.indexOf("<br>");
                            species = line.substring(sindex + 4, eindex);
                            break;
                        }
                    }
                    System.out.println("!! " + species + " !!");
                    writer.write(originalURI + "\t" + species);
                    writer.newLine();
                    writer.flush();
                    logger.write(" - " + originalURI + " is " + species);
                    logger.newLine();
                    logger.flush();
                } catch (Exception e) {
                    logger.write(e.toString());
                    logger.newLine();
                } finally {
                    long stopTime = System.currentTimeMillis();
                    double duration = (stopTime - startTime) / 10000d;
                    logger.write("took " + duration + " seconds ");
                    logger.newLine();
                    logger.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
