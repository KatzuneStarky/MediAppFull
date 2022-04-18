package com.example.mediappfull;

import android.widget.RadioGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

public class Recipe {
    private MaterialEditText recipeName;
    private MaterialEditText doctorName;
    private MaterialEditText medicamentName;
    private MaterialEditText gramos;
    private MaterialEditText horas;
    private MaterialEditText dias;
    private MaterialEditText cantidad;
    private RadioGroup via;

    public Recipe() {
    }

    public Recipe(MaterialEditText recipeName, MaterialEditText doctorName, MaterialEditText medicamentName, MaterialEditText gramos, MaterialEditText horas, MaterialEditText dias, MaterialEditText cantidad, RadioGroup via) {
        this.recipeName = recipeName;
        this.doctorName = doctorName;
        this.medicamentName = medicamentName;
        this.gramos = gramos;
        this.horas = horas;
        this.dias = dias;
        this.cantidad = cantidad;
        this.via = via;
    }

    public MaterialEditText getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(MaterialEditText recipeName) {
        this.recipeName = recipeName;
    }

    public MaterialEditText getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(MaterialEditText doctorName) {
        this.doctorName = doctorName;
    }

    public MaterialEditText getMedicamentName() {
        return medicamentName;
    }

    public void setMedicamentName(MaterialEditText medicamentName) {
        this.medicamentName = medicamentName;
    }

    public MaterialEditText getGramos() {
        return gramos;
    }

    public void setGramos(MaterialEditText gramos) {
        this.gramos = gramos;
    }

    public MaterialEditText getHoras() {
        return horas;
    }

    public void setHoras(MaterialEditText horas) {
        this.horas = horas;
    }

    public MaterialEditText getDias() {
        return dias;
    }

    public void setDias(MaterialEditText dias) {
        this.dias = dias;
    }

    public MaterialEditText getCantidad() {
        return cantidad;
    }

    public void setCantidad(MaterialEditText cantidad) {
        this.cantidad = cantidad;
    }

    public RadioGroup getVia() {
        return via;
    }

    public void setVia(RadioGroup via) {
        this.via = via;
    }
}
