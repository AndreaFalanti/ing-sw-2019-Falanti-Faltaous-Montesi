package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.weapon.Effect;
import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickEffect extends Expression {

    // subexpressions
    private Map<Integer, List<Effect>> mSubexpressions;

    // maximum priority encountered
    private int mMaximumPriority;

    // trivial constructor
    public PickEffect() {
        mSubexpressions = new HashMap<>();
        mMaximumPriority = 0;
    }

    /**
     * Add a subexpression
     * @param value subexpression to add
     */
    public void addSub(Effect value) {
        // update maximum priority
        if (value.getPriority() > mMaximumPriority)
            mMaximumPriority = value.getPriority();

        // initialize priority list if not already there
        mSubexpressions.computeIfAbsent(
                value.getPriority(),
                key -> new ArrayList<>()

        );

        // get subexpression list corresponding to the added subexpression's priority
        List<Effect> correspondingPriorityList = mSubexpressions.get(value.getPriority());

        // add subexpression
        correspondingPriorityList.add(value);
    }

    @Override
    public Expression eval(ShootContext context) {
        // TODO: use the below code, refining it
        /*********************************************************************************************************/
        /* // get list of expressions corresponding to current priority                                          */
        /* List<Expression> currentSubs = mPriorityMap.getOrDefault(                                             */
        /*         context.getCurrentPriority(),                                                                 */
        /*         new ArrayList<>()                                                                             */
        /* );                                                                                                    */
        /*                                                                                                       */
        /* // if no subs present in this priority, keep evaluating with higher priority until maximum is reached */
        /* if (currentSubs.isEmpty()) {                                                                          */
        /*     // exit if maximum is reached                                                                     */
        /*     if (context.getCurrentPriority() >= mMaxPriority)                                                 */
        /*         return new EvalResult(false, new Done());                                                     */
        /*                                                                                                       */
        /*     // go on to next priority if not                                                                  */
        /*     context.incrementPriority();                                                                      */
        /*     return evalToEvalResult(context);                                                                 */
        /* }                                                                                                     */
        /*                                                                                                       */
        /* // do not request which sub to choose from view if there is only one that is forced                   */
        /* else if (currentSubs.size() == 1 && !currentSubs.get(0).isOptional()) {                               */
        /*     return new EvalResult(false, currentSubs.get(0).eval(context));                                   */
        /* }                                                                                                     */
        /* // else request which expression to choose from view                                                  */
        /* else {                                                                                                */
        /*     Response toAsk = new PickWeaponEffectResponse(                                                    */
        /*             mPriorityMap.entrySet().stream()                                                          */
        /*                     .map(entry ->                                                                     */
        /*                             new Map.Entry<>(entry.getKey(), entry.getValue().stream()                 */
        /*                                     .map(WeaponEffectInfo::from)))                                    */
        /*                     .collect(Collectors.toMap(                                                        */
        /*                             entry -> entry.getKey(),                                                  */
        /*                             entry -> entry.getValue()                                                 */
        /*                     ))                                                                                */
        /*     );                                                                                                */
        /*                                                                                                       */
        /*     return new EvalResult(                                                                            */
        /*             false,                                                                                    */
        /*             requestInfoFromPlayer(context, new ResponseLiteral(toAsk))                                */
        /*     );                                                                                                */
        /* }                                                                                                     */
        /*********************************************************************************************************/

        if (mSubexpressions.get(0) == null ||
                mSubexpressions.get(0).get(0) == null)
            throw new UnsupportedOperationException("WIP");

        return mSubexpressions.get(0).get(0).getBehaviour();
    }
}
