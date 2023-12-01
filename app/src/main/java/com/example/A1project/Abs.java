package com.example.A1project;

import java.util.Date;

class Abs<T> {
    final Date tstm;
    final T val;

    Abs(Date tstm, T val) {
        this.tstm = tstm;
        this.val = val;
    }
}
