            } else if ("polygon".equals(localName)) {

                GeneralPath subPath = new GeneralPath();

                String pointsDef = attributes.getValue("", "points");

                if (pointsDef != null) {

                    String[] ps = pointsDef.split(" ");

                    for (int i = 0; i < ps.length; i++) {
