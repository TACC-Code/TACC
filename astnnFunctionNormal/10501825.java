class BackupThread extends Thread {
    public String formatHTML(Packet packet, int id, long time) {
        StringBuffer out = new StringBuffer();
        String classes = "";
        classes += "channel_" + packet.getHeader().getChannelId() + " ";
        classes += "datatype_" + packet.getHeader().getDataType() + " ";
        out.append("<div id=\"packet_").append(id).append("\" class=").append(classes).append("\">\n");
        out.append("<pre>\n");
        out.append(packet.getHeader().toString()).append("\n");
        out.append(packet.getMessage().toString()).append("\n");
        out.append("<pre>\n");
        out.append("</div>\n\n");
        return out.toString();
    }
}
