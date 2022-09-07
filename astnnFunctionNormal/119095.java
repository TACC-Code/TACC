class BackupThread extends Thread {
    public static void dumpPart(Part p) throws Exception {
        if (p instanceof Message) dumpEnvelope((Message) p);
        System.out.println("CONTENT-TYPE: " + p.getContentType());
        Object o = p.getContent();
        if (o instanceof String) {
            System.out.println("This is a String");
            System.out.println("---------------------------");
            System.out.println((String) o);
        } else if (o instanceof Multipart) {
            System.out.println("This is a Multipart");
            System.out.println("---------------------------");
            Multipart mp = (Multipart) o;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) dumpPart(mp.getBodyPart(i));
        } else if (o instanceof Message) {
            System.out.println("This is a Nested Message");
            System.out.println("---------------------------");
            dumpPart((Part) o);
        } else if (o instanceof InputStream) {
            System.out.println("This is just an input stream");
            System.out.println("---------------------------");
            InputStream is = (InputStream) o;
            int c;
            while ((c = is.read()) != -1) System.out.write(c);
        }
    }
}
