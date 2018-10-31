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
comment|//import org.apache.cayenne.map.DataMap;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.pref.DataMapDefaults;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import java.util.Map;
end_comment

begin_comment
comment|//import java.util.Map.Entry;
end_comment

begin_comment
comment|//import java.util.Set;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//public class CustomPreferencesUpdater {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    enum Property {
end_comment

begin_comment
comment|//        SUBCLASS_TEMPLATE,
end_comment

begin_comment
comment|//        SUPERCLASS_TEMPLATE,
end_comment

begin_comment
comment|//        OVERWRITE,
end_comment

begin_comment
comment|//        PAIRS,
end_comment

begin_comment
comment|//        USE_PACKAGE_PATH,
end_comment

begin_comment
comment|//        MODE,
end_comment

begin_comment
comment|//        OUTPUT_PATTERN,
end_comment

begin_comment
comment|//        CREATE_PROPERTY_NAMES,
end_comment

begin_comment
comment|//        CREATE_PK_PROPERTIES
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private static final String OVERWRITE = "overwrite";
end_comment

begin_comment
comment|//    private static final String PAIRS = "pairs";
end_comment

begin_comment
comment|//    private static final String USE_PACKAGE_PATH = "usePackagePath";
end_comment

begin_comment
comment|//    private static final String MODE = "mode";
end_comment

begin_comment
comment|//    private static final String OUTPUT_PATTERN = "outputPattern";
end_comment

begin_comment
comment|//    private static final String CREATE_PROPERTY_NAMES = "createPropertyNames";
end_comment

begin_comment
comment|//    private static final String CREATE_PK_PROPERTIES = "createPKProperties";
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private Map<DataMap, DataMapDefaults> mapPreferences;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public CustomPreferencesUpdater(Map<DataMap, DataMapDefaults> mapPreferences) {
end_comment

begin_comment
comment|//        this.mapPreferences = mapPreferences;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public String getMode() {
end_comment

begin_comment
comment|//        return (String) getProperty(Property.MODE);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setMode(String mode) {
end_comment

begin_comment
comment|//        updatePreferences(Property.MODE, mode);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public String getSubclassTemplate() {
end_comment

begin_comment
comment|//        return (String) getProperty(Property.SUBCLASS_TEMPLATE);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setSubclassTemplate(String subclassTemplate) {
end_comment

begin_comment
comment|//        updatePreferences(Property.SUBCLASS_TEMPLATE, subclassTemplate);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public String getSuperclassTemplate() {
end_comment

begin_comment
comment|//        return (String) getProperty(Property.SUPERCLASS_TEMPLATE);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setSuperclassTemplate(String superclassTemplate) {
end_comment

begin_comment
comment|//        updatePreferences(Property.SUPERCLASS_TEMPLATE, superclassTemplate);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Boolean getOverwrite() {
end_comment

begin_comment
comment|//        return (Boolean) getProperty(Property.OVERWRITE);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setOverwrite(Boolean overwrite) {
end_comment

begin_comment
comment|//        updatePreferences(Property.OVERWRITE, overwrite);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Boolean getPairs() {
end_comment

begin_comment
comment|//        return (Boolean) getProperty(Property.PAIRS);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setPairs(Boolean pairs) {
end_comment

begin_comment
comment|//        updatePreferences(Property.PAIRS, pairs);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Boolean getUsePackagePath() {
end_comment

begin_comment
comment|//        return (Boolean) getProperty(Property.USE_PACKAGE_PATH);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setUsePackagePath(Boolean usePackagePath) {
end_comment

begin_comment
comment|//        updatePreferences(Property.USE_PACKAGE_PATH, usePackagePath);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public String getOutputPattern() {
end_comment

begin_comment
comment|//        return (String) getProperty(Property.OUTPUT_PATTERN);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setOutputPattern(String outputPattern) {
end_comment

begin_comment
comment|//        updatePreferences(Property.OUTPUT_PATTERN, outputPattern);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Boolean getCreatePropertyNames() {
end_comment

begin_comment
comment|//        return (Boolean) getProperty(Property.CREATE_PROPERTY_NAMES);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setCreatePropertyNames(Boolean createPropertyNames) {
end_comment

begin_comment
comment|//        updatePreferences(Property.CREATE_PROPERTY_NAMES, createPropertyNames);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Boolean getCreatePKProperties() {
end_comment

begin_comment
comment|//        return (Boolean) getProperty(Property.CREATE_PK_PROPERTIES);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setCreatePKProperties(Boolean createPKProperties) {
end_comment

begin_comment
comment|//        updatePreferences(Property.CREATE_PK_PROPERTIES, createPKProperties);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private Object getProperty(Property property) {
end_comment

begin_comment
comment|//        Object obj = null;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        Set<Entry<DataMap, DataMapDefaults>> entities = mapPreferences.entrySet();
end_comment

begin_comment
comment|//        for (Entry<DataMap, DataMapDefaults> entry : entities) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            switch (property) {
end_comment

begin_comment
comment|//                case MODE:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getProperty(MODE);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case OUTPUT_PATTERN:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getProperty(OUTPUT_PATTERN);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case SUBCLASS_TEMPLATE:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getSubclassTemplate();
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case SUPERCLASS_TEMPLATE:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getSuperclassTemplate();
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case OVERWRITE:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getBooleanProperty(OVERWRITE);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case PAIRS:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getBooleanProperty(PAIRS);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case USE_PACKAGE_PATH:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getBooleanProperty(USE_PACKAGE_PATH);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case CREATE_PROPERTY_NAMES:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getBooleanProperty(CREATE_PROPERTY_NAMES);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case CREATE_PK_PROPERTIES:
end_comment

begin_comment
comment|//                    obj = entry.getValue().getBooleanProperty(CREATE_PK_PROPERTIES);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                default:
end_comment

begin_comment
comment|//                    throw new IllegalArgumentException("Bad type property: " + property);
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        return obj;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private void updatePreferences(Property property, Object value) {
end_comment

begin_comment
comment|//        Set<Entry<DataMap, DataMapDefaults>> entities = mapPreferences.entrySet();
end_comment

begin_comment
comment|//        for (Entry<DataMap, DataMapDefaults> entry : entities) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            switch (property) {
end_comment

begin_comment
comment|//                case MODE:
end_comment

begin_comment
comment|//                    entry.getValue().setProperty(MODE, (String) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case OUTPUT_PATTERN:
end_comment

begin_comment
comment|//                    entry.getValue().setProperty(OUTPUT_PATTERN, (String) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case SUBCLASS_TEMPLATE:
end_comment

begin_comment
comment|//                    entry.getValue().setSubclassTemplate((String) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case SUPERCLASS_TEMPLATE:
end_comment

begin_comment
comment|//                    entry.getValue().setSuperclassTemplate((String) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case OVERWRITE:
end_comment

begin_comment
comment|//                    entry.getValue().setBooleanProperty(OVERWRITE, (Boolean) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case PAIRS:
end_comment

begin_comment
comment|//                    entry.getValue().setBooleanProperty(PAIRS, (Boolean) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case USE_PACKAGE_PATH:
end_comment

begin_comment
comment|//                    entry.getValue().setBooleanProperty(USE_PACKAGE_PATH, (Boolean) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case CREATE_PROPERTY_NAMES:
end_comment

begin_comment
comment|//                    entry.getValue().setBooleanProperty(CREATE_PROPERTY_NAMES, (Boolean) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                case CREATE_PK_PROPERTIES:
end_comment

begin_comment
comment|//                    entry.getValue().setBooleanProperty(CREATE_PK_PROPERTIES, (Boolean) value);
end_comment

begin_comment
comment|//                    break;
end_comment

begin_comment
comment|//                default:
end_comment

begin_comment
comment|//                    throw new IllegalArgumentException("Bad type property: " + property);
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

