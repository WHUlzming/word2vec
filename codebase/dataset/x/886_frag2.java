            if ("creationDate".equals(pColumn)) return fTotal.getCreationDate();

            if ("dueDate".equals(pColumn)) return fTotal.getDueDate();

            if ("recipientID".equals(pColumn)) return new Long(fTotal.getRecipientID());
