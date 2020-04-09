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

package vartas.monticore.cd2java;

import de.monticore.cd.cd4analysis._ast.ASTCDCompilationUnit;
import vartas.monticore.cd2code.CDGeneratorHelper;
import vartas.monticore.cd2java.template.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class CDTransformerChain implements Supplier<List<ASTCDCompilationUnit>> {
    @Nonnull
    protected final CDGeneratorHelper generatorHelper;
    @Nonnull
    protected final List<CDConsumerTemplate<ASTCDCompilationUnit>> templates;


    public CDTransformerChain(
            @Nonnull CDGeneratorHelper generatorHelper,
            @Nonnull List<CDConsumerTemplate<ASTCDCompilationUnit>> templates
    )
    {
        this.generatorHelper = generatorHelper;
        this.templates = new ArrayList<>(templates);
    }

    public CDTransformerChain(@Nonnull CDGeneratorHelper generatorHelper){
        this(
                generatorHelper,
                Arrays.asList(
                        new CDOptionalUnwrapperTemplate(generatorHelper),
                        new CDInitializerTemplate(generatorHelper),
                        new CDImportTransformer(generatorHelper),
                        new CDPackageTransformer(generatorHelper),
                        new CDHandwrittenFileTemplate(generatorHelper),
                        new CDAnnotatorTemplate(generatorHelper)
                )
        );
    }

    @Override
    public List<ASTCDCompilationUnit> get() {
        List<ASTCDCompilationUnit> asts = Arrays.asList(
                getDecorated(),
                getVisitor(),
                getFactory()
        );

        for(ASTCDCompilationUnit ast : asts)
            for(CDConsumerTemplate<ASTCDCompilationUnit> template : templates)
                template.accept(ast);

        return asts;
    }

    protected ASTCDCompilationUnit getDecorated(){
        ASTCDCompilationUnit ast = generatorHelper.getAst();
        return new CDDecoratorTemplate(generatorHelper).apply(ast);
    }

    protected ASTCDCompilationUnit getVisitor(){
        ASTCDCompilationUnit ast = generatorHelper.getAst();
        return new CDVisitorTemplate(generatorHelper).apply(ast);
    }

    protected ASTCDCompilationUnit getFactory(){
        ASTCDCompilationUnit ast = generatorHelper.getAst();
        return new CDFactoryTemplate(generatorHelper).apply(ast);
    }
}
