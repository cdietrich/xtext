/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtend.core.tests.compiler;

import org.eclipse.xtend.core.tests.compiler.AbstractXtendCompilerTest;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.junit.Test;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@SuppressWarnings("all")
public class CompilerBug427637Test extends AbstractXtendCompilerTest {
  @Test
  public void testBug_427637_01() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<? extends V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.forEach2 [");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<? extends V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<V> _function = new Procedure1<V>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final V it) {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.forEach2(list, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.forEach2 [");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<V> _function = new Procedure1<V>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final V it) {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.<V>forEach2(list, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_03() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<? super V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.forEach2 [");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<? super V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<Object> _function = new Procedure1<Object>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final Object it) {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.forEach2(list, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_04() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<? extends V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.subList(1,1).forEach2 [");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<? extends V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("List<? extends V> _subList = list.subList(1, 1);");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<V> _function = new Procedure1<V>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final V it) {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.forEach2(_subList, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_05() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.subList(1,1).forEach2 [");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("List<V> _subList = list.subList(1, 1);");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<V> _function = new Procedure1<V>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final V it) {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.<V>forEach2(_subList, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_06() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<? super V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.subList(1,1).forEach2 [");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<? super V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("List<? super V> _subList = list.subList(1, 1);");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<Object> _function = new Procedure1<Object>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final Object it) {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.forEach2(_subList, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_07() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<? extends V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.subList(1,1).forEach2 [");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("it.toString");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<? extends V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("List<? extends V> _subList = list.subList(1, 1);");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<V> _function = new Procedure1<V>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final V it) {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("it.toString();");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.forEach2(_subList, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_08() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.subList(1,1).forEach2 [");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("it.toString");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("List<V> _subList = list.subList(1, 1);");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<V> _function = new Procedure1<V>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final V it) {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("it.toString();");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.<V>forEach2(_subList, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_09() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<? super V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.subList(1,1).forEach2 [");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("it.toString");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<? super V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("List<? super V> _subList = list.subList(1, 1);");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<Object> _function = new Procedure1<Object>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final Object it) {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("it.toString();");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.forEach2(_subList, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_10() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <V> m(List<? extends V> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("val List<V> target = null");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.forEach2 [");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("target += it");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> void forEach2(Iterable<T> iterable, (T)=>void procedure) {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <V extends Object> void m(final List<? extends V> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final List<V> target = null;");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<V> _function = new Procedure1<V>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final V it) {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("target.add(it);");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("this.forEach2(list, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void forEach2(final Iterable<T> iterable, final Procedure1<? super T> procedure) {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
  
  @Test
  public void testBug_427637_11() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class C {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("def <T> m(List<? extends T> list) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("val List<T> target = null");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("list.subList(0,1).forEach [");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("target.subList(0, 1) += it");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("target.subList(0, 1) -= it");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("target.subList(0, 1) += newArrayList(it)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("target.subList(0, 1) -= newArrayList(it)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("import com.google.common.collect.Iterables;");
    _builder_1.newLine();
    _builder_1.append("import java.util.ArrayList;");
    _builder_1.newLine();
    _builder_1.append("import java.util.List;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.CollectionLiterals;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.IterableExtensions;");
    _builder_1.newLine();
    _builder_1.append("import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("@SuppressWarnings(\"all\")");
    _builder_1.newLine();
    _builder_1.append("public class C {");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("public <T extends Object> void m(final List<? extends T> list) {");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final List<T> target = null;");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("List<? extends T> _subList = list.subList(0, 1);");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("final Procedure1<T> _function = new Procedure1<T>() {");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("public void apply(final T it) {");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("List<T> _subList = target.subList(0, 1);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("_subList.add(it);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("List<T> _subList_1 = target.subList(0, 1);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("_subList_1.remove(it);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("List<T> _subList_2 = target.subList(0, 1);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("ArrayList<T> _newArrayList = CollectionLiterals.<T>newArrayList(it);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("Iterables.<T>addAll(_subList_2, _newArrayList);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("List<T> _subList_3 = target.subList(0, 1);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("ArrayList<T> _newArrayList_1 = CollectionLiterals.<T>newArrayList(it);");
    _builder_1.newLine();
    _builder_1.append("        ");
    _builder_1.append("Iterables.removeAll(_subList_3, _newArrayList_1);");
    _builder_1.newLine();
    _builder_1.append("      ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("};");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("IterableExtensions.forEach(_subList, _function);");
    _builder_1.newLine();
    _builder_1.append("  ");
    _builder_1.append("}");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    this.assertCompilesTo(_builder, _builder_1);
  }
}