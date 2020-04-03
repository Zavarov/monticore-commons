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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.monticore.cd.cd4analysis._ast.*;
import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.monticore.cd2code.creator.FactoryCreator;
import vartas.monticore.cd2code.creator.VisitorCreator;
import vartas.monticore.cd2code.prettyprint.CD2CodePrettyPrinter;
import vartas.monticore.cd2code.transformer.CDAnnotatorTransformer;
import vartas.monticore.cd2code.transformer.CDClassTransformers;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

        glex.setGlobalValue("cdPrinter", new CD2CodePrettyPrinter());
        glex.setGlobalValue("cdGenHelper", this.genHelper);

        List<ASTMCImportStatement> mcImportStatements = cdCompilationUnit.getMCImportStatementList();
        List<String> importStatements = Lists.transform(mcImportStatements, ASTMCImportStatement::printType);
        String importStatement = Joiner.on("\n").join(importStatements);

        glex.replaceTemplate(CDGeneratorHelper.IMPORT_TEMPLATE, new StringHookPoint(importStatement));

        CDAnnotatorTransformer.apply(cdVisitor, glex);
    }

    public static void generate
    (
            @Nonnull GeneratorSetup generatorSetup,
            @Nonnull ASTCDCompilationUnit cdCompilationUnit
    )
    {
        ASTCDDefinition cdDefinition = cdCompilationUnit.getCDDefinition();
        CDGenerator generator = new CDGenerator(generatorSetup, cdCompilationUnit);

        generator.log.info("Generating Visitors.");
        // == Visitors ==
        generator.generateVisitor();
        generator.log.info("Generating Factories.");
        // == Factories ==
        cdDefinition.streamCDClasss()
                .filter(cdClass -> CDGeneratorHelper.hasStereoValue(cdClass, DecoratorHelper.FACTORY_STEREOVALUE))
                .forEach(generator::generateFactory);
        generator.log.info("Generating Interfaces.");
        // == Interfaces ==
        cdDefinition.forEachCDInterfaces(generator::generateInterface);
        generator.log.info("Generating Enums.");
        // == Enums ==
        generator.log.info("Generating Classes.");
        cdDefinition.forEachCDEnums(generator::generateEnum);
        // == Classes ==
    }

    public ASTCDClass transform(ASTCDClass cdClass){
        return CDClassTransformers.transform(cdClass, cdVisitor, glex);
    }

    public void generateFactory(ASTCDClass cdClass){
        ASTCDClass cdFactoryClass = FactoryCreator.create(cdClass, glex);
        glex.replaceTemplate(CDGeneratorHelper.PACKAGE_TEMPLATE, cdFactoryClass, new StringHookPoint(genHelper.getFactoryPackage()));
        generate(CDGeneratorHelper.CLASS_TEMPLATE, genHelper.getFactoryPackagePath() , cdFactoryClass);
    }

    public void generateVisitor(){
        glex.replaceTemplate(CDGeneratorHelper.PACKAGE_TEMPLATE, cdVisitor, new StringHookPoint(genHelper.getVisitorPackage()));
        generate(CDGeneratorHelper.INTERFACE_TEMPLATE, genHelper.getVisitorPackagePath() , cdVisitor);
    }

    public void generateInterface(ASTCDInterface cdInterface){
        glex.replaceTemplate(CDGeneratorHelper.PACKAGE_TEMPLATE, cdInterface, new StringHookPoint(genHelper.getDefaultPackage()));
        generate(CDGeneratorHelper.INTERFACE_TEMPLATE, genHelper.getDefaultPackagePath() , cdInterface);
    }

    public void generateEnum(ASTCDEnum cdEnum){
        glex.replaceTemplate(CDGeneratorHelper.PACKAGE_TEMPLATE, cdEnum, new StringHookPoint(genHelper.getDefaultPackage()));
        generate(CDGeneratorHelper.ENUM_TEMPLATE, genHelper.getDefaultPackagePath() , cdEnum);
    }

    public void generateClass(ASTCDClass cdClass){
        ASTCDClass cdTransformedClass = transform(cdClass);
        glex.replaceTemplate(CDGeneratorHelper.PACKAGE_TEMPLATE, cdTransformedClass, new StringHookPoint(genHelper.getDefaultPackage()));
        generate(CDGeneratorHelper.CLASS_TEMPLATE, genHelper.getDefaultPackagePath(), cdTransformedClass);
    }

    protected void generate(String template, Path outputDirectory, ASTCDType cdType){
        log.info("Generating {}.",cdType.getName());
        Path outputPath = outputDirectory.resolve(cdType.getName() + "." + generatorSetup.getDefaultFileExtension());
        generatorEngine.generate(template, outputPath, cdType);
    }
}
