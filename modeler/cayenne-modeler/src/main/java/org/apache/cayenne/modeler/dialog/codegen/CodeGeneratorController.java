begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|///*****************************************************************
end_comment

begin_comment
comment|// *   Licensed to the Apache Software Foundation (ASF) under one
end_comment

begin_comment
comment|// *  or more contributor license agreements.  See the NOTICE file
end_comment

begin_comment
comment|// *  distributed with this work for additional information
end_comment

begin_comment
comment|// *  regarding copyright ownership.  The ASF licenses this file
end_comment

begin_comment
comment|// *  to you under the Apache License, Version 2.0 (the
end_comment

begin_comment
comment|// *  "License"); you may not use this file except in compliance
end_comment

begin_comment
comment|// *  with the License.  You may obtain a copy of the License at
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *    http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *  Unless required by applicable law or agreed to in writing,
end_comment

begin_comment
comment|// *  software distributed under the License is distributed on an
end_comment

begin_comment
comment|// *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
end_comment

begin_comment
comment|// *  KIND, either express or implied.  See the License for the
end_comment

begin_comment
comment|// *  specific language governing permissions and limitations
end_comment

begin_comment
comment|// *  under the License.
end_comment

begin_comment
comment|// ****************************************************************/
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//package org.apache.cayenne.modeler.dialog.codegen;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import org.apache.cayenne.gen.ClassGenerationAction;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.DataMap;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.dialog.ErrorDebugDialog;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.util.CayenneController;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.BindingBuilder;
end_comment

begin_comment
comment|//import org.slf4j.Logger;
end_comment

begin_comment
comment|//import org.slf4j.LoggerFactory;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import javax.swing.JOptionPane;
end_comment

begin_comment
comment|//import java.awt.Component;
end_comment

begin_comment
comment|//import java.util.Collection;
end_comment

begin_comment
comment|//import java.util.function.Predicate;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * A controller for the class generator dialog.
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class CodeGeneratorController extends CodeGeneratorControllerBase {
end_comment

begin_comment
comment|//    /**
end_comment

begin_comment
comment|//     * Logger to print stack traces
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    private static Logger logObj = LoggerFactory.getLogger(ErrorDebugDialog.class);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected CodeGeneratorDialog view;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected ClassesTabController classesSelector;
end_comment

begin_comment
comment|//    protected GeneratorTabController generatorSelector;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public CodeGeneratorController(CayenneController parent, Collection<DataMap> dataMaps) {
end_comment

begin_comment
comment|//        super(parent, dataMaps);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.classesSelector = new ClassesTabController(this, dataMaps);
end_comment

begin_comment
comment|//        this.generatorSelector = new GeneratorTabController(this);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    @Override
end_comment

begin_comment
comment|//    public Component getView() {
end_comment

begin_comment
comment|//        return view;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void startup() {
end_comment

begin_comment
comment|//        // show dialog even on empty DataMap, as custom generation may still take
end_comment

begin_comment
comment|//        // advantage of it
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        view = new CodeGeneratorDialog(generatorSelector.getView(), classesSelector.getView());
end_comment

begin_comment
comment|//        initBindings();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        view.pack();
end_comment

begin_comment
comment|//        view.setModal(true);
end_comment

begin_comment
comment|//        centerView();
end_comment

begin_comment
comment|//        makeCloseableOnEscape();
end_comment

begin_comment
comment|//        view.setVisible(true);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected void initBindings() {
end_comment

begin_comment
comment|//        BindingBuilder builder = new BindingBuilder(
end_comment

begin_comment
comment|//                getApplication().getBindingFactory(),
end_comment

begin_comment
comment|//                this);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.bindToAction(view.getCancelButton(), "cancelAction()");
end_comment

begin_comment
comment|//        builder.bindToAction(view.getGenerateButton(), "generateAction()");
end_comment

begin_comment
comment|//        builder.bindToAction(this, "classesSelectedAction()", SELECTED_PROPERTY);
end_comment

begin_comment
comment|//        builder.bindToAction(generatorSelector, "generatorSelectedAction()",
end_comment

begin_comment
comment|//                GeneratorTabController.GENERATOR_PROPERTY);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        generatorSelectedAction();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void generatorSelectedAction() {
end_comment

begin_comment
comment|//        GeneratorController controller = generatorSelector.getGeneratorController();
end_comment

begin_comment
comment|//        validate(controller);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        Predicate<Object> predicate = controller != null
end_comment

begin_comment
comment|//                ? controller.getDefaultClassFilter()
end_comment

begin_comment
comment|//                : o -> false;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        updateSelection(predicate);
end_comment

begin_comment
comment|//        classesSelector.classSelectedAction();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void classesSelectedAction() {
end_comment

begin_comment
comment|//        int size = getSelectedEntitiesSize();
end_comment

begin_comment
comment|//        String label;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (size == 0) {
end_comment

begin_comment
comment|//            label = "No entities selected";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        else if (size == 1) {
end_comment

begin_comment
comment|//            label = "One entity selected";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        else {
end_comment

begin_comment
comment|//            label = size + " entities selected";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        label = label.concat("; ");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        int sizeEmb = getSelectedEmbeddablesSize();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (sizeEmb == 0) {
end_comment

begin_comment
comment|//            label = label + "No embeddables selected";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        else if (sizeEmb == 1) {
end_comment

begin_comment
comment|//            label = label + "One embeddable selected";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        else {
end_comment

begin_comment
comment|//            label = label + sizeEmb + " embeddables selected";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        label = label.concat("; ");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        int sizeDataMap = getSelectedDataMapsSize();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if(sizeDataMap == 0) {
end_comment

begin_comment
comment|//            label = label + "No datamaps selected";
end_comment

begin_comment
comment|//        } else if(sizeDataMap == 1) {
end_comment

begin_comment
comment|//            label = label + "One datamap selected";
end_comment

begin_comment
comment|//        } else {
end_comment

begin_comment
comment|//            label = label + sizeDataMap + " datamaps selected";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        view.getClassesCount().setText(label);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void cancelAction() {
end_comment

begin_comment
comment|//        view.dispose();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void generateAction() {
end_comment

begin_comment
comment|//        Collection<ClassGenerationAction> generators = generatorSelector.getConfiguration();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (generators != null) {
end_comment

begin_comment
comment|//            try {
end_comment

begin_comment
comment|//                for (ClassGenerationAction generator : generators) {
end_comment

begin_comment
comment|//                    generator.execute();
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//                JOptionPane.showMessageDialog(
end_comment

begin_comment
comment|//                        this.getView(),
end_comment

begin_comment
comment|//                        "Class generation finished");
end_comment

begin_comment
comment|//            } catch (Exception e) {
end_comment

begin_comment
comment|//                logObj.error("Error generating classes", e);
end_comment

begin_comment
comment|//                JOptionPane.showMessageDialog(
end_comment

begin_comment
comment|//                        this.getView(),
end_comment

begin_comment
comment|//                        "Error generating classes - " + e.getMessage());
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        view.dispose();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

