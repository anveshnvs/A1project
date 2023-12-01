package com.example.A1project;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbsKeep {
    private final CopyOnWriteArrayList<Abs<Integer>> vals = new CopyOnWriteArrayList<>();
    private int min = 2147483647;
    private int max = -2147483648;

    void add(int reading) {
        Abs<Integer> absWithDate = new Abs<>(new Date(), reading);

        vals.add(absWithDate);
        if (reading < min) min = reading;
        if (reading > max) max = reading;
    }

    @SuppressWarnings("SameParameterValue")
    CopyOnWriteArrayList<Abs<Integer>> getLastStdValues(int count) {
        if (count < vals.size()) {
            return  new CopyOnWriteArrayList<>(vals.subList(vals.size() - 1 - count, vals.size() - 1));
        } else {
            return vals;
        }
    }

    Date getLastTimestamp() {
        return vals.get(vals.size() - 1).tstm;
    }
}

