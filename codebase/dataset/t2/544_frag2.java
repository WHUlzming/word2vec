                    aDiaryEntry.setTitle(nextEntry.getSubject());

                    aDiaryEntry.setEntryID(nextEntry.getID());

                    if (nextEntry.getOwner().compareTo(username) == 0) {

                        aDiaryEntry.setOwnerFlag('Y');

                    } else {

                        aDiaryEntry.setOwnerFlag('N');

                    }

                    aDiaryEntry.setStart(nextEntry.getStart().getTimeInMillis());
