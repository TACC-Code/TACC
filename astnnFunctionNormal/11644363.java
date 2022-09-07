class BackupThread extends Thread {
    public EscapeCodeEatingConsole(Reader reader, PrintWriter writer) {
        this.writer = writer;
        RegExpEatingReader eater = new RegExpEatingReader(reader, 128);
        this.reader = eater;
        Collection<RegExp> regexps = new HashSet<RegExp>();
        regexps.add(new RegExp("\033\\[[1-6]\\~"));
        regexps.add(new RegExp("\033\\[[A-D]"));
        regexps.add(new RegExp("\033O[l-np-yMP-S]"));
        regexps.add(new RegExp("\033\\[(1[789]|2[013-689]|3[1-4])\\~"));
        regexps.add(new RegExp("\033\\[\\??[0-9]+(;[0-9]+)*[hl]"));
        regexps.add(new RegExp("\033\\[[0-9]*[A-D]"));
        regexps.add(new RegExp("\033\\[[0-9]+;[0-9]+[Hf]"));
        regexps.add(new RegExp("\033[DME78]"));
        regexps.add(new RegExp("\033H"));
        regexps.add(new RegExp("\033\\[[03]?g"));
        regexps.add(new RegExp("\033\\[[0-9]+(;[0-9]+)*m"));
        regexps.add(new RegExp("\033\\[[012]\\\"q"));
        regexps.add(new RegExp("\033\\[[0-9]+[LM@P]"));
        regexps.add(new RegExp("\033\\[[0-9]+X"));
        regexps.add(new RegExp("\033\\[\\??[012]?[KJ]"));
        regexps.add(new RegExp("\033\\[[0-9]+;[0-9]+r"));
        eater.eat(regexps);
    }
}
