package controller;

import model.Action;
import model.ExitException;

public class Logout implements Action {
    @Override
    public boolean execute() throws ExitException {

        return false;
    }
}