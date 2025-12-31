package hexa;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "hexa.chat", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    @ArchTest
    void hexagonalArchitecture(JavaClasses classes){
        Architectures.layeredArchitecture()
            .consideringAllDependencies()
            .layer("domain").definedBy("hexa.chat.domain..")
            .layer("application").definedBy("hexa.chat.application..")
            .layer("adapter").definedBy("hexa.chat.adapter..")
            .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
            .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
            .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
            .check(classes);
    }
}
