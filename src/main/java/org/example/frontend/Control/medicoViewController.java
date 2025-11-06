package org.example.frontend.Control;

import org.example.backend.dao.RecetaDao;
import org.example.frontend.View.recetaView;
import org.example.frontend.View.prescribirView;
import org.example.backend.service.PrescripcionService;

public class medicoViewController {
    prescribirView prescribir;
    PrescripcionService prescribirService;
    prescripcionController prescripcionController;
    RecetaDao recetaDao;
    recetaView recetaView;
    recetaController recetaController;

    public medicoViewController(prescribirView prescribir, PrescripcionService prescribirService, RecetaDao recetaDao, recetaView recetaView, String medicoUsername) {
        this.prescribir = prescribir;
        this.prescribirService = prescribirService;
        this.recetaDao = recetaDao;
        this.recetaView = recetaView;
        this.recetaController = new recetaController(recetaView, recetaDao);
        this.prescripcionController = new prescripcionController(prescribir, prescribirService, medicoUsername, recetaController);
    }
}
