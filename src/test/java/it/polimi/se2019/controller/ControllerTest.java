package it.polimi.se2019.controller;

import it.polimi.se2019.view.request.LeaderboardRequest;
import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.ValidMoveRequest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ControllerTest {

    private class RequestMaker {
        public Request makeLeaderboardRequest () {
            return new LeaderboardRequest();
        }
        public Request makeValidMoveRequest() {
            return new ValidMoveRequest();
        }
    }

    @Test
    public void testRequestHandler() {
        /*************************************************************/
        /* Controller controller = new Controller();                 */
        /* RequestMaker requestMaker = new RequestMaker();           */
        /*                                                           */
        /* Request request1 = requestMaker.makeLeaderboardRequest(); */
        /* Request request2 = requestMaker.makeValidMoveRequest();   */
        /*                                                           */
        /* request1.handleMe(controller);                            */
        /* request2.handleMe(controller);                            */
        /*                                                           */
        /* assertTrue(true);                                         */
        /*************************************************************/
    }
}
