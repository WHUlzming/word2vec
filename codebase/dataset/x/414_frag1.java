                try {

                    overwrite.putXX(Tags.forName(key.substring(4)), cfg.getProperty(key));

                } catch (Exception e) {

                    throw new IllegalArgumentException("Illegal entry in dcmsnd.cfg - " + key + "=" + cfg.getProperty(key));
