package org.example.common.wrappers;

import org.example.common.Farmaceutico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FarmaceuticosWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Farmaceutico> farmaceuticos = new ArrayList<>();

    public FarmaceuticosWrapper() {}

    public FarmaceuticosWrapper(List<Farmaceutico> farmaceuticos) {
        this.farmaceuticos = farmaceuticos;
    }

    public List<Farmaceutico> getFarmaceuticos() {
        return farmaceuticos;
    }

    public void setFarmaceuticos(List<Farmaceutico> farmaceuticos) {
        this.farmaceuticos = farmaceuticos;
    }

    public void add(Farmaceutico f) { farmaceuticos.add(f); }
}
