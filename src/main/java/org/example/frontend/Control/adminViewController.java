package org.example.frontend.Control;

import org.example.backend.dao.FarmaceuticoDao;
import org.example.backend.dao.MedicoDao;
import org.example.backend.dao.PacienteDao;
import org.example.backend.dao.RecetaDao;
import org.example.backend.dao.MedicamentoDao;

import org.example.frontend.View.*;
import org.example.frontend.View.MedicoView;

public class adminViewController {
    MedicoDao medicoDao;
    FarmaceuticoDao farmaceuticoDao;
    PacienteDao pacienteDao;
    RecetaDao recetaDao;
    MedicamentoDao medicamentoDao;


    farmaceuticoView farmaceuticoView;
    MedicoView medicoView;
    pacienteView pacienteView;
    recetaView recetaView;
    medicamentoView medicamentoView;

    medicoController medicoController;
    farmaceuticoController farmaceuticoController;
    pacienteController pacienteController;
    recetaController recetaController;
    medicamentoController medicamentoController;


    public adminViewController(MedicoDao medicoDao,
                                FarmaceuticoDao farmaceuticoDao,
                                farmaceuticoView farmaceuticoView,
                                MedicoView medicoPanel,
                                pacienteView pacienteView,
                                PacienteDao pacienteDao,
                                RecetaDao recetaDao,
                                recetaView recetaView,
                                MedicamentoDao medicamentoDao,
                                medicamentoView medicamentoView) {
        this.farmaceuticoView = farmaceuticoView;
        this.medicoView = medicoPanel;
        this.pacienteView = pacienteView;
        this.recetaView = recetaView;
        this.medicamentoView = medicamentoView;

        this.medicoDao = medicoDao;
        this.farmaceuticoDao = farmaceuticoDao;
        this.pacienteDao = pacienteDao;
        this.recetaDao = recetaDao;
        this.medicamentoDao = medicamentoDao;
        initController();
    }
    private void initController() {
        this.medicoController = new medicoController(medicoView, medicoDao);
        this.farmaceuticoController = new farmaceuticoController(farmaceuticoView, farmaceuticoDao);
        this.pacienteController = new pacienteController(pacienteView, pacienteDao);
        this.recetaController = new recetaController(recetaView, recetaDao);
        this.medicamentoController = new medicamentoController(medicamentoView, medicamentoDao);
    }
}