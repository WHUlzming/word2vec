        final String[] _ob_names = { "_get_MyAdmin", "_get_MyType", "_get_lifetime_filter", "_get_priority_filter", "_set_lifetime_filter", "_set_priority_filter", "add_filter", "connect_sequence_push_consumer", "disconnect_sequence_push_supplier", "get_all_filters", "get_filter", "get_qos", "obtain_offered_types", "remove_all_filters", "remove_filter", "resume_connection", "set_qos", "subscription_change", "suspend_connection", "validate_event_qos", "validate_qos" };

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

                return _OB_att_get_MyAdmin(in, handler);

            case 1:

                return _OB_att_get_MyType(in, handler);

            case 2:

                return _OB_att_get_lifetime_filter(in, handler);

            case 3:

                return _OB_att_get_priority_filter(in, handler);

            case 4:

                return _OB_att_set_lifetime_filter(in, handler);

            case 5:

                return _OB_att_set_priority_filter(in, handler);

            case 6:

                return _OB_op_add_filter(in, handler);

            case 7:

                return _OB_op_connect_sequence_push_consumer(in, handler);

            case 8:
