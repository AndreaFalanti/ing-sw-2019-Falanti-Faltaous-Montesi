package it.polimi.se2019.util;

import java.util.function.Function;

public interface AbstractHandler<Handled> {
    static <Handled> AbstractHandler<Handled> singleton(Function<Handled, Void> universalHandlerFunc) {
        return new AbstractHandler<Handled>() {
            @Override
            public void fallbackHandle(Handled handled) {
                universalHandlerFunc.apply(handled);
            }
        };
    }

    default void fallbackHandle(Handled handled) {
        throw new UnsupportedOperationException(
                "Trying to use unimplemented fallback handler function on Handled of type: "
                        + handled.getClass().getSimpleName()
        );
    }
}
