            while (it.hasNext()) {

                Record r = (Record) it.next();

                if (r.getName().isWild() && !name.isWild()) r = r.withName(name);

                response.addRecord(r, section);
