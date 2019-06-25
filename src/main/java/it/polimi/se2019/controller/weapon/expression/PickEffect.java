package it.polimi.se2019.controller.weapon.expression;

import com.sun.media.sound.AbstractMidiDeviceProvider;
import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.action.AmmoPayment;
import it.polimi.se2019.util.ArrayUtils;
import it.polimi.se2019.view.View;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PickEffect extends Expression {
    // subexpressions
    private SortedMap<Integer, Set<Effect>> mSubexpressions;

    // maximum priority encountered
    private int mMaximumPriority;

    // trivial constructor
    public PickEffect() {
        mSubexpressions = new TreeMap<>();
        mMaximumPriority = 0;
    }

    /**
     * Add a subexpression
     * @param value subexpression to add
     */
    public void addEffect(Effect value) {
        // update maximum priority
        if (value.getPriority() > mMaximumPriority)
            mMaximumPriority = value.getPriority();

        // initialize priority list if not already there
        mSubexpressions.computeIfAbsent(
                value.getPriority(),
                key -> new HashSet<>()

        );

        // get subexpression list corresponding to the added subexpression's priority
        Set<Effect> correspondingPriorityList = mSubexpressions.get(value.getPriority());

        // add subexpression
        correspondingPriorityList.add(value);
    }

    private void manageAmmoPayment(ShootContext context, Set<Effect> effectsToSelect, List<Effect> effectsToPay) {
        // manage payment
        AmmoValue totalEffectsCost = effectsToPay.stream()
                .map(Effect::getCost)
                .reduce(new AmmoValue(0, 0, 0), AmmoValue::add);

        LOGGER.log(Level.INFO,
                "proceeding with payment of {0} (total cost: {1})",
                new Object[] {
                        effectsToSelect.stream()
                                .map(Effect::getId)
                                .collect(Collectors.toList()),
                        totalEffectsCost
                }
        );

        // if the effects cannot be payed, the selection is invalid. Ask it again
        if (!AmmoPayment.canPayWithPowerUps(context.getShooter(), totalEffectsCost)) {
            context.getView().reportError("The effects you chose cost too much to activate!" +
                    " Select less effects or undo the shoot interaction"
            );

            manageEffectSelection(context, effectsToSelect);
        }

        // if the user has enough ammo, make him pay without asking for powerup selection
        else if (AmmoPayment.isValid(context.getShooter(), totalEffectsCost, ArrayUtils.ofAll(false, 4))) {
            context.getView().showMessage("skipping powerup discard...");

            AmmoPayment.payCost(context.getShooter(), totalEffectsCost, ArrayUtils.ofAll(false, 4));
        }

        // otherwise a powerup discard is requested
        else {
            LOGGER.log(Level.INFO, "starting poewrup discard interaction loop...");

            List<Boolean> selectedPowerups = Stream.generate(() -> false).limit(4).collect(Collectors.toList());
            while (context.getShooter().getNumOfPowerupsInHand() > selectedPowerups.size()) {
                Optional<Integer> selectedPowerup =
                        context.getInteraction().selectPowerupsForPayment(context.getView(), context.getShooterColor());

                // no poewrup card is discarded when additional cost is still required
                if (!selectedPowerup.isPresent()) {
                    context.getView().reportError(
                            "Not enough discarded powerups to pay for selected effects! " +
                                    "Please select enough to satisfy the effect's cost..."
                    );
                }

                else {
                    selectedPowerups.set(selectedPowerup.get(), true);

                    if (AmmoPayment.isValid(
                            context.getShooter(),
                            totalEffectsCost,
                            ArrayUtils.from(selectedPowerups)
                    )) {
                        // payment is valid with currently selected powerups, make player pay and exit
                        LOGGER.log(Level.INFO, "Player paying discarding powerups: {0}",
                                IntStream.range(0, 4)
                                        .filter(selectedPowerups::get)
                                        .mapToObj(i -> context.getShooter().getPowerUpCard(i))
                        );

                        AmmoPayment.payCost(context.getShooter(), totalEffectsCost, ArrayUtils.from(selectedPowerups));
                        break;
                    }
                }
            }

            // this should never happen... a check above ensures that the player has enough powerups to pay for
            // the selected effects
            throw new IllegalStateException("player should have enough powerups to pay for selected effects at this" +
                    "point");
        }
    }

    private void manageEffectSelection(ShootContext context, Set<Effect> effectsToSelect) {
        if (effectsToSelect.isEmpty())
            return;

        // get view for communicating with client
        ShootInteraction interaction = context.getInteraction();
        View view = context.getView();
        // if only one non-optional effect is present no user interaction is necessary
        // TODO: check if this is duplicated inside interaction.selectEffects
        if (effectsToSelect.size() == 1 && !effectsToSelect.iterator().next().isOptional()) {
            // directly evaluate this effect and go on to next priority
            discardEvalResult(effectsToSelect.iterator().next().getBehaviour().eval(context));
            return;
        }

        // user input is requested for optional effect choice AND effect evaluation order
        List<String> selectedEffectIDs = interaction.selectEffects(view, mSubexpressions, effectsToSelect);

        // an empty choice is interpreted specially: it corresponds with the will of picking no
        // optional effects. If any non-optional effects are still available, they are offered again
        // with another selection (since they must be picked)
        if (selectedEffectIDs.isEmpty()) {
            // if all effects are already non-optional, issue a warning to the user
            if (effectsToSelect.stream().noneMatch(Effect::isOptional))
                view.showMessage("It is mandatory to select all non-optional effects!");

            // strip effects to select from optionals
            effectsToSelect.removeAll(effectsToSelect.stream()
                    .filter(Effect::isOptional)
                    .collect(Collectors.toSet()));
        }

        // once verified, the selected effect IDs are turned into actual effects
        List<Effect> selectedEffects = new ArrayList<>();
        for (String id : selectedEffectIDs) {
            selectedEffects.add(
                    effectsToSelect.stream()
                            .filter(eff -> eff.getId().equals(id))
                            .findFirst()
                            .orElseThrow(() ->
                                    new InputMismatchException("Illegal effect names!")));
        }

        // manage payment for selected effects
        manageAmmoPayment(context, effectsToSelect, selectedEffects);

        // evaluate selected effects
        selectedEffects.stream()
                .map(Effect::getBehaviour)
                .forEach(e -> discardEvalResult(e.eval(context)));

        // remove selected effects from effects to select and go on selecting
        manageEffectSelection(
                context,
                effectsToSelect.stream()
                        .filter(eff -> !selectedEffects.contains(eff))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public final Expression eval(ShootContext context) {
        // client interaction loop
        for (Map.Entry<Integer, Set<Effect>> entry : mSubexpressions.entrySet()) {
            final Set<Effect> effectsToSelect = new HashSet<>(entry.getValue());

            manageEffectSelection(context, effectsToSelect);
        }

        return new Done();
    }
}
