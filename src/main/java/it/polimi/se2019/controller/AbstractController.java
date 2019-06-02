package it.polimi.se2019.controller;

import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.util.Either;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.request.Request;

public interface AbstractController  extends Observer<Either<Request, Action>>, RequestHandler { }
