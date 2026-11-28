package mars.arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

/** The pure simulation core must not use Minecraft or NeoForge (ARCHITECTURE.md 4.4). */
class ArchitectureTest {
  private final JavaClasses classes =
      new ClassFileImporter()
          .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
          .importPackages("mars");

  @Test
  void pureCoreHasNoMinecraftDependencies() {
    noClasses()
        .that()
        .resideInAnyPackage(
            "mars.core..",
            "mars.index.sim..",
            "mars.session..",
            "mars.monitor.view..",
            "mars.dimension.ice..",
            "mars.hazards.dust..",
            "mars.power..",
            "mars.gates..",
            "mars.visual..",
            "mars.dome..",
            "mars.life..",
            "mars.landing..",
            "mars.journal..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("net.minecraft..", "net.neoforged..", "com.mojang..")
        .check(classes);
  }

  @Test
  void clientCodeStaysInClientPackage() {
    noClasses()
        .that()
        .resideOutsideOfPackage("mars.client..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("net.minecraft.client..")
        .check(classes);
  }
}
