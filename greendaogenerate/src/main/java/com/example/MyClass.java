package com.example;


import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String args[]){
        Schema schema = new Schema(1, "com.zy.callinterceptor.dao");

        Entity interceptorContact = schema.addEntity("InterceptorContact");
        interceptorContact.addIdProperty().autoincrement();
        interceptorContact.addStringProperty("name");
        interceptorContact.addStringProperty("phoneNumber");
        interceptorContact.addIntProperty("groupId");
        interceptorContact.addIntProperty("type");
        interceptorContact.addIntProperty("timeRef");

        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "./app/src/main/java");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
