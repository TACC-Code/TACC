class BackupThread extends Thread {
    public void updateProfile(final MapPanel mapPanel) {
        mapPanel.showMessage("Profile set as shown");
        List<DataPoint> xSectionProfilePoints = editor.getXSectionProfilePoints();
        if (origin != null) {
            logger.fine("origin is not null");
            DataPoint xp1 = xSectionProfilePoints.get(0);
            DataPoint xp2 = xSectionProfilePoints.get(xSectionProfilePoints.size() - 1);
            double[] ep1 = new double[] { xp1.x, xp1.z };
            double[] ep2 = new double[] { xp2.x, xp2.z };
            List<double[]> currentEndPoints = xsProfile.getEndPoints();
            double[] cep1 = currentEndPoints.get(0);
            double[] cep2 = currentEndPoints.get(1);
            if (LogConfiguration.loggingIsEnabled()) {
                logger.fine("current ep1: " + cep1[0] + "," + cep1[0]);
                logger.fine("current ep2: " + cep2[0] + "," + cep2[0]);
            }
            logger.fine("ep1: " + ep1[0] + "," + ep1[0]);
            logger.fine("ep2: " + ep2[0] + "," + ep2[0]);
            ep1 = GeomUtils.calculateUTMFromPointAtFeetDistanceAlongLine(xp1.x, origin, secondPointForLine);
            logger.fine("in utm coordinates ep1: " + ep1[0] + "," + ep1[0]);
            ep1 = GeomUtils.convertToLatLng(ep1[0], ep1[1]);
            logger.fine("in latlng coordinates ep1: " + ep1[0] + "," + ep1[0]);
            ep2 = GeomUtils.calculateUTMFromPointAtFeetDistanceAlongLine(xp2.x, origin, secondPointForLine);
            logger.fine("in utm coordinates ep2: " + ep2[0] + "," + ep2[0]);
            ep2 = GeomUtils.convertToLatLng(ep2[0], ep2[1]);
            logger.fine("in latlng coordinates ep2: " + ep2[0] + "," + ep2[0]);
            List<double[]> endPoints = new ArrayList<double[]>();
            if (isWithinPrecisionDifference(cep1, ep1)) {
                ep1 = cep1;
            }
            if (isWithinPrecisionDifference(cep2, ep2)) {
                ep2 = cep2;
            }
            endPoints.add(ep1);
            endPoints.add(ep2);
            xsProfile.setEndPoints(endPoints);
            logger.fine("end points set to ep1 and ep2");
        }
        List<double[]> profilePoints = new ArrayList<double[]>();
        double shift = xSectionProfilePoints.get(0).x;
        for (int i = 0; i < xSectionProfilePoints.size(); i++) {
            double[] ppoint = new double[2];
            DataPoint p = xSectionProfilePoints.get(i);
            ppoint[0] = p.x - shift;
            ppoint[1] = p.z;
            profilePoints.add(ppoint);
        }
        xsProfile.setProfilePoints(profilePoints);
        List<XSectionLayer> layers = xsProfile.calculateLayers();
        xsection.setLayers(layers);
        mapPanel.getChannelManager().getXSectionLineClickHandler().updateXSLine();
    }
}
