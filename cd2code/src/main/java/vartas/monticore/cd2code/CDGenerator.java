/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.monticore.cd2code;

import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.monticore.cd2code.creator.FactoryCreator;
import vartas.monticore.cd2code.creator.VisitorCreator;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;
import vartas.monticore.cd2code.transformer.*;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

@Nonnull
public class CDGenerator {
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    @Nonnull
    protected final Path qualifiedPath;
    @Nonnull
    protected final GeneratorEngine generatorEngine;
    @Nonnull
    protected final GeneratorSetup generatorSetup;
    @Nonnull
    protected final ASTCDInterface cdVisitor;
    @Nonnull
    protected final GlobalExtensionManagement glex;
    @Nonnull
    protected final CDGeneratorHelper genHelper;
    @Nonnull
    protected final ASTCDCompilationUnit cdCompilationUnit;


    public CDGenerator
    (
                @Nonnull GeneratorSetup generatorSetup,
                @Nonnull ASTCDCompilationUnit cdCompilationUnit
    )
    {
        this.generatorSetup = generatorSetup;
        this.generatorEngine = new GeneratorEngine(generatorSetup);
        this.qualifiedPath = Paths.get("", cdCompilationUnit.getPackageList().toArray(new String[0]));
        this.glex = generatorSetup.getGlex();
        this.cdVisitor = VisitorCreator.create(cdCompilationUnit.getCDDefinition(), glex);
        this.genHelper = new CDGeneratorHelper(cdCompilationUnit);
        this.cdCompilationUnit = cdCompilationUnit;

        glex.setGlobalValue("cdPrinter", new CD2CodePrettyPrinter());
        glex.setGlobalValue("cdGenHelper", this.genHelper);
    }

    public static void generate
    (
            @Nonnull GeneratorSetup generatorSetup,
            @Nonnull ASTCDCompilationUnit cdCompilationUnit
    )
    {
        ASTCDDefinition cdDefinition = cdCompilationUnit.getCDDefinition();
        CDGenerator generator = new CDGenerator(generatorSetup, cdCompilationUnit);

        // == Visitors ==
        generator.log.info("Generating Visitors.");
        generator.generateVisitor();
        // == Factories ==
        generator.log.info("Generating Factories.");
        cdDefinition.streamCDClasss()
                .filter(cdClass -> CDGeneratorHelper.hasStereoValue(cdClass, DecoratorHelper.FACTORY_STEREOVALUE))
                .forEach(generator::generateFactory);
        // == Interfaces ==
        generator.log.info("Generating Interfaces.");
        cdDefinition.forEachCDInterfaces(generator::generateInterface);
        // == Enums ==
        generator.log.info("Generating Enums.");
        cdDefinition.forEachCDEnums(generator::generateEnum);
        // == Classes ==
        generator.log.info("Generating Classes.");
        cdDefinition.streamCDClasss()
                .map(cdClass -> CDClassTransformer.apply(cdClass, generator.glex, generator.cdVisitor))
                .forEach(generator::generateClass);
    }

    public void generateFactory(ASTCDClass cdClass){
        CDImportTransformer.applyFactoryPackage(cdCompilationUnit, glex, genHelper);
        CDPackageTransformer.applyFactoryPackage(cdCompilationUnit, glex, genHelper);

        ASTCDClass cdFactoryClass = FactoryCreator.create(cdClass, glex);
        generate(CDGeneratorHelper.CLASS_TEMPLATE, genHelper.getPackagePath(CDGeneratorHelper.FACTORY_PACKAGE) , cdFactoryClass);
    }

    public void generateVisitor(){
        CDImportTransformer.applyVisitorPackage(cdCompilationUnit, glex, genHelper);
        CDPackageTransformer.applyVisitorPackage(cdCompilationUnit, glex, genHelper);

        generate(CDGeneratorHelper.INTERFACE_TEMPLATE, genHelper.getPackagePath(CDGeneratorHelper.VISITOR_PACKAGE) , cdVisitor);
    }

    public void generateInterface(ASTCDInterface cdInterface){
        CDImportTransformer.applyDefaultPackage(cdCompilationUnit, glex, genHelper);
        CDPackageTransformer.applyDefaultPackage(cdCompilationUnit, glex, genHelper);

        generate(CDGeneratorHelper.INTERFACE_TEMPLATE, genHelper.getPackagePath() , cdInterface);
    }

    public void generateEnum(ASTCDEnum cdEnum){
        CDImportTransformer.applyDefaultPackage(cdCompilationUnit, glex, genHelper);
        CDPackageTransformer.applyDefaultPackage(cdCompilationUnit, glex, genHelper);

        generate(CDGeneratorHelper.ENUM_TEMPLATE, genHelper.getPackagePath() , cdEnum);
    }

    public void generateClass(ASTCDClass cdClass){
        CDImportTransformer.applyDefaultPackage(cdCompilationUnit, glex, genHelper);
        CDPackageTransformer.applyDefaultPackage(cdCompilationUnit, glex, genHelper);

        generate(CDGeneratorHelper.CLASS_TEMPLATE, genHelper.getPackagePath(), cdClass);
    }

    protected void generate(String template, Path outputDirectory, ASTCDType cdType){
        log.info("Applying transformers.");
        CDInitializerTransformer.apply(cdType, glex);
        CDOptionalTransformer.apply(cdType, glex);
        CDAnnotatorTransformer.apply(cdType, glex);

        log.info("Generating {}.",cdType.getName());
        Path outputPath = outputDirectory.resolve(cdType.getName() + "." + generatorSetup.getDefaultFileExtension());
        generatorEngine.generate(template, outputPath, cdType);
    }
}
