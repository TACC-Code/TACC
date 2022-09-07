class BackupThread extends Thread {
    public void colorizeChannels(boolean useMinMaxBoxValue) {
        colorPanel.clear();
        if (!colorizeButton.isDown()) {
            ChannelLineDataManager channelManager = display.getMapPanel().getChannelManager();
            for (Polyline line : channelManager.getPolylines()) {
                line.setStrokeStyle(channelManager.getPolylineStyle());
            }
            return;
        }
        HashMap<String, Double> channelIdToValueMap = getManningsMap();
        if (colorTypeBox.getItemText(colorTypeBox.getSelectedIndex()).equals("Dispersion")) {
            channelIdToValueMap = getDispersionMap();
        }
        String selected = schemeTypeBox.getItemText(schemeTypeBox.getSelectedIndex());
        String[] colors = ColorRangeMapper.SEQUENTIAL_COLORS;
        if (selected.equalsIgnoreCase("SEQUENTIAL")) {
            colors = ColorRangeMapper.SEQUENTIAL_COLORS;
        } else if (selected.equalsIgnoreCase("DIVERGING")) {
            colors = ColorRangeMapper.DIVERGING_COLORS;
        } else if (selected.equalsIgnoreCase("QUALITATIVE")) {
            colors = ColorRangeMapper.QUALITATIVE_COLORS;
        }
        ColorRangeMapper colorMapper = new ColorRangeMapper(colors);
        ChannelLineDataManager channelManager = display.getMapPanel().getChannelManager();
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        if (useMinMaxBoxValue) {
            min = Double.parseDouble(minValueBox.getText());
            max = Double.parseDouble(maxValueBox.getText());
        } else {
            for (String id : channelIdToValueMap.keySet()) {
                Double value = channelIdToValueMap.get(id);
                max = Math.max(max, value);
                min = Math.min(min, value);
            }
            int l = colors.length - 2;
            max = Math.ceil(max * l) / l;
            min = Math.floor(min * l) / l;
            minValueBox.setText(formatter.format(min));
            maxValueBox.setText(formatter.format(max));
        }
        colorPanel.add(new ColorSchemePanel(colors, max, min));
        for (String id : channelIdToValueMap.keySet()) {
            Double value = channelIdToValueMap.get(id);
            String colorSpec = colorMapper.convertValueToColor(value, min, max);
            PolyStyleOptions styleOptions = PolyStyleOptions.newInstance(colorSpec);
            styleOptions.setOpacity(1.0);
            styleOptions.setWeight(3);
            channelManager.getPolyline(id).setStrokeStyle(styleOptions);
        }
    }
}
