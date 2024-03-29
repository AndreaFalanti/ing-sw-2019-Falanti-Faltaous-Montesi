package it.polimi.se2019.controller.weapon.expression;

/**
 * Exception launched when an unsupported conversion is attempted
 */
public class UnsupportedConversionException extends RuntimeException {
    public UnsupportedConversionException(String toConvert, String toConvertTo) {
        this(toConvert, toConvertTo, null);
    }
    public UnsupportedConversionException(String toConvert, String toConvertTo, String reason) {
        super("Cannot convert " + toConvert + " expression to " + toConvertTo +
                (reason == null ?
                        "!" :
                        " because " + reason + "!")
        );
    }
}
