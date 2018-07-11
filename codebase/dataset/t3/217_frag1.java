        if (p_tag == TAG_EmailThreadID) {

            _EmailThreadID = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_EmailType) {

            _EmailType = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_OrigTime) {

            _OrigTime = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_Subject) {

            _Subject = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_EncodedSubjectLen) {

            _EncodedSubjectLen = new String(v_value);

            return DATA_TYPE;

        }

        if (p_tag == TAG_EncodedSubject) {

            _EncodedSubject = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_NoRelatedSym) {

            _NoRelatedSym = new String(v_value);

            return START_GROUP;

        }

        if (p_tag == TAG_OrderID) {

            _OrderID = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_ClOrdID) {

            _ClOrdID = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_LinesOfText) {

            _LinesOfText = new String(v_value);

            return START_GROUP;

        }

        if (p_tag == TAG_RawDataLength) {

            _RawDataLength = new String(v_value);

            return DATA_TYPE;

        }
