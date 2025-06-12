package org.example.exhibitly.controller;

import org.example.exhibitly.controller.ArtefactController;
import org.example.exhibitly.models.Artefact;
import javafx.scene.control.TextField;
import org.testfx.framework.junit5.ApplicationTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ArtefactControllerTest extends ApplicationTest {

    static class TestableArtefactController extends ArtefactController {
        public void setPeriodFields(String from, String to) {
            this.periodFromField = new TextField(from);
            this.periodToField = new TextField(to);
        }
        public List<Artefact> callFilterByPeriod(List<Artefact> artefacts) {
            return this.filterByPeriod(artefacts);
        }
    }

    @Test
    void testFilterByPeriod() {
        Artefact a1 = new Artefact(1, "ARCA GANESHA", "Jawa Barat", 800, "desc", "url");
        Artefact a2 = new Artefact(2, "ARCA CISITU", "Jawa Barat", 900, "desc", "url");
        Artefact a3 = new Artefact(3, "ARCA DAGU", "Jawa Barat", 1000, "desc", "url");
        Artefact a4 = new Artefact(4, "ARCA DI LUAR", "Jawa Timur", 1200, "desc", "url");
        Artefact a5 = new Artefact(5, "ARCA TUA", "DKI Jakarta", 700, "desc", "url");
        List<Artefact> artefacts = Arrays.asList(a1, a2, a3, a4, a5);

        TestableArtefactController controller = new TestableArtefactController();
        controller.setPeriodFields("800", "1000");

        List<Artefact> filtered = controller.callFilterByPeriod(artefacts);

        assertEquals(3, filtered.size());
        List<String> names = filtered.stream().map(Artefact::getTitle).collect(Collectors.toList());
        assertTrue(names.contains("ARCA GANESHA"));
        assertTrue(names.contains("ARCA CISITU"));
        assertTrue(names.contains("ARCA DAGU"));
        assertFalse(names.contains("ARCA DI LUAR"));
        assertFalse(names.contains("ARCA TUA"));
    }
}