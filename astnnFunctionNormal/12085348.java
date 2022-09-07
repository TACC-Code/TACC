class BackupThread extends Thread {
    public static void parse(String templateFile, String outputFile, Map<String, Object> map) {
        try {
            VelocityContext context = new VelocityContext();
            for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                context.put(key, map.get(key));
            }
            Template template = null;
            try {
                Properties p = new Properties();
                p.put(RuntimeInstance.INPUT_ENCODING, "UTF-8");
                p.put(RuntimeInstance.OUTPUT_ENCODING, "UTF-8");
                p.put("file.resource.loader.path", "");
                Velocity.init(p);
                template = Velocity.getTemplate(templateFile);
                BufferedWriter writer = writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(templateFile + ".merge")));
                if (template != null) {
                    template.merge(context, writer);
                }
                writer.flush();
                writer.close();
                File merge = new File(templateFile + ".merge");
                FileUtils.copyFile(merge, new File(outputFile));
                merge.delete();
            } catch (ResourceNotFoundException rnfe) {
                System.out.println("Error : cannot find template " + templateFile + ":" + rnfe);
                rnfe.printStackTrace();
            } catch (ParseErrorException pee) {
                System.out.println("Error : Syntax error in template " + templateFile + ":" + pee);
                pee.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
