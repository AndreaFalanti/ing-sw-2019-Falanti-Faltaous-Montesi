package it.polimi.se2019.controller.weapon.behaviour;

public class UnsupportedConversionException extends RuntimeException {
    public UnsupportedConversionException(String toConvert, String toConvertTo) {
        super("Cannot convert " + toConvert + " expression to " + toConvertTo + "!");
    }
}
