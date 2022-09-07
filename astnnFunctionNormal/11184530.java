class BackupThread extends Thread {
    public void writeXML(String indent, BufferedWriter writer) throws IOException {
        writer.write(indent + "<colourControl>");
        writer.newLine();
        writer.write(indent + "  <channel>" + getChannel() + "</channel>");
        writer.newLine();
        writer.write(indent + "  <control>" + getControl() + "</control>");
        writer.newLine();
        writer.write(indent + "  <max>" + getMax() + "</max>");
        writer.newLine();
        writer.write(indent + "  <min>" + getMin() + "</min>");
        writer.newLine();
        writer.write(indent + "  <note>" + getNote() + "</note>");
        writer.newLine();
        writer.write(indent + "  <baseColor>");
        writer.newLine();
        writeColourXML(indent + "    ", writer, baseColor);
        writer.write(indent + "  </baseColor>");
        writer.newLine();
        writer.write(indent + "  <color>");
        writer.newLine();
        writeColourXML(indent + "    ", writer, color);
        writer.write(indent + "  </color>");
        writer.newLine();
        writer.write(indent + "  <device>" + (getDevice() != null ? getDevice().getName() : "") + "</device>");
        writer.newLine();
        writer.write(indent + "  <message>" + getMessage() + "</message>");
        writer.newLine();
        writer.write(indent + "  <scan>" + getScan() + "</scan>");
        writer.newLine();
        writer.write(indent + "  <sweeper>" + (getSweeper() != null ? getSweeper().getName() : "") + "</sweeper>");
        writer.newLine();
        writer.write(indent + "</colourControl>");
        writer.newLine();
    }
}
