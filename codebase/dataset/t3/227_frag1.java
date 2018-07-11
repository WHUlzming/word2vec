    public int compareTo(Object comTo) {

        if (comTo == null) return -1;

        long comStart = ((ScheduleItem) comTo).getStart().getTime();

        long thisStart = getStart().getTime();

        int result = 0;

        if (comStart > thisStart) result = -1; else if (comStart < thisStart) result = +1;

        return result;

    }
