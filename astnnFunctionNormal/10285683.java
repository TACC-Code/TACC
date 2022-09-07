class BackupThread extends Thread {
    public static boolean loadGeodataFile(byte rx, byte ry) {
        if (rx < Config.WORLD_X_MIN || rx > Config.WORLD_X_MAX || ry < Config.WORLD_Y_MIN || ry > Config.WORLD_Y_MAX) {
            _log.warning("Failed to Load GeoFile: invalid region " + rx + "," + ry + "\n");
            return false;
        }
        String fname = "./data/geodata/" + rx + "_" + ry + ".l2j";
        short regionoffset = (short) ((rx << 5) + ry);
        _log.info("Geo Engine: - Loading: " + fname + " -> region offset: " + regionoffset + "X: " + rx + " Y: " + ry);
        File Geo = new File(fname);
        int size, index = 0, block = 0, flor = 0;
        FileChannel roChannel = null;
        try {
            roChannel = new RandomAccessFile(Geo, "r").getChannel();
            size = (int) roChannel.size();
            MappedByteBuffer geo;
            if (Config.FORCE_GEODATA) geo = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size).load(); else geo = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            geo.order(ByteOrder.LITTLE_ENDIAN);
            if (size > 196608) {
                IntBuffer indexs = IntBuffer.allocate(65536);
                while (block < 65536) {
                    byte type = geo.get(index);
                    indexs.put(block, index);
                    block++;
                    index++;
                    if (type == 0) index += 2; else if (type == 1) index += 128; else {
                        int b;
                        for (b = 0; b < 64; b++) {
                            byte layers = geo.get(index);
                            index += (layers << 1) + 1;
                            if (layers > flor) flor = layers;
                        }
                    }
                }
                _geodataIndex.put(regionoffset, indexs);
            }
            _geodata.put(regionoffset, geo);
            _log.info("Geo Engine: - Max Layers: " + flor + " Size: " + size + " Loaded: " + index);
        } catch (Exception e) {
            e.printStackTrace();
            _log.warning("Failed to Load GeoFile at block: " + block + "\n");
            return false;
        } finally {
            try {
                roChannel.close();
            } catch (Exception e) {
            }
        }
        return true;
    }
}
