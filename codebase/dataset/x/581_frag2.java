    public void write(JLabel status) {

        if (log.isDebugEnabled()) log.debug("write call with Cv number " + _num);

        setToWrite(false);

        _status = status;

        if (status != null) status.setText(java.text.MessageFormat.format(rbt.getString("StateWritingCV"), new Object[] { "" + _num }));

        if (mProgrammer != null) {

            setBusy(true);

            _reading = false;

            _confirm = false;

            try {

                setState(UNKNOWN);

                mProgrammer.writeCV(_num, _value, this);

            } catch (Exception e) {

                setState(UNKNOWN);

                if (status != null) status.setText(java.text.MessageFormat.format(rbt.getString("StateExceptionDuringWrite"), new Object[] { e.toString() }));

                log.warn("Exception during CV write: " + e);

                setBusy(false);

            }

        } else {

            if (status != null) status.setText(rbt.getString("StateNoProgrammer"));

            log.error("No programmer available!");

        }

    }
