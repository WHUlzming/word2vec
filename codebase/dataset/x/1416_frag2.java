    public void orderBeats() {

        Iterator it = getSong().getTracks();

        while (it.hasNext()) {

            TGTrack track = (TGTrack) it.next();

            getTrackManager().orderBeats(track);

        }

    }
