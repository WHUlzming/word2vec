            final ICacheEntry<Long, byte[]> entry = cache.allocateEntryForClock(key, lock);

            if (volatilesync) {

                if (entry.volatileGetValue() == null) {

                    synchronized (entry) {

                        if (entry.getValue() == null) {

                            byte[] b = emurateReadInPage(pager, capacity, key);

                            entry.setValue(b);

                            miss++;

                        }

                    }

                }

            } else {

                if (entry.getValue() == null) {
