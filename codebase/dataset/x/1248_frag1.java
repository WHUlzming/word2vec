    public void barre(int fret) {

        for (int i = 0; i < strings.size(); i++) {

            if (i < getStringCount() - getBarreSize()) {

                strings.get(i).hold(0);

            } else {

                strings.get(i).hold(fret);

            }

        }

    }
