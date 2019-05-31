package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.AtomicExpression;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.ShootResult;
import it.polimi.se2019.model.weapon.behaviour.TargetsLiteral;
import it.polimi.se2019.util.Either;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.HashSet;

public class ShootInteractionController implements AbstractController {
    private Controller mParentController;
    private AtomicExpression mCurrentExpression;
    private ShootContext mCurrentShootContext;
    private View mView;

    public ShootInteractionController(Controller parent, View view, Game game, ShootRequest request) {
        // initialize fields
        mParentController = parent;
        mView = view;
        mCurrentExpression = request.getBehaviour();
        mCurrentShootContext = new ShootContext(
                game.getBoard(),
                new HashSet<>(game.getPlayers()),
                request.getShooterColor()
        );

        // register to view
        mView.register(this);
    }

    private void completeShooting(Action shootAction) {
        // unregister from view
        mView.unregister(this);

        // remove shoot interaction from parent Controller
        mParentController.mShootInteractions.remove(this);

        // execute shoot action using controller
        mParentController.update(Either.right(shootAction));
    }

    private void continueShooting(AtomicExpression info) {
        mCurrentShootContext.provideInfo(info);

        ShootResult result = mCurrentExpression.evalToShootResult(mCurrentShootContext);

        if (result.isComplete()) {
            // TODO: maybe replace with call to Action dedicated controller
            completeShooting(result.asAction());
        }
        else {
            result.asResponse().handleMe(mView.getResponseHandler());
        }
    }

    @Override
    public Response handle(TargetsSelectedRequest request) {
        continueShooting(new TargetsLiteral(request.getSelectedTargets()));

        // TODO: consider if return type should be void
        return null;
    }

    @Override
    public void update(Either<Request, Action> message) {
        message.apply(
                request -> request.handleMe(this),
                action -> {
                    // ignore actions
                }
        );
    }
}
