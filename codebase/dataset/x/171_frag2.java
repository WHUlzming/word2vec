        float max = Math.max(r, Math.max(g, b));

        float delta = max - min;

        l = (max + min) / 2;

        if (delta == 0) {

            h = 0;

            s = 0;

        } else {

            if (l < 0.5f) s = delta / (max + min); else s = delta / (2 - max - min);
