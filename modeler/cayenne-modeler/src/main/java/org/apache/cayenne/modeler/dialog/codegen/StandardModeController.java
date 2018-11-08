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
comment|//import org.apache.cayenne.modeler.pref.DataMapDefaults;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import java.awt.Component;
end_comment

begin_comment
comment|//import java.util.ArrayList;
end_comment

begin_comment
comment|//import java.util.Collection;
end_comment

begin_comment
comment|//import java.util.TreeMap;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//public class StandardModeController extends GeneratorController {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected StandardModePanel view;
end_comment

begin_comment
comment|//    protected DataMapDefaults preferences;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public StandardModeController(CodeGeneratorControllerBase parent) {
end_comment

begin_comment
comment|//        super(parent);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected void createDefaults() {
end_comment

begin_comment
comment|//        TreeMap<DataMap, DataMapDefaults> treeMap = new TreeMap<>();
end_comment

begin_comment
comment|//        ArrayList<DataMap> dataMaps = (ArrayList<DataMap>) getParentController().getDataMaps();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for (DataMap dataMap : dataMaps) {
end_comment

begin_comment
comment|//            DataMapDefaults preferences = getApplication()
end_comment

begin_comment
comment|//                    .getFrameController()
end_comment

begin_comment
comment|//                    .getProjectController()
end_comment

begin_comment
comment|//                    .getDataMapPreferences(dataMap);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            preferences.setSuperclassPackage("");
end_comment

begin_comment
comment|//            preferences.updateSuperclassPackage(dataMap, false);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            treeMap.put(dataMap, preferences);
end_comment

begin_comment
comment|//            if (getOutputPath() == null) {
end_comment

begin_comment
comment|//                setOutputPath(preferences.getOutputPath());
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
comment|//        setMapPreferences(treeMap);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected GeneratorControllerPanel createView() {
end_comment

begin_comment
comment|//        this.view = new StandardModePanel();
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
comment|//    @Override
end_comment

begin_comment
comment|//    protected ClassGenerationAction newGenerator() {
end_comment

begin_comment
comment|//        ClassGenerationAction action = new ClassGenerationAction();
end_comment

begin_comment
comment|//        getApplication().getInjector().injectMembers(action);
end_comment

begin_comment
comment|//        return action;
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
comment|//    public Collection<ClassGenerationAction> createConfiguration() {
end_comment

begin_comment
comment|//        return super.createConfiguration();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

