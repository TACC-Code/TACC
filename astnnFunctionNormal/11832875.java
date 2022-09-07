class BackupThread extends Thread {
    public String print() {
        StringBuffer sb = new StringBuffer();
        sb.append("<bajjer xmlns='bajjer:prefs'>");
        for (int i = 0; i < favorites.size(); i++) {
            favorite = (JabFavorite) favorites.get(i);
            sb.append("<favorite name='" + favorite.getName() + "'>");
            if (favorite.getChannel() != null) {
                sb.append("<channel>" + favorite.getChannel() + "</channel>");
            }
            if (favorite.getServer() != null) {
                sb.append("<server>" + favorite.getServer() + "</server>");
            }
            if (favorite.getNickname() != null) {
                sb.append("<nickname>" + favorite.getNickname() + "</nickname>");
            }
            sb.append("</favorite>");
        }
        sb.append("</bajjer>");
        return sb.toString();
    }
}
