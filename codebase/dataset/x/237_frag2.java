    public synchronized TypeObject isBomb(Point p) {

        synchronized (_bombs) {

            Iterator i = _bombs.iterator();

            while (i.hasNext()) {

                TypeObject bomb = (TypeObject) i.next();

                if (p.equals(bomb.getPosition())) return bomb;

            }

            return null;

        }

    }
