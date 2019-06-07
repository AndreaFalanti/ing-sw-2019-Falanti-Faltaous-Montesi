package it.polimi.se2019.controller;

import it.polimi.se2019.view.request.Request;
import it.polimi.se2019.view.request.ValidPositionRequest;
import org.junit.Test;

public class ControllerTest {

    private class RequestMaker {
        public Request makeValidMoveRequest() {
            return new ValidPositionRequest();
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
