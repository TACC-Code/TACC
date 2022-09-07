class BackupThread extends Thread {
    private void saveSpotLight() throws IOException {
        for (int i = 0; i < lightManager.getSpotLightVector().size(); i++) {
            WilmaLight w = (WilmaLight) lightManager.getSpotLightVector().get(i);
            SpotLight l = (SpotLight) w.getLight();
            l.getColor(colour);
            out.write("Light" + i + "SpotColourR=" + colour.x + "\r\n");
            out.write("Light" + i + "SpotColourG=" + colour.y + "\r\n");
            out.write("Light" + i + "SpotColourB=" + colour.z + "\r\n");
            l.getPosition(position);
            out.write("Light" + i + "SpotPositionX=" + position.x + "\r\n");
            out.write("Light" + i + "SpotPositionY=" + position.y + "\r\n");
            out.write("Light" + i + "SpotPositionZ=" + position.z + "\r\n");
            l.getDirection(direction);
            out.write("Light" + i + "SpotDirectionVectorX=" + direction.x + "\r\n");
            out.write("Light" + i + "SpotDirectionVectorY=" + direction.y + "\r\n");
            out.write("Light" + i + "SpotDirectionVectorZ=" + direction.z + "\r\n");
            l.getAttenuation(attenuation);
            out.write("Light" + i + "SpotAttenuationConstant=" + attenuation.x + "\r\n");
            out.write("Light" + i + "SpotAttenuationLinear=" + attenuation.y + "\r\n");
            out.write("Light" + i + "SpotAttenuationQuadratic=" + attenuation.z + "\r\n");
            out.write("Light" + i + "SpotSpreadAngle=" + l.getSpreadAngle() + "\r\n");
            out.write("Light" + i + "SpotConcentration=" + l.getConcentration() + "\r\n");
            if (l.getEnable() == true) out.write("Light" + i + "SpotEnable=" + "true" + "\r\n"); else out.write("Light" + i + "SpotEnable=" + "false" + "\r\n");
        }
    }
}
