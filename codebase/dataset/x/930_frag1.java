    protected region addPingSupport(int x, int y, int rtt, int metric) {

        if (ping_count == pings.length) {

            int to_discard = pings.length / 10;

            if (to_discard < 3) {

                to_discard = 3;

            }

            ping_count = pings.length - to_discard;

            System.arraycopy(pings, to_discard, pings, 0, ping_count);

            for (int i = 0; i < to_discard; i++) {

                regions.removeFirst();

            }

        }

        pingValue ping = new pingValue(x, y, metric);

        pings[ping_count++] = ping;

        region new_region = null;

        if (prev_ping != null) {

            new_region = new region(prev_ping, ping);

            regions.add(new_region);

        }

        prev_ping = ping;

        return (new_region);

    }
