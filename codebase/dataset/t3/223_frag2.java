            } else {

                if (old != null) {

                    throw new Exception(service.getClass().getName() + " with path '" + path + "' and index [" + service.index() + "] is conflicting with " + old.getClass().getName() + " for the same path and index.");

                }
