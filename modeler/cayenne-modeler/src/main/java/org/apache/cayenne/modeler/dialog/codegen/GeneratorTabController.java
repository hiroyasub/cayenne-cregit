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
comment|//import org.apache.cayenne.modeler.util.CayenneController;
end_comment

begin_comment
comment|//import org.apache.cayenne.pref.CayenneProjectPreferences;
end_comment

begin_comment
comment|//import org.apache.cayenne.pref.PreferenceDetail;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.BindingBuilder;
end_comment

begin_comment
comment|//import org.apache.cayenne.util.Util;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import java.awt.Component;
end_comment

begin_comment
comment|//import java.awt.Dimension;
end_comment

begin_comment
comment|//import java.util.Collection;
end_comment

begin_comment
comment|//import java.util.HashMap;
end_comment

begin_comment
comment|//import java.util.Map;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class GeneratorTabController extends CayenneController {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private static final String STANDARD_OBJECTS_MODE = "Standard Persistent Objects";
end_comment

begin_comment
comment|//    private static final String CLIENT_OBJECTS_MODE = "Client Persistent Objects";
end_comment

begin_comment
comment|//    private static final String ADVANCED_MODE = "Advanced";
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public static final String GENERATOR_PROPERTY = "generator";
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private static final String[] GENERATION_MODES = new String[] {
end_comment

begin_comment
comment|//            STANDARD_OBJECTS_MODE, CLIENT_OBJECTS_MODE, ADVANCED_MODE
end_comment

begin_comment
comment|//    };
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected GeneratorTabPanel view;
end_comment

begin_comment
comment|//    protected Map controllers;
end_comment

begin_comment
comment|//    protected PreferenceDetail preferences;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public GeneratorTabController(CodeGeneratorControllerBase parent) {
end_comment

begin_comment
comment|//        super(parent);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.controllers = new HashMap(5);
end_comment

begin_comment
comment|//        controllers.put(STANDARD_OBJECTS_MODE, new StandardModeController(parent));
end_comment

begin_comment
comment|//        controllers.put(CLIENT_OBJECTS_MODE, new ClientModeController(parent));
end_comment

begin_comment
comment|//        controllers.put(ADVANCED_MODE, new CustomModeController(parent));
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        Component[] modePanels = new Component[GENERATION_MODES.length];
end_comment

begin_comment
comment|//        for (int i = 0; i< GENERATION_MODES.length; i++) {
end_comment

begin_comment
comment|//            modePanels[i] = ((GeneratorController) controllers.get(GENERATION_MODES[i]))
end_comment

begin_comment
comment|//                    .getView();
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.view = new GeneratorTabPanel(GENERATION_MODES, modePanels);
end_comment

begin_comment
comment|//        initBindings();
end_comment

begin_comment
comment|//        view.setPreferredSize(new Dimension(600, 480));
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
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
comment|//    protected CodeGeneratorControllerBase getParentController() {
end_comment

begin_comment
comment|//        return (CodeGeneratorControllerBase) getParent();
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
comment|//
end_comment

begin_comment
comment|//        // bind actions
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
comment|//        CayenneProjectPreferences cayPrPref = application.getCayenneProjectPreferences();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.preferences = (PreferenceDetail) cayPrPref.getProjectDetailObject(
end_comment

begin_comment
comment|//                PreferenceDetail.class,
end_comment

begin_comment
comment|//                getViewPreferences().node("controller"));
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (Util.isEmptyString(preferences.getProperty("mode"))) {
end_comment

begin_comment
comment|//            preferences.setProperty("mode", STANDARD_OBJECTS_MODE);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.bindToComboSelection(
end_comment

begin_comment
comment|//                view.getGenerationMode(),
end_comment

begin_comment
comment|//                "preferences.property['mode']").updateView();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public PreferenceDetail getPreferences() {
end_comment

begin_comment
comment|//        return preferences;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public GeneratorController getGeneratorController() {
end_comment

begin_comment
comment|//        Object name = view.getGenerationMode().getSelectedItem();
end_comment

begin_comment
comment|//        return (GeneratorController) controllers.get(name);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Collection<ClassGenerationAction> getConfiguration() {
end_comment

begin_comment
comment|//        GeneratorController modeController = getGeneratorController();
end_comment

begin_comment
comment|//        return (modeController != null) ? modeController.createConfiguration() : null;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

