package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.*;
import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.model.weapon.serialization.ExpressionFactory;
import it.polimi.se2019.util.Exclude;
import it.polimi.se2019.view.request.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: refine doc
public abstract class Expression {
    @Exclude
    public Logger logger = Logger.getLogger(getClass().getName());

    public Expression() {

    }

    /**
     * Evaluates expressions depending on its type
     * @param context context used for evaluation
     * @return evaluated expression
     */
    public abstract Expression eval(ShootContext context);

    // TODO: add doc
    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass()) return false;

        Expression casted = (Expression) o;
        return ExpressionFactory.toJsonTree(this).equals(
                ExpressionFactory.toJsonTree(casted)
        );
    }

    // TODO: add doc
    @Override
    public final int hashCode() {
        return Objects.hash(ExpressionFactory.toJsonTree(this));
    }

    // TODO: add doc
    // TODO: maybe make faster
    public Expression deepCopy() {
        return ExpressionFactory.fromRawJson(ExpressionFactory.toJsonTree(this));
    }

    // inflict damage
    protected static void inflictDamage(ShootContext context, PlayerColor inflicter, Set<PlayerColor> inflicted, Damage amount) {
        Game game = context.getGame();

        System.out.println(inflicter + " inflicting " + amount + " damage to " + inflicted);

        inflicted.forEach(
                singularInflicted -> game.handleDamageInteraction(inflicter, singularInflicted, amount)
        );
    }

    // move player around
    protected static void move(ShootContext context, Set<PlayerColor> who, Position where) {
        Game game = context.getGame();

        who
                .forEach(pl -> game.getPlayerFromColor(pl).move(where));
    }

    // safely discard result of evaluated expression by issuing a warning
    protected static Expression discardEvalResult(Expression result) {
        if (!result.isDone())
            // TODO: use logger (todof)
            System.out.println(
                    "WARNING:" + result.getClass().getSimpleName() + "was discarded after evaluation!"
            );

        return result;
    }

    // wait for a particular request
    private Request waitForSelectionRequest(ShootInteraction interaction, Function<Request, Object> selectionGetter,
                                            String selectionDescriptor) {
        // TODO: use poll instead of take in case nothing is ever returned by anyone...
        // NB: treat this as an exception since player timeout should be handled by the main RequestHandler
        try {
            logger.log(Level.INFO, "Shoot interaction waiting for {0} selection...", selectionDescriptor);
            Request request = interaction.getRequestQueue().take();
            // TODO: check that request type is sound
            logger.log(Level.INFO, "Shoot interaction received {0} selection: [{1}]",
                    new Object[]{selectionDescriptor, selectionGetter.apply(request)});
            return request;
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Shoot interaction interrupted while waiting for {0} selection!", selectionDescriptor);
            throw new EvaluationInterruptedException("selectTargets");
        }
    }

    // select targets
    protected Set<PlayerColor> selectTargets(ShootContext context, int min, int max,
                                             Set<PlayerColor> possibleTargets) {
        context.getView().showTargetsSelectionView(min, max, possibleTargets);

        TargetsSelectedRequest request = (TargetsSelectedRequest) waitForSelectionRequest(
                context.getShootInteraction(),
                req -> ((TargetsSelectedRequest) req).getSelectedTargets(),
                "target"
        );

        return request.getSelectedTargets();
    }

    // select position
    protected Position selectPosition(ShootContext context, Set<Position> range) {
        context.getView().showPositionSelectionView(range);

        PositionSelectedRequest request = (PositionSelectedRequest) waitForSelectionRequest(
                context.getShootInteraction(),
                req -> ((PositionSelectedRequest) req).getPosition(),
                "position"
        );

        return request.getPosition();
    }

    // select effects
    protected List<String> selectEffects(ShootContext context,
                                         SortedMap<Integer, Set<Effect>> priorityMap, int currentPriority) {
        // select effects through view
        context.getView().showEffectsSelectionView(priorityMap, currentPriority);

        EffectsSelectedRequest request = (EffectsSelectedRequest) waitForSelectionRequest(
                context.getShootInteraction(),
                req -> ((EffectsSelectedRequest) req).getSelectedEffects(),
                "effect"
        );

        return request.getSelectedEffects();
    }

    // select modes
    protected String selectWeaponMode(ShootContext context, Effect mode1, Effect mode2) {
        // select effects through view
        context.getView().showWeaponModeSelectionView(mode1, mode2);

        WeaponModeSelectedRequest request = (WeaponModeSelectedRequest) waitForSelectionRequest(
                context.getShootInteraction(),
                req -> ((WeaponModeSelectedRequest) req).getId(),
                "mode"
        );

        return request.getId();
    }

    /**
     * Undoes the side effects that the evaluation of an expression had on a game object
     * @param info an object containing the info needed to undo the partial effects of a shoot expression on a game
     */
    public static void undoShootInteraction(ShootUndoInfo info) {
        throw new UnsupportedOperationException();
    }

    // evaluation is done (it's only done in Done expression)
    public boolean isDone() {
        return false;
    }

    // conversions into primitives
    public int asInt() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "int");
    }
    public Set<PlayerColor> asTargets() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Targets");
    }
    public PlayerColor asTarget() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Target");
    }
    public Set<Position> asRange() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Range");
    }
    public Position asPosition() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Position");
    }
    public Damage asDamage() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Damage");
    }
    public String asString() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "String");
    }
    public Direction asDirection() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "Direction");
    }
    public SetExpression asSetExpr() {
        throw new UnsupportedConversionException(getClass().getSimpleName(), "SetExpression");
    }
}

