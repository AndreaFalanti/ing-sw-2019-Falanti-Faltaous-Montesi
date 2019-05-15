package it.polimi.se2019.model.weapon.behaviour;

public class UnsupportedConversionException extends RuntimeException {
    public UnsupportedConversionException(String toConvert, String toConvertTo) {
        super("Cannot convert " + toConvert + " expression to " + toConvertTo + "!");
    }
}
