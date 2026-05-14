package dev.charlles.teachgram;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class TeachgramApplicationTests {

    @Test
    void verifiesModularStructure() {
        ApplicationModules.of(TeachgramApplication.class).verify();
    }
}

