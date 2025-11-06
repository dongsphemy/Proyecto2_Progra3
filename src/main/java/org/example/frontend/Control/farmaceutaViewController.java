package org.example.frontend.Control;

import org.example.frontend.View.farmaceutaView;
import org.example.frontend.View.recetaView;
import org.example.backend.service.DespachoService;
import org.example.backend.dao.RecetaDao;


public class farmaceutaViewController {
    private farmaceutaView farmaceutaPanel;
    private DespachoService despachoService;
    private RecetaDao recetaDao;
    private recetaView recetaView;
    private despachoController despachoController;
    private recetaController recetaController;

    public farmaceutaViewController(farmaceutaView farmaceutaPanel, DespachoService despachoService, RecetaDao recetaDao, recetaView recetaView) {
        this.farmaceutaPanel = farmaceutaPanel;
        this.despachoService = despachoService;

        this.recetaDao = recetaDao;
        this.recetaView = recetaView;
        initController();
    }

    private void initController() {
        this.despachoController = new despachoController(farmaceutaPanel.getDespachoPanel(), despachoService, recetaDao);
        this.recetaController = new recetaController(recetaView, recetaDao);
    }
}
