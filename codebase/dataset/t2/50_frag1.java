        for (int i = 0; i < maxID; i++) {

            bestChipsList[i] = users[i];

        }

        for (int i = maxID; --i >= 0; ) {

            for (int j = 0; j < i; j++) {

                if (bestChipsList[j].getTotalValue() > bestChipsList[j + 1].getTotalValue()) {

                    User u = bestChipsList[j];

                    bestChipsList[j] = bestChipsList[j + 1];

                    bestChipsList[j + 1] = u;

                }

            }

        }

        richest = bestChipsList[maxID - 1].getHandle();
