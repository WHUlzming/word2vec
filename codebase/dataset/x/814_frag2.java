            int size = ed.getSize();

            if (size != 0) {

                boolean needed = true;

                for (int i = 0; i < count; i++) if (testsPerformed[i][size]) needed = false;

                if (needed) emitVerify(0, size, level);
