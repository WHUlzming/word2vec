        public void stateChanged(ChangeEvent e) {

            JSlider slider = (JSlider) e.getSource();

            int value = slider.getValue();

            if (slider.equals(seekSlider)) {

                if (currentSound instanceof Clip) {

                    ((Clip) currentSound).setFramePosition(value);

                } else if (currentSound instanceof Sequence) {

                    long dur = ((Sequence) currentSound).getMicrosecondLength();

                    sequencer.setMicrosecondPosition(value * 1000);

                } else if (currentSound instanceof BufferedInputStream) {

                    long dur = sequencer.getMicrosecondLength();

                    sequencer.setMicrosecondPosition(value * 1000);

                }

                playbackMonitor.repaint();

                return;

            }

            TitledBorder tb = (TitledBorder) slider.getBorder();

            String s = tb.getTitle();

            if (s.startsWith("Pan")) {

                s = s.substring(0, s.indexOf('=') + 1) + s.valueOf(value / 100.0);

                if (currentSound != null) {

                    setPan();

                }

            } else if (s.startsWith("Gain")) {

                s = s.substring(0, s.indexOf('=') + 1) + s.valueOf(value);

                if (currentSound != null) {

                    setGain();

                }

            }

            tb.setTitle(s);

            slider.repaint();

        }
