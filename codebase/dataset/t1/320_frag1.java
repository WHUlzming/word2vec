        for (int nth = 0, i = 0; !_stop; nth = (nth >= limit) ? 0 : nth + 1, i++) {

            final long key;

            if (scanning || scanLength != 0 && (i == scanLength && rand.nextDouble() < percent)) {

                if (++scanCount <= scanLength) {

                    if (!scanning) {

                        base = dist[nth];

                    }

                    key = base + scanCount;

                    scanning = true;

                    scanned++;

                } else {

                    key = dist[nth];

                    scanCount = 0;

                    scanning = false;

                    i = 0;

                    zipf++;

                }

            } else {

                key = dist[nth];

                zipf++;

            }

            final ICacheEntry<Long, byte[]> entry;
