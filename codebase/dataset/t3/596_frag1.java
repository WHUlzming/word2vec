    protected void buildEditor(Patch patch) {

        algoIcon[0] = new ImageIcon(getClass().getResource("images/algo01.gif"));

        algoIcon[1] = new ImageIcon(getClass().getResource("images/algo02.gif"));

        algoIcon[2] = new ImageIcon(getClass().getResource("images/algo03.gif"));

        algoIcon[3] = new ImageIcon(getClass().getResource("images/algo04.gif"));

        algoIcon[4] = new ImageIcon(getClass().getResource("images/algo05.gif"));

        algoIcon[5] = new ImageIcon(getClass().getResource("images/algo06.gif"));

        algoIcon[6] = new ImageIcon(getClass().getResource("images/algo07.gif"));

        algoIcon[7] = new ImageIcon(getClass().getResource("images/algo08.gif"));

        algoIcon[8] = new ImageIcon(getClass().getResource("images/algo09.gif"));

        algoIcon[9] = new ImageIcon(getClass().getResource("images/algo10.gif"));

        algoIcon[10] = new ImageIcon(getClass().getResource("images/algo11.gif"));

        algoIcon[11] = new ImageIcon(getClass().getResource("images/algo12.gif"));

        algoIcon[12] = new ImageIcon(getClass().getResource("images/algo13.gif"));

        algoIcon[13] = new ImageIcon(getClass().getResource("images/algo14.gif"));

        algoIcon[14] = new ImageIcon(getClass().getResource("images/algo15.gif"));

        algoIcon[15] = new ImageIcon(getClass().getResource("images/algo16.gif"));

        algoIcon[16] = new ImageIcon(getClass().getResource("images/algo17.gif"));

        algoIcon[17] = new ImageIcon(getClass().getResource("images/algo18.gif"));

        algoIcon[18] = new ImageIcon(getClass().getResource("images/algo19.gif"));

        algoIcon[19] = new ImageIcon(getClass().getResource("images/algo20.gif"));

        algoIcon[20] = new ImageIcon(getClass().getResource("images/algo21.gif"));

        algoIcon[21] = new ImageIcon(getClass().getResource("images/algo22.gif"));

        algoIcon[22] = new ImageIcon(getClass().getResource("images/algo23.gif"));

        algoIcon[23] = new ImageIcon(getClass().getResource("images/algo24.gif"));

        algoIcon[24] = new ImageIcon(getClass().getResource("images/algo25.gif"));

        algoIcon[25] = new ImageIcon(getClass().getResource("images/algo26.gif"));

        algoIcon[26] = new ImageIcon(getClass().getResource("images/algo27.gif"));

        algoIcon[27] = new ImageIcon(getClass().getResource("images/algo28.gif"));

        algoIcon[28] = new ImageIcon(getClass().getResource("images/algo29.gif"));

        algoIcon[29] = new ImageIcon(getClass().getResource("images/algo30.gif"));

        algoIcon[30] = new ImageIcon(getClass().getResource("images/algo31.gif"));

        algoIcon[31] = new ImageIcon(getClass().getResource("images/algo32.gif"));

        final JLabel l = new JLabel(algoIcon[patch.sysex[6 + 134]]);

        JPanel cmnPane = new JPanel();

        cmnPane.setLayout(new GridBagLayout());

        gbc.weightx = 0;

        final ScrollBarWidget algo = new ScrollBarWidget("Algorithm", patch, 0, 31, 1, new ParamModel(patch, 6 + 134), new VoiceSender(134));

        addWidget(cmnPane, algo, 1, 2, 6, 1, 17);

        algo.addEventListener(new ChangeListener() {



            public void stateChanged(ChangeEvent e) {

                l.setIcon(algoIcon[algo.getValue()]);

            }

        });

        addWidget(cmnPane, new ScrollBarWidget("Feedback", patch, 0, 7, 0, new ParamModel(patch, 6 + 135), new VoiceSender(135)), 1, 3, 6, 1, 18);

        addWidget(cmnPane, new ScrollBarLookupWidget("Key Transpose", patch, 0, 48, new ParamModel(patch, 6 + 144), new VoiceSender(144), KeyTransposeName), 1, 1, 6, 1, 19);

        addWidget(cmnPane, new PatchNameWidget("Name (10 Char.)", patch), 1, 0, 6, 1, 0);

        gbc.gridx = 0;

        gbc.gridy = 4;

        gbc.gridwidth = 1;

        gbc.gridheight = 1;

        cmnPane.add(new JLabel(" "), gbc);

        gbc.gridx = 0;

        gbc.gridy = 5;

        gbc.gridwidth = 1;

        gbc.gridheight = 1;

        cmnPane.add(new JLabel(" "), gbc);

        gbc.gridx = 0;

        gbc.gridy = 6;

        gbc.gridwidth = 1;

        gbc.gridheight = 1;

        cmnPane.add(new JLabel(" "), gbc);

        gbc.gridx = 0;

        gbc.gridy = 7;

        gbc.gridwidth = 1;

        gbc.gridheight = 1;

        cmnPane.add(new JLabel(" "), gbc);

        gbc.gridx = 1;

        gbc.gridy = 5;

        gbc.gridwidth = 6;

        gbc.gridheight = 1;

        cmnPane.add(new JLabel("Operator on/off ( not stored as voice-parameter )"), gbc);

        Op0State = new JCheckBox("1  ", (OperatorState & 32) == 32);

        Op1State = new JCheckBox("2  ", (OperatorState & 16) == 16);

        Op2State = new JCheckBox("3  ", (OperatorState & 8) == 8);

        Op3State = new JCheckBox("4  ", (OperatorState & 4) == 4);

        Op4State = new JCheckBox("5  ", (OperatorState & 2) == 2);

        Op5State = new JCheckBox("6  ", (OperatorState & 1) == 1);

        Op0State.addItemListener(this);

        Op1State.addItemListener(this);

        Op2State.addItemListener(this);

        Op3State.addItemListener(this);

        Op4State.addItemListener(this);

        Op5State.addItemListener(this);

        gbc.gridx = 1;

        gbc.gridy = 6;

        gbc.gridwidth = 1;

        gbc.gridheight = 2;

        cmnPane.add(Op0State, gbc);

        gbc.gridx = 2;

        gbc.gridy = 6;

        gbc.gridwidth = 1;

        gbc.gridheight = 2;

        cmnPane.add(Op1State, gbc);

        gbc.gridx = 3;

        gbc.gridy = 6;

        gbc.gridwidth = 1;

        gbc.gridheight = 2;

        cmnPane.add(Op2State, gbc);

        gbc.gridx = 4;

        gbc.gridy = 6;

        gbc.gridwidth = 1;

        gbc.gridheight = 2;

        cmnPane.add(Op3State, gbc);

        gbc.gridx = 5;

        gbc.gridy = 6;

        gbc.gridwidth = 1;

        gbc.gridheight = 2;

        cmnPane.add(Op4State, gbc);

        gbc.gridx = 6;

        gbc.gridy = 6;

        gbc.gridwidth = 1;

        gbc.gridheight = 2;

        cmnPane.add(Op5State, gbc);

        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.gridwidth = 1;

        gbc.gridheight = 6;

        cmnPane.add(l, gbc);

        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.gridwidth = 3;

        gbc.gridheight = 8;

        cmnPane.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Common", TitledBorder.CENTER, TitledBorder.CENTER));

        scrollPane.add(cmnPane, gbc);

        JPanel lfoPane = new JPanel();

        lfoPane.setLayout(new GridBagLayout());

        gbc.weightx = 0;

        addWidget(lfoPane, new ScrollBarWidget("Speed", patch, 0, 99, 0, new ParamModel(patch, 6 + 137), new VoiceSender(137)), 0, 0, 6, 1, 20);

        addWidget(lfoPane, new ScrollBarWidget("Delay", patch, 0, 99, 0, new ParamModel(patch, 6 + 138), new VoiceSender(138)), 0, 1, 6, 1, 21);

        addWidget(lfoPane, new ScrollBarWidget("PMD", patch, 0, 99, 0, new ParamModel(patch, 6 + 139), new VoiceSender(139)), 0, 2, 6, 1, 22);

        addWidget(lfoPane, new ScrollBarWidget("AMD", patch, 0, 99, 0, new ParamModel(patch, 6 + 140), new VoiceSender(140)), 0, 3, 6, 1, 24);

        gbc.gridx = 0;

        gbc.gridy = 4;

        gbc.gridwidth = 6;

        gbc.gridheight = 1;

        lfoPane.add(new JLabel(" "), gbc);

        gbc.gridx = 0;

        gbc.gridy = 5;

        gbc.gridwidth = 6;

        gbc.gridheight = 1;

        lfoPane.add(new JLabel(" "), gbc);

        addWidget(lfoPane, new ComboBoxWidget("Wave", patch, new ParamModel(patch, 6 + 142), new VoiceSender(142), LfoWaveName), 1, 5, 1, 3, 26);

        addWidget(lfoPane, new ComboBoxWidget("Sync", patch, new ParamModel(patch, 6 + 141), new VoiceSender(141), OnOffName), 2, 5, 1, 3, 27);

        gbc.gridx = 0;

        gbc.gridy = 6;

        gbc.gridwidth = 6;

        gbc.gridheight = 1;

        lfoPane.add(new JLabel(" "), gbc);

        gbc.gridx = 0;

        gbc.gridy = 7;

        gbc.gridwidth = 6;

        gbc.gridheight = 1;

        lfoPane.add(new JLabel(" "), gbc);

        gbc.gridx = 4;

        gbc.gridy = 0;

        gbc.gridwidth = 3;

        gbc.gridheight = 8;

        lfoPane.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "LFO", TitledBorder.CENTER, TitledBorder.CENTER));

        scrollPane.add(lfoPane, gbc);

        JTabbedPane oscPane = new JTabbedPane();

        final DecimalFormat freqFormatter = new DecimalFormat("###0.000");

        int i = 5;

        int j = 1;

        while (i != 6) {

            JPanel panel = new JPanel();

            panel.setLayout(new GridBagLayout());

            oscPane.addTab("OP" + (j), panel);

            gbc.weightx = 1;

            final ComboBoxWidget FreqMode = new ComboBoxWidget("Mode", patch, new ParamModel(patch, 6 + (i * 21) + 17), new VoiceSender((i * 21) + 17), FreqModeName);

            final ComboBoxWidget OscSync = new ComboBoxWidget("Sync (*)", patch, new ParamModel(patch, 6 + 136), new VoiceSender(136), OnOffName);

            final ScrollBarWidget FreqCoarse = new ScrollBarWidget("Frequency Coarse", patch, 0, 31, 0, new ParamModel(patch, 6 + (i * 21) + 18), new VoiceSender((i * 21) + 18));

            final ScrollBarWidget FreqFine = new ScrollBarWidget("Frequency Fine", patch, 0, 99, 0, new ParamModel(patch, 6 + (i * 21) + 19), new VoiceSender((i * 21) + 19));

            final JLabel OscFreqLabel = new JLabel("Frequency");

            addWidget(panel, FreqMode, 0, 1, 3, 2, 1);

            FreqMode.addEventListener(new ActionListener() {



                public void actionPerformed(ActionEvent e) {

                    OscFreqLabel.setText("Oscillator Frequency - " + FreqModeName[FreqMode.getValue()] + ": " + freqFormatter.format((Float.valueOf(FreqCoarseName[FreqMode.getValue()][FreqCoarse.getValue()]).floatValue()) * (Float.valueOf(FreqFineName[FreqMode.getValue()][FreqFine.getValue()]).floatValue())));

                }

            });

            OscSync.addEventListener(new ActionListener() {



                public void actionPerformed(ActionEvent e) {

                    int OscSyncValue = OscSync.getValue();

                    SysexWidget w;

                    for (int i = 0; i < widgetList.size(); i++) {

                        w = ((SysexWidget) widgetList.get(i));

                        if ((w instanceof ComboBoxWidget) && (((ComboBoxWidget) w).getLabel().equals("Sync (*)"))) {

                            ((ComboBoxWidget) w).setValue(OscSyncValue);

                        }

                    }

                }

            });

            FreqCoarse.addEventListener(new ChangeListener() {



                public void stateChanged(ChangeEvent e) {

                    OscFreqLabel.setText("Oscillator Frequency - " + FreqModeName[FreqMode.getValue()] + ": " + freqFormatter.format((Float.valueOf(FreqCoarseName[FreqMode.getValue()][FreqCoarse.getValue()]).floatValue()) * (Float.valueOf(FreqFineName[FreqMode.getValue()][FreqFine.getValue()]).floatValue())));

                }

            });

            FreqFine.addEventListener(new ChangeListener() {



                public void stateChanged(ChangeEvent e) {

                    OscFreqLabel.setText("Oscillator Frequency - " + FreqModeName[FreqMode.getValue()] + ": " + freqFormatter.format((Float.valueOf(FreqCoarseName[FreqMode.getValue()][FreqCoarse.getValue()]).floatValue()) * (Float.valueOf(FreqFineName[FreqMode.getValue()][FreqFine.getValue()]).floatValue())));

                }

            });

            addWidget(panel, OscSync, 3, 1, 3, 2, 28);

            addWidget(panel, FreqCoarse, 0, 3, 6, 1, 2);

            addWidget(panel, FreqFine, 0, 4, 6, 1, 3);

            gbc.gridx = 0;

            gbc.gridy = 0;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            OscFreqLabel.setText("Oscillator Frequency - " + FreqModeName[FreqMode.getValue()] + ": " + freqFormatter.format(((Float.valueOf(FreqCoarseName[FreqMode.getValue()][FreqCoarse.getValue()]).floatValue()) * (Float.valueOf(FreqFineName[FreqMode.getValue()][FreqFine.getValue()]).floatValue()))));

            panel.add(OscFreqLabel, gbc);

            addWidget(panel, new ScrollBarWidget("Detune", patch, 0, 14, -7, new ParamModel(patch, 6 + (i * 21) + 20), new VoiceSender((i * 21) + 20)), 0, 6, 6, 1, 4);

            gbc.gridx = 0;

            gbc.gridy = 7;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel(" "), gbc);

            gbc.gridx = 0;

            gbc.gridy = 8;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel("Keyboard Level Scaling"), gbc);

            addWidget(panel, new ScrollBarLookupWidget("Breakpoint", patch, 0, 99, new ParamModel(patch, 6 + (i * 21) + 8), new VoiceSender((i * 21) + 8), KbdBreakPointName), 0, 9, 6, 1, 5);

            gbc.gridx = 0;

            gbc.gridy = 10;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel(" "), gbc);

            addWidget(panel, new ComboBoxWidget("Curve Left", patch, new ParamModel(patch, 6 + (i * 21) + 11), new VoiceSender((i * 21) + 11), KbdCurveName), 0, 11, 3, 2, 6);

            addWidget(panel, new ComboBoxWidget("Curve Right", patch, new ParamModel(patch, 6 + (i * 21) + 12), new VoiceSender((i * 21) + 12), KbdCurveName), 3, 11, 3, 2, 8);

            addWidget(panel, new ScrollBarWidget("Depth Left", patch, 0, 99, 0, new ParamModel(patch, 6 + (i * 21) + 9), new VoiceSender((i * 21) + 9)), 0, 13, 6, 1, 17);

            addWidget(panel, new ScrollBarWidget("Depth Right", patch, 0, 99, 0, new ParamModel(patch, 6 + (i * 21) + 10), new VoiceSender((i * 21) + 10)), 0, 14, 6, 1, 9);

            gbc.gridx = 0;

            gbc.gridy = 15;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel(" "), gbc);

            addWidget(panel, new ScrollBarWidget("Keyboard Rate Scaling", patch, 0, 7, 0, new ParamModel(patch, 6 + (i * 21) + 13), new VoiceSender((i * 21) + 13)), 0, 16, 6, 1, 10);

            gbc.gridx = 0;

            gbc.gridy = 16;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel(" "), gbc);

            gbc.gridx = 0;

            gbc.gridy = 17;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel("(*) one single parameter for six operators"), gbc);

            gbc.gridx = 6;

            gbc.gridy = 0;

            gbc.gridwidth = 1;

            gbc.gridheight = 1;

            panel.add(new JLabel("        "), gbc);

            gbc.gridx = 7;

            gbc.gridy = 0;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel("Operator"), gbc);

            addWidget(panel, new ScrollBarWidget("Output Level", patch, 0, 99, 0, new ParamModel(patch, 6 + (i * 21) + 16), new VoiceSender((i * 21) + 16)), 7, 1, 6, 1, 11);

            addWidget(panel, new ScrollBarWidget("Velocity Sensitivity", patch, 0, 7, 0, new ParamModel(patch, 6 + (i * 21) + 15), new VoiceSender((i * 21) + 15)), 7, 2, 6, 1, 12);

            gbc.gridx = 7;

            gbc.gridy = 4;

            gbc.gridwidth = 6;

            gbc.gridheight = 1;

            panel.add(new JLabel("Modulation Sensitivity"), gbc);

            addWidget(panel, new ScrollBarWidget("Amplitude", patch, 0, 3, 0, new ParamModel(patch, 6 + (i * 21) + 14), new VoiceSender((i * 21) + 14)), 7, 5, 6, 1, 13);

            final ScrollBarWidget PitchModSens = new ScrollBarWidget("Pitch (*)", patch, 0, 7, 0, new ParamModel(patch, 6 + 143), new VoiceSender(143));

            addWidget(panel, PitchModSens, 7, 6, 6, 1, 23);

            PitchModSens.addEventListener(new ChangeListener() {



                public void stateChanged(ChangeEvent e) {

                    int pmsValue = PitchModSens.getValue();

                    SysexWidget w;

                    for (int i = 0; i < widgetList.size(); i++) {

                        w = ((SysexWidget) widgetList.get(i));

                        if ((w instanceof ScrollBarWidget) && (((ScrollBarWidget) w).getLabel().equals("Pitch (*)"))) {

                            ((ScrollBarWidget) w).setValue(pmsValue);

                        }

                    }

                }

            });

            addWidget(panel, new EnvelopeWidget("Envelope Generator", patch, new EnvelopeWidget.Node[] { new EnvelopeWidget.Node(0, 0, null, 0, 99, new ParamModel(patch, 6 + (i * 21) + 7), 0, true, null, new VoiceSender((i * 21) + 7), null, "L4"), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + (i * 21) + 0), 0, 99, new ParamModel(patch, 6 + (i * 21) + 4), 0, true, new VoiceSender((i * 21) + 0), new VoiceSender((i * 21) + 4), "R1", "L1"), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + (i * 21) + 1), 0, 99, new ParamModel(patch, 6 + (i * 21) + 5), 0, true, new VoiceSender((i * 21) + 1), new VoiceSender((i * 21) + 5), "R2", "L2"), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + (i * 21) + 2), 0, 99, new ParamModel(patch, 6 + (i * 21) + 6), 0, true, new VoiceSender((i * 21) + 2), new VoiceSender((i * 21) + 6), "R3", "L3"), new EnvelopeWidget.Node(100, 100, null, EnvelopeWidget.Node.SAME, 99, null, 0, true, null, null, null, null), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + (i * 21) + 3), 0, 99, new ParamModel(patch, 6 + (i * 21) + 7), 0, true, new VoiceSender((i * 21) + 3), new VoiceSender((i * 21) + 7), "R4", "L4") }), 7, 8, 6, 9, 14);

            if (i == 5) {

                i = 4;

            } else {

                if (i == 4) {

                    i = 3;

                } else {

                    if (i == 3) {

                        i = 2;

                    } else {

                        if (i == 2) {

                            i = 1;

                        } else {

                            if (i == 1) {

                                i = 0;

                            } else {

                                if (i == 0) {

                                    i = 6;

                                }

                            }

                        }

                    }

                }

            }

            j++;

        }

        JPanel pegPane = new JPanel();

        pegPane.setLayout(new GridBagLayout());

        oscPane.addTab("PEG", pegPane);

        gbc.fill = GridBagConstraints.BOTH;

        addWidget(pegPane, new EnvelopeWidget("Pitch Envelope Generator", patch, new EnvelopeWidget.Node[] { new EnvelopeWidget.Node(0, 0, null, 0, 99, new ParamModel(patch, 6 + 133), 0, true, null, new VoiceSender(133), null, "Pitch 4"), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + 126), 0, 99, new ParamModel(patch, 6 + 130), 0, true, new VoiceSender(126), new VoiceSender(130), "Rate 1", "Pitch 1"), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + 127), 0, 99, new ParamModel(patch, 6 + 131), 0, true, new VoiceSender(127), new VoiceSender(131), "Rate 2", "Pitch 2"), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + 128), 0, 99, new ParamModel(patch, 6 + 132), 0, true, new VoiceSender(128), new VoiceSender(132), "Rate 3", "Pitch 3"), new EnvelopeWidget.Node(100, 100, null, EnvelopeWidget.Node.SAME, 99, null, 0, true, null, null, null, null), new EnvelopeWidget.Node(0, 99, new ParamModel(patch, 6 + 129), 0, 99, new ParamModel(patch, 6 + 133), 0, true, new VoiceSender(129), new VoiceSender(133), "Rate 4", "Pitch 4") }), 0, 0, 1, 9, 1);

        gbc.gridx = 0;

        gbc.gridy = 18;

        gbc.gridwidth = 1;

        gbc.gridheight = 1;

        pegPane.add(new JLabel("                                                                                            ", JLabel.LEFT), gbc);

        pegPane.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "PEG", TitledBorder.CENTER, TitledBorder.CENTER));

        gbc.gridx = 0;

        gbc.gridy = 10;

        gbc.gridwidth = 7;

        gbc.gridheight = 18;

        scrollPane.add(oscPane, gbc);

        pack();

    }
