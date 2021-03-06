    public void paint(Graphics g) {

        int left = 0;

        int right = kAppWidth - 1;

        int top = 0;

        int bottom = kAppHeight - 1;

        int tempLeft = 0;

        int tempRight = 0;

        int tempTop = 0;

        int viewWidth;

        int viewHeight;

        int buttonWidth = 0;

        int buttonHeight = 0;

        int buttonLeft = 0;

        int buttonTop = 0;

        int tileLeft;

        int tileTop;

        g.setColor(getForeground());

        Font f = new java.awt.Font("Helvetica", 0, 12);

        g.setFont(f);

        Font numFont = new java.awt.Font("Helvetica", 0, 9);

        Font answerFont = new java.awt.Font("Helvetica", Font.BOLD, 16);

        Font questionFont = new java.awt.Font("Helvetica", 0, 18);

        Font questionFont18 = new java.awt.Font("Helvetica", 0, 14);

        FontMetrics answerFontMetrics = g.getFontMetrics(answerFont);

        FontMetrics questionFontMetrics = g.getFontMetrics(questionFont);

        FontMetrics questionFont18Metrics = g.getFontMetrics(questionFont18);

        viewWidth = kBlocksWide * kBlockWidth;

        viewHeight = kBlocksHigh * kBlockHeight;

        top = kPaddingTop;

        left = kPaddingLeft;

        g.setColor(Color.white);

        g.fill3DRect(left, top, viewWidth, kQuestionAreaHeight, false);

        g.setFont(f);

        String s = new String(String.valueOf(layout[gBlockMinY][gBlockMinX]));

        s = s.concat(" - ");

        if (gDirection == kAcross) s = s.concat("across"); else s = s.concat("down");

        g.drawString(s, left + 5, top + 12);

        g.setFont(questionFont);

        if (gDirection == kAcross) {

            Font userFont = questionFont;

            if (questionFontMetrics.stringWidth(gQuestionsAcross[layout[gBlockMinY][gBlockMinX]]) > viewWidth - 4) {

                userFont = questionFont18;

                g.setFont(questionFont18);

            }

            g.drawString(gQuestionsAcross[layout[gBlockMinY][gBlockMinX]], left + 5, (top + kQuestionAreaHeight) - 8);

        } else {

            Font userFont = questionFont;

            if (questionFontMetrics.stringWidth(gQuestionsDown[layout[gBlockMinY][gBlockMinX]]) > viewWidth - 4) {

                userFont = questionFont18;

                g.setFont(questionFont18);

            }

            g.drawString(gQuestionsDown[layout[gBlockMinY][gBlockMinX]], left + 5, (top + kQuestionAreaHeight) - 8);

        }

        left = kPaddingLeft;

        top = (kPaddingTop + kPaddingClues) + kQuestionAreaHeight;

        for (int j = 0; j < kBlocksHigh; j++) {

            for (int i = 0; i < kBlocksWide; i++) {

                tempLeft = left + (i * kBlockWidth);

                tempTop = top + (j * kBlockHeight);

                if (InActiveBlock(i, j)) {

                    if (i == gCurX && j == gCurY) g.setColor(Color.cyan); else g.setColor(Color.yellow);

                    g.fillRect(tempLeft, tempTop, kBlockWidth, kBlockHeight);

                } else {

                    g.setColor(Color.white);

                    g.fillRect(tempLeft, tempTop, kBlockWidth, kBlockHeight);

                }

                g.setColor(Color.black);

                g.drawRect(tempLeft, tempTop, kBlockWidth, kBlockHeight);

                if (layout[j][i] == -1) {

                    g.setColor(Color.black);

                    g.fillRect(tempLeft, tempTop, kBlockWidth, kBlockHeight);

                } else if (layout[j][i] != 0) {

                    String numStr = String.valueOf(layout[j][i]);

                    g.setFont(numFont);

                    g.drawString(numStr, tempLeft + 4, tempTop + 10);

                }

                if (layout[j][i] != -1) {

                    if (gGuesses[i][j].length() != 0) {

                        int sWidth = 0;

                        if (ColorMatrix[i][j] == 1) g.setColor(Color.red); else if (ColorMatrix[i][j] == 2) g.setColor(Color.blue); else g.setColor(Color.black);

                        sWidth = answerFontMetrics.stringWidth(gGuesses[i][j]);

                        g.setFont(answerFont);

                        g.drawString(gGuesses[i][j], tempLeft + ((kBlockWidth / 2) - (sWidth / 2) + 3), (tempTop + kBlockHeight) - 3);

                    }

                }

            }

        }

    }
