package com.MotorbikeRental.service;

import java.util.List;

public interface Model {
    public List<Model> getModelByBrandId();
    public void addNewModel(String modelName);
}
