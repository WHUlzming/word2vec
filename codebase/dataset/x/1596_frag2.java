        if (idx != -1) {

            String prefix = name.substring(0, idx);

            try {

                Integer.parseInt(prefix);

                isParseable = true;

            } catch (Throwable t) {

                isParseable = false;

            }

            if (isParseable) {

                name = name.substring(idx + 1);
