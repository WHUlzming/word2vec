    public HiPerfInfiniteProgressPanel(boolean i_bUseBackBuffer) {

        m_bUseBackBuffer = i_bUseBackBuffer;

        m_oBars = buildTicker(NUMBER_OF_BARS);

        m_oBarsBounds = new Rectangle();

        for (int i = 0; i < m_oBars.length; i++) {

            m_oBarsBounds = m_oBarsBounds.union(m_oBars[i].getBounds());

        }

        for (int i = 0; i < m_oBars.length; i++) {

            int channel = 224 - 128 / (i + 1);

            m_oColors[i] = new Color(channel, channel, channel);

            m_oColors[NUMBER_OF_BARS + i] = m_oColors[i];

        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        setOpaque(m_bUseBackBuffer);

    }
