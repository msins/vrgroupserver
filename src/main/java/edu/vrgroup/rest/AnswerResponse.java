package edu.vrgroup.rest;

import com.google.common.base.MoreObjects;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.model.User;

import java.lang.reflect.Field;

public class AnswerResponse {

    public User user;
    public Scenario scenario;
    public Question question;
    public Choice choice;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("user", user)
                .add("scenario", scenario)
                .add("question", question)
                .add("choice", choice)
                .toString();
    }

}
