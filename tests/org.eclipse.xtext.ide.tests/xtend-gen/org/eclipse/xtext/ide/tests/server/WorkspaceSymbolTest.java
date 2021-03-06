/**
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.ide.tests.server;

import io.typefox.lsapi.SymbolInformation;
import io.typefox.lsapi.WorkspaceSymbolParamsImpl;
import io.typefox.lsapi.util.LsapiFactories;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.ide.tests.server.AbstractLanguageServerTest;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kosyakov - Initial contribution and API
 */
@SuppressWarnings("all")
public class WorkspaceSymbolTest extends AbstractLanguageServerTest {
  @Accessors
  public static class WorkspaceSymbolConfiguraiton {
    private String model = "";
    
    private String filePath = "MyModel.testlang";
    
    private String query = "";
    
    private String expectedSymbols = "";
    
    @Pure
    public String getModel() {
      return this.model;
    }
    
    public void setModel(final String model) {
      this.model = model;
    }
    
    @Pure
    public String getFilePath() {
      return this.filePath;
    }
    
    public void setFilePath(final String filePath) {
      this.filePath = filePath;
    }
    
    @Pure
    public String getQuery() {
      return this.query;
    }
    
    public void setQuery(final String query) {
      this.query = query;
    }
    
    @Pure
    public String getExpectedSymbols() {
      return this.expectedSymbols;
    }
    
    public void setExpectedSymbols(final String expectedSymbols) {
      this.expectedSymbols = expectedSymbols;
    }
  }
  
  @Test
  public void testDocumentSymbol_01() {
    final Procedure1<WorkspaceSymbolTest.WorkspaceSymbolConfiguraiton> _function = (WorkspaceSymbolTest.WorkspaceSymbolConfiguraiton it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("type Foo {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int bar");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("type Bar {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Foo foo");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      it.model = _builder.toString();
      it.query = "F";
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("symbol \"Foo\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[0, 5] .. [0, 8]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("symbol \"Foo.bar\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[1, 5] .. [1, 8]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("symbol \"Foo.bar.int\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[1, 1] .. [1, 4]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("symbol \"Bar.foo\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[4, 5] .. [4, 8]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      it.expectedSymbols = _builder_1.toString();
    };
    this.testDocumentSymbol(_function);
  }
  
  @Test
  public void testDocumentSymbol_02() {
    final Procedure1<WorkspaceSymbolTest.WorkspaceSymbolConfiguraiton> _function = (WorkspaceSymbolTest.WorkspaceSymbolConfiguraiton it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("type Foo {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int bar");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("type Bar {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Foo foo");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      it.model = _builder.toString();
      it.query = "oO";
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("symbol \"Foo\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[0, 5] .. [0, 8]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("symbol \"Foo.bar\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[1, 5] .. [1, 8]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("symbol \"Foo.bar.int\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[1, 1] .. [1, 4]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("symbol \"Bar.foo\" {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("kind: 0");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("location: MyModel.testlang [[4, 5] .. [4, 8]]");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      it.expectedSymbols = _builder_1.toString();
    };
    this.testDocumentSymbol(_function);
  }
  
  protected void testDocumentSymbol(final Procedure1<? super WorkspaceSymbolTest.WorkspaceSymbolConfiguraiton> configurator) {
    try {
      @Extension
      final WorkspaceSymbolTest.WorkspaceSymbolConfiguraiton configuration = new WorkspaceSymbolTest.WorkspaceSymbolConfiguraiton();
      configurator.apply(configuration);
      final String fileUri = this.operator_mappedTo(configuration.filePath, configuration.model);
      this.initialize();
      this.open(fileUri, configuration.model);
      WorkspaceSymbolParamsImpl _newWorkspaceSymbolParams = LsapiFactories.newWorkspaceSymbolParams(configuration.query);
      CompletableFuture<List<? extends SymbolInformation>> _symbol = this.languageServer.symbol(_newWorkspaceSymbolParams);
      final List<? extends SymbolInformation> symbols = _symbol.get();
      final String actualSymbols = this.toExpectation(symbols);
      Assert.assertEquals(configuration.expectedSymbols, actualSymbols);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
