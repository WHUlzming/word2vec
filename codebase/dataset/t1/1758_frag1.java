        int _ob_left = 0;

        int _ob_right = _ob_names.length;

        int _ob_index = -1;

        while (_ob_left < _ob_right) {

            int _ob_m = (_ob_left + _ob_right) / 2;

            int _ob_res = _ob_names[_ob_m].compareTo(opName);

            if (_ob_res == 0) {

                _ob_index = _ob_m;

                break;

            } else if (_ob_res > 0) _ob_right = _ob_m; else _ob_left = _ob_m + 1;

        }

        switch(_ob_index) {

            case 0:
