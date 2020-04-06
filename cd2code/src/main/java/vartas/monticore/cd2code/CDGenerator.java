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

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Nonnull
public class CDGenerator {
    @Nonnull
    protected final GeneratorEngine generatorEngine;
    @Nonnull
    protected final GeneratorSetup generatorSetup;
    @Nonnull
    protected final CDGeneratorHelper generatorHelper;

    public CDGenerator
    (
                @Nonnull GeneratorSetup generatorSetup,
                @Nonnull CDGeneratorHelper generatorHelper
    )
    {
        this.generatorSetup = generatorSetup;
        this.generatorEngine = new GeneratorEngine(generatorSetup);
        this.generatorHelper = generatorHelper;
    }

    public void generate(@Nonnull ASTCDCompilationUnit cdCompilationUnit){
        ASTCDDefinition cdDefinition = cdCompilationUnit.getCDDefinition();

        for(ASTCDClass ast : cdDefinition.getCDClassList())
            generateClass(cdCompilationUnit.getPackageList(), ast);

        for(ASTCDEnum ast : cdDefinition.getCDEnumList())
            generateEnum(cdCompilationUnit.getPackageList(), ast);

        for(ASTCDInterface ast : cdDefinition.getCDInterfaceList())
            generateInterface(cdCompilationUnit.getPackageList(), ast);
    }

    private void generateInterface(List<String> packageName, ASTCDInterface ast){
        generate(CDGeneratorHelper.INTERFACE_TEMPLATE, packageName, ast);
    }

    private void generateEnum(List<String> packageName, ASTCDEnum ast){
        generate(CDGeneratorHelper.ENUM_TEMPLATE, packageName, ast);
    }

    private void generateClass(List<String> packageName, ASTCDClass ast){
        generate(CDGeneratorHelper.CLASS_TEMPLATE, packageName, ast);
    }

    protected void generate(String template, List<String> packageName , ASTCDType ast){
        Path outputPath = Paths.get("", packageName.toArray(String[]::new));
        Path outputFile = outputPath.resolve(ast.getName() + "." + generatorSetup.getDefaultFileExtension());
        generatorEngine.generate(template, outputFile, ast);
    }

    protected void generate(String template, Path outputFile, ASTCDType ast){
        generatorEngine.generate(template, outputFile, ast);
    }
}
