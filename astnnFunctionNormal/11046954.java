class BackupThread extends Thread {
    private int findLocation() {
        int level = -1;
        GeoCoord center = loc.getCenter();
        if (center == null) {
            String locName = loc.getName();
            String urlString = googleUrlStr + locName.replaceAll("\\s+", "+") + "&key=" + key;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlString).openStream()));
                String line = null;
                if ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    int status = Integer.parseInt(values[0]);
                    if (status == 200) {
                        level = Integer.parseInt(values[1]);
                        lat = Double.parseDouble(values[2]);
                        lon = Double.parseDouble(values[3]);
                        double latRads = lat * Math.PI / 180;
                        loc.setCenter(new GeoCoord(lat, lon));
                        double yTmp = Math.log(Math.tan(Math.PI / 4 + 0.5 * Math.abs(latRads)));
                        projLat = yTmp * 180 / Math.PI;
                        level = 10 - level + (int) (Math.abs(lat / 30));
                        double radAtLat = Math.cos(latRads) * 2 * Math.PI;
                        int gridSize = 131072 >> level;
                        loc.setRadius(radAtLat / gridSize);
                        locValid = true;
                    }
                }
                reader.close();
            } catch (IOException exc) {
                throw new RuntimeException("Cannot parse stream from " + urlString, exc);
            }
        } else {
            lat = center.getLatitude();
            lon = center.getLongitude();
            double yTmp = Math.log(Math.tan(Math.PI / 4 + 0.5 * Math.abs(center.getLatRadians())));
            projLat = yTmp * 180 / Math.PI;
            double radius = loc.getRadius();
            if (radius == 0) {
                loc.setRadius(1000);
            }
            double radAtLat = Math.cos(center.getLatRadians()) * 2 * Math.PI;
            double cells = radAtLat / radius;
            double log2 = Math.log(cells) / Math.log(2.0);
            level = 17 - (int) log2;
        }
        return level;
    }
}
