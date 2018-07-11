    public static String convertFrameID23To22(String identifier) {

        if (identifier.length() < 4) {

            return null;

        }

        if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier)) {

            return ID3Frames.convertv23Tov22.get(identifier.substring(0, 4));

        }

        return null;

    }
