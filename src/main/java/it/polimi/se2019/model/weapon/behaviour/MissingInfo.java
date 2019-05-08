package it.polimi.se2019.model.weapon.behaviour;

public class MissingInfo extends Expression {
    // TODO: add doc
    @Override
    protected Expression continueEval(ShootContext shootContext) {
        return shootContext.popInfo().orElse(this);
    }
}

// TODO: maybe consider doing this to prevent wrong info in the future
/*
public class MissingInfo<InfoType> extends Expression {
    Class<InfoType> mInfoTypeClass;

    public MissingInfo(Class<InfoType> infoTypeClass) {
        super();

        mInfoTypeClass = infoTypeClass;
    }

    @Override
    protected Expression continueEval(ShootContext shootContext) {
        Optional<Expression> maybeInfo = shootContext.popInfo();

        if (maybeInfo.isPresent()) {
            Expression info = maybeInfo.get();

            if (!info.getClass().equals(mInfoTypeClass.getClass()))
                throw new BadInfoException(info.getClass(), mInfoTypeClass.getClass());

            return info;
        }

        return this;
    }
}
 */
