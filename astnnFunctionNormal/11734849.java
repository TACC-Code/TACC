class BackupThread extends Thread {
    public String toXml() {
        String xml = "";
        xml += "  <monome>\n";
        xml += "    <prefix>" + this.prefix + "</prefix>\n";
        xml += "    <serial>" + this.serial + "</serial>\n";
        xml += "    <sizeX>" + this.sizeX + "</sizeX>\n";
        xml += "    <sizeY>" + this.sizeY + "</sizeY>\n";
        if (this.serialOSCHostname != null) {
            xml += "    <serialOSCHostname>" + this.serialOSCHostname + "</serialOSCHostname>\n";
        }
        String state = "off";
        if (altClear) {
            state = "on";
        }
        xml += "    <altClear>" + state + "</altClear>\n";
        xml += "    <usePageChangeButton>" + (this.usePageChangeButton ? "true" : "false") + "</usePageChangeButton>\n";
        xml += "    <useMIDIPageChanging>" + (this.useMIDIPageChanging ? "true" : "false") + "</useMIDIPageChanging>\n";
        for (int i = 0; i < this.pageChangeMidiInDevices.length; i++) {
            if (pageChangeMidiInDevices[i] == null || pageChangeMidiInDevices[i].compareTo("") == 0) {
                continue;
            }
            xml += "    <selectedpagechangemidiinport>" + StringEscapeUtils.escapeXml(pageChangeMidiInDevices[i]) + "</selectedpagechangemidiinport>\n";
        }
        for (int i = 0; i < this.midiPageChangeRules.size(); i++) {
            MIDIPageChangeRule mpcr = this.midiPageChangeRules.get(i);
            if (mpcr != null) {
                xml += "    <MIDIPageChangeRule>\n";
                xml += "      <pageIndex>" + mpcr.getPageIndex() + "</pageIndex>\n";
                xml += "      <note>" + mpcr.getNote() + "</note>\n";
                xml += "      <channel>" + mpcr.getChannel() + "</channel>\n";
                xml += "      <cc>" + mpcr.getCC() + "</cc>\n";
                xml += "      <ccVal>" + mpcr.getCCVal() + "</ccVal>\n";
                xml += "      <linkedSerial>" + mpcr.getLinkedSerial() + "</linkedSerial>\n";
                xml += "      <linkedPageIndex>" + mpcr.getLinkedPageIndex() + "</linkedPageIndex>\n";
                xml += "    </MIDIPageChangeRule>\n";
            }
        }
        for (int i = 0; i < this.numPages; i++) {
            if (this.pages.get(i).toXml() != null) {
                xml += "    <page class=\"" + this.pages.get(i).getClass().getName() + "\">\n";
                xml += this.pages.get(i).toXml();
                for (int j = 0; j < midiInDevices[i].length; j++) {
                    if (midiInDevices[i][j] == null || midiInDevices[i][j].compareTo("") == 0) {
                        continue;
                    }
                    xml += "      <selectedmidiinport>" + StringEscapeUtils.escapeXml(midiInDevices[i][j]) + "</selectedmidiinport>\n";
                }
                for (int j = 0; j < midiOutDevices[i].length; j++) {
                    if (midiOutDevices[i][j] == null || midiOutDevices[i][j].compareTo("") == 0) {
                        continue;
                    }
                    xml += "      <selectedmidioutport>" + StringEscapeUtils.escapeXml(midiOutDevices[i][j]) + "</selectedmidioutport>\n";
                }
                xml += "      <pageChangeDelay>" + this.pageChangeDelays[i] + "</pageChangeDelay>\n";
                int patternLength = this.patternBanks.get(i).getPatternLength();
                int quantization = this.patternBanks.get(i).getQuantization();
                xml += "      <patternlength>" + patternLength + "</patternlength>\n";
                xml += "      <quantization>" + quantization + "</quantization>\n";
                xml += "    </page>\n";
            }
        }
        xml += "  </monome>\n";
        return xml;
    }
}
