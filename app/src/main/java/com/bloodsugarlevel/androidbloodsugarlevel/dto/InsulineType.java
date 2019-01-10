package com.bloodsugarlevel.androidbloodsugarlevel.dto;

public enum InsulineType {
    LONG("LONG"),
    SHORT("SHORT"),
    ULTRA_SHORT("ULTRA_SHORT");

    private final String text;

    InsulineType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
