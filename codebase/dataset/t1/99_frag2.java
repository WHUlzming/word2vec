        stream = revert.getStreams().get(1);

        assertTrue(stream.getId() > 0);

        assertEquals(1, stream.getCodecType());

        assertEquals(1, stream.getStreamIndex());

        assertEquals(1, stream.getStreamType());

        assertEquals(1, stream.getCodecId());

        assertEquals("testcodec", stream.getCodecName());

        assertEquals(1, stream.getFrameRateNum());

        assertEquals(20, stream.getFrameRateDen());

        assertEquals(new Long(21), stream.getStartTime());

        assertEquals(new Long(22), stream.getFirstDts());

        assertEquals(new Long(23), stream.getDuration());

        assertEquals(new Long(24), stream.getNumFrames());

        assertEquals(25, stream.getTimeBaseNum());

        assertEquals(26, stream.getTimeBaseDen());

        assertEquals(27, stream.getCodecTimeBaseNum());

        assertEquals(28, stream.getCodecTimeBaseDen());

        assertEquals(29, stream.getTicksPerFrame());

        assertEquals(30, stream.getFrameCount());

        assertEquals(320, stream.getWidth());

        assertEquals(240, stream.getHeight());

        assertEquals(250, stream.getGopSize());

        assertEquals(100, stream.getPixelFormat());

        assertEquals(1024, stream.getBitrate());

        assertEquals(0, stream.getRateEmu());

        assertEquals(44100, stream.getSampleRate());

        assertEquals(2, stream.getChannels());

        assertEquals(5, stream.getSampleFormat());

        assertEquals(8, stream.getBitsPerCodedSample());

        assertEquals(815, stream.getFlags());

        assertEquals("blafasel", stream.getExtraCodecFlags());

        assertEquals(0, stream.getPrivateDataSize());

        assertEquals(10, stream.getExtraDataSize());

        assertEquals(new String(tmp_e), new String(stream.getExtraData()));
