            case 3:

                return _OB_op_connect_any_push_supplier(in, handler);

            case 4:

                return _OB_op_disconnect_push_consumer(in, handler);

            case 5:

                return _OB_op_get_all_filters(in, handler);

            case 6:

                return _OB_op_get_filter(in, handler);

            case 7:

                return _OB_op_get_qos(in, handler);

            case 8:

                return _OB_op_obtain_subscription_types(in, handler);

            case 9:

                return _OB_op_offer_change(in, handler);

            case 10:

                return _OB_op_push(in, handler);

            case 11:

                return _OB_op_remove_all_filters(in, handler);

            case 12:

                return _OB_op_remove_filter(in, handler);

            case 13:

                return _OB_op_set_qos(in, handler);

            case 14:

                return _OB_op_validate_event_qos(in, handler);

            case 15:

                return _OB_op_validate_qos(in, handler);

        }
