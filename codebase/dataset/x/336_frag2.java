            case 0:

                return _OB_op_define_properties(in, handler);

            case 1:

                return _OB_op_define_property(in, handler);

            case 2:

                return _OB_op_delete_all_properties(in, handler);

            case 3:

                return _OB_op_delete_properties(in, handler);

            case 4:

                return _OB_op_delete_property(in, handler);

            case 5:

                return _OB_op_get_all_properties(in, handler);

            case 6:

                return _OB_op_get_all_property_names(in, handler);

            case 7:

                return _OB_op_get_number_of_properties(in, handler);

            case 8:

                return _OB_op_get_properties(in, handler);

            case 9:

                return _OB_op_get_property_value(in, handler);

            case 10:

                return _OB_op_is_property_defined(in, handler);

        }
