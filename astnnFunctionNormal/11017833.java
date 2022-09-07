class BackupThread extends Thread {
    public static DirichletBayesIm bifToDirichletBayesIm(File bifFile) {
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(bifFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found: " + bifFile.getAbsolutePath());
        }
        String fileContent = null;
        FileChannel fc = fs.getChannel();
        try {
            ByteBuffer bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
            bb.flip();
            fileContent = new String(bb.array());
            fc.close();
            fc = null;
        } catch (IOException e) {
            System.out.println("Cannot get size: " + e);
        }
        Dag g = new Dag();
        java.util.regex.Pattern var = java.util.regex.Pattern.compile("\\bvariable\\s*(\\w+)\\s*");
        Matcher variable = var.matcher(fileContent);
        while (variable.find()) {
            Node tmpNode = new GraphNode(variable.group(1));
            g.addNode(tmpNode);
        }
        java.util.regex.Pattern prob = java.util.regex.Pattern.compile("\\bprobability\\s*\\(\\s*(\\w*)\\s*\\|+\\s*(\\w.*?)\\s*\\)", java.util.regex.Pattern.DOTALL);
        Matcher probability = prob.matcher(fileContent);
        while (probability.find()) {
            boolean hasParents = probability.groupCount() > 1;
            Node node = g.getNode(probability.group(1));
            if (hasParents) {
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(",\\s*");
                String[] parents = p.split(probability.group(2));
                for (int i = 0; i < parents.length; i++) g.addDirectedEdge(g.getNode(parents[i]), node);
            }
        }
        BayesPm pm = new BayesPm(g);
        var = java.util.regex.Pattern.compile("\\bvariable\\s*(\\w+)\\s*\\{.*?\\[\\s*(\\d+)\\s*\\]\\s*\\{\\s*(.*?)\\s*\\}", java.util.regex.Pattern.DOTALL);
        variable = var.matcher(fileContent);
        while (variable.find()) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(",\\s*");
            String categories[] = p.split(variable.group(3));
            pm.setCategories(g.getNode(variable.group(1)), Arrays.asList(categories));
        }
        DirichletBayesIm im = DirichletBayesIm.blankDirichletIm(pm);
        prob = java.util.regex.Pattern.compile("\\bprobability\\s*\\((.*?)\\)\\s*\\{(.*?)\\}", java.util.regex.Pattern.DOTALL);
        probability = prob.matcher(fileContent);
        while (probability.find()) {
            String header = probability.group(1);
            String body = probability.group(2);
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\b\\w+\\b");
            Matcher m = p.matcher(header);
            m.find();
            Node node = im.getNode(m.group());
            int nodeIndex = im.getNodeIndex(node);
            boolean hasParents = header.matches(".*\\|.*");
            if (hasParents) {
                int nRow = im.getNumRows(nodeIndex);
                int nCol = im.getNumColumns(nodeIndex);
                p = java.util.regex.Pattern.compile("\\d*\\.*\\d+");
                m = p.matcher(body);
                for (int i = 0; i < nRow; i++) for (int j = 0; j < nCol; j++) {
                    m.find();
                    double value = new Double(m.group()).doubleValue();
                    im.setPseudocount(nodeIndex, i, j, value);
                }
            } else {
                p = java.util.regex.Pattern.compile("\\d*\\.*\\d+");
                m = p.matcher(body);
                int i = 0;
                while (m.find()) {
                    double value = new Double(m.group()).doubleValue();
                    im.setPseudocount(nodeIndex, 0, i, value);
                    i++;
                }
            }
        }
        System.out.println(im);
        return im;
    }
}
