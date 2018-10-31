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
comment|//import org.apache.cayenne.map.Embeddable;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.ObjEntity;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.util.CayenneController;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.util.CellRenderers;
end_comment

begin_comment
comment|//import org.apache.cayenne.validation.ValidationFailure;
end_comment

begin_comment
comment|//import org.apache.cayenne.validation.ValidationResult;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import javax.swing.Icon;
end_comment

begin_comment
comment|//import javax.swing.JLabel;
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
comment|//import java.util.HashSet;
end_comment

begin_comment
comment|//import java.util.List;
end_comment

begin_comment
comment|//import java.util.Set;
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
comment|// * A base superclass of a top controller for the code generator. Defines all common model
end_comment

begin_comment
comment|// * parts used in class generation.
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public abstract class CodeGeneratorControllerBase extends CayenneController {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public static final String SELECTED_PROPERTY = "selected";
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected Collection<DataMap> dataMaps;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected ValidationResult validation;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected List<Object> classes;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private Set<String> selectedEntities;
end_comment

begin_comment
comment|//    private Set<String> selectedEmbeddables;
end_comment

begin_comment
comment|//    private Set<String> selectedDataMaps;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private transient Object currentClass;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public CodeGeneratorControllerBase(CayenneController parent, Collection<DataMap> dataMaps) {
end_comment

begin_comment
comment|//        super(parent);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.dataMaps = dataMaps;
end_comment

begin_comment
comment|//        this.classes = new ArrayList<>();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for(DataMap dataMap:dataMaps){
end_comment

begin_comment
comment|//            this.classes.addAll(dataMap.getObjEntities());
end_comment

begin_comment
comment|//            this.classes.addAll(dataMap.getEmbeddables());
end_comment

begin_comment
comment|//            this.classes.add(dataMap);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        this.selectedEntities = new HashSet<>();
end_comment

begin_comment
comment|//        this.selectedEmbeddables = new HashSet<>();
end_comment

begin_comment
comment|//        this.selectedDataMaps = new HashSet<>();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public List<Object> getClasses() {
end_comment

begin_comment
comment|//        return classes;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public abstract Component getView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void validate(GeneratorController validator) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationResult validationBuffer = new ValidationResult();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (validator != null) {
end_comment

begin_comment
comment|//            for (Object classObj : classes) {
end_comment

begin_comment
comment|//                if (classObj instanceof ObjEntity) {
end_comment

begin_comment
comment|//                    validator.validateEntity(
end_comment

begin_comment
comment|//                            validationBuffer,
end_comment

begin_comment
comment|//                            (ObjEntity) classObj,
end_comment

begin_comment
comment|//                            false);
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//                else if (classObj instanceof Embeddable) {
end_comment

begin_comment
comment|//                    validator.validateEmbeddable(validationBuffer, (Embeddable) classObj);
end_comment

begin_comment
comment|//                }
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
comment|//
end_comment

begin_comment
comment|//        this.validation = validationBuffer;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public boolean updateSelection(Predicate<Object> predicate) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        boolean modified = false;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for (Object classObj : classes) {
end_comment

begin_comment
comment|//            boolean select = predicate.test(classObj);
end_comment

begin_comment
comment|//            if (classObj instanceof ObjEntity) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                if (select) {
end_comment

begin_comment
comment|//                    if (selectedEntities.add(((ObjEntity) classObj).getName())) {
end_comment

begin_comment
comment|//                        modified = true;
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//                else {
end_comment

begin_comment
comment|//                    if (selectedEntities.remove(((ObjEntity) classObj).getName())) {
end_comment

begin_comment
comment|//                        modified = true;
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            else if (classObj instanceof Embeddable) {
end_comment

begin_comment
comment|//                if (select) {
end_comment

begin_comment
comment|//                    if (selectedEmbeddables.add(((Embeddable) classObj).getClassName())) {
end_comment

begin_comment
comment|//                        modified = true;
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//                else {
end_comment

begin_comment
comment|//                    if (selectedEmbeddables
end_comment

begin_comment
comment|//                            .remove(((Embeddable) classObj).getClassName())) {
end_comment

begin_comment
comment|//                        modified = true;
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            } else if(classObj instanceof DataMap) {
end_comment

begin_comment
comment|//                if(select) {
end_comment

begin_comment
comment|//                    if(selectedDataMaps.add(((DataMap) classObj).getName())) {
end_comment

begin_comment
comment|//                        modified = true;
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                } else {
end_comment

begin_comment
comment|//                    if(selectedDataMaps.remove(((DataMap) classObj).getName())) {
end_comment

begin_comment
comment|//                        modified = true;
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
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
comment|//
end_comment

begin_comment
comment|//        if (modified) {
end_comment

begin_comment
comment|//            firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return modified;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public boolean updateDataMapSelection(Predicate<Object> predicate, DataMap dataMap) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        boolean modified = false;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for (Object classObj : classes) {
end_comment

begin_comment
comment|//            boolean select = predicate.test(classObj);
end_comment

begin_comment
comment|//            if (classObj instanceof ObjEntity) {
end_comment

begin_comment
comment|//                if(dataMap.getObjEntities().contains(classObj)) {
end_comment

begin_comment
comment|//                    if (select) {
end_comment

begin_comment
comment|//                        if (selectedEntities.add(((ObjEntity) classObj).getName())) {
end_comment

begin_comment
comment|//                            modified = true;
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//                    } else {
end_comment

begin_comment
comment|//                        if (selectedEntities.remove(((ObjEntity) classObj).getName())) {
end_comment

begin_comment
comment|//                            modified = true;
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            else if (classObj instanceof Embeddable) {
end_comment

begin_comment
comment|//                if(dataMap.getEmbeddables().contains(classObj)) {
end_comment

begin_comment
comment|//                    if (select) {
end_comment

begin_comment
comment|//                        if (selectedEmbeddables.add(((Embeddable) classObj).getClassName())) {
end_comment

begin_comment
comment|//                            modified = true;
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//                    } else {
end_comment

begin_comment
comment|//                        if (selectedEmbeddables
end_comment

begin_comment
comment|//                                .remove(((Embeddable) classObj).getClassName())) {
end_comment

begin_comment
comment|//                            modified = true;
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            } else {
end_comment

begin_comment
comment|//                if(dataMap == classObj) {
end_comment

begin_comment
comment|//                    if (select) {
end_comment

begin_comment
comment|//                        if (selectedDataMaps.add(((DataMap) classObj).getName())) {
end_comment

begin_comment
comment|//                            modified = true;
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//                    } else {
end_comment

begin_comment
comment|//                        if (selectedDataMaps.remove(((DataMap) classObj).getName())) {
end_comment

begin_comment
comment|//                            modified = true;
end_comment

begin_comment
comment|//                        }
end_comment

begin_comment
comment|//                    }
end_comment

begin_comment
comment|//                }
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
comment|//
end_comment

begin_comment
comment|//        if (modified) {
end_comment

begin_comment
comment|//            firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return modified;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public List<Embeddable> getSelectedEmbeddables() {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        List<Embeddable> selected = new ArrayList<>(selectedEmbeddables.size());
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for (Object classObj : classes) {
end_comment

begin_comment
comment|//            if (classObj instanceof Embeddable
end_comment

begin_comment
comment|//&& selectedEmbeddables.contains(((Embeddable) classObj)
end_comment

begin_comment
comment|//                    .getClassName())) {
end_comment

begin_comment
comment|//                selected.add((Embeddable) classObj);
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
comment|//        return selected;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public List<ObjEntity> getSelectedEntities() {
end_comment

begin_comment
comment|//        List<ObjEntity> selected = new ArrayList<>(selectedEntities.size());
end_comment

begin_comment
comment|//        for (Object classObj : classes) {
end_comment

begin_comment
comment|//            if (classObj instanceof ObjEntity
end_comment

begin_comment
comment|//&& selectedEntities.contains(((ObjEntity) classObj).getName())) {
end_comment

begin_comment
comment|//                selected.add(((ObjEntity) classObj));
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
comment|//        return selected;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public List<DataMap> getSelectedDataMaps() {
end_comment

begin_comment
comment|//        List<DataMap> selected = new ArrayList<>(selectedDataMaps.size());
end_comment

begin_comment
comment|//        for(Object classObj : classes) {
end_comment

begin_comment
comment|//            if(classObj instanceof DataMap
end_comment

begin_comment
comment|//&& selectedDataMaps.contains(((DataMap) classObj).getName())) {
end_comment

begin_comment
comment|//                selected.add((DataMap) classObj);
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        return selected;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public int getSelectedEntitiesSize() {
end_comment

begin_comment
comment|//        return selectedEntities.size();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public int getSelectedEmbeddablesSize() {
end_comment

begin_comment
comment|//        return selectedEmbeddables.size();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public int getSelectedDataMapsSize() {
end_comment

begin_comment
comment|//        return selectedDataMaps.size();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    /**
end_comment

begin_comment
comment|//     * Returns the first encountered validation problem for an antity matching the name or
end_comment

begin_comment
comment|//     * null if the entity is valid or the entity is not present.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    public String getProblem(Object obj) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        String name = null;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (obj instanceof ObjEntity) {
end_comment

begin_comment
comment|//            name = ((ObjEntity) obj).getName();
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        else if (obj instanceof Embeddable) {
end_comment

begin_comment
comment|//            name = ((Embeddable) obj).getClassName();
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (validation == null) {
end_comment

begin_comment
comment|//            return null;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        List failures = validation.getFailures(name);
end_comment

begin_comment
comment|//        if (failures.isEmpty()) {
end_comment

begin_comment
comment|//            return null;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return ((ValidationFailure) failures.get(0)).getDescription();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public boolean isSelected() {
end_comment

begin_comment
comment|//        if (currentClass instanceof ObjEntity) {
end_comment

begin_comment
comment|//            return selectedEntities
end_comment

begin_comment
comment|//                    .contains(((ObjEntity) currentClass).getName());
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        if (currentClass instanceof Embeddable) {
end_comment

begin_comment
comment|//            return selectedEmbeddables
end_comment

begin_comment
comment|//                    .contains(((Embeddable) currentClass).getClassName());
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        if(currentClass instanceof DataMap) {
end_comment

begin_comment
comment|//            return selectedDataMaps
end_comment

begin_comment
comment|//                    .contains(((DataMap) currentClass).getName());
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        return false;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setSelected(boolean selectedFlag) {
end_comment

begin_comment
comment|//        if (currentClass == null) {
end_comment

begin_comment
comment|//            return;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        if (currentClass instanceof ObjEntity) {
end_comment

begin_comment
comment|//            if (selectedFlag) {
end_comment

begin_comment
comment|//                if (selectedEntities.add(((ObjEntity) currentClass).getName())) {
end_comment

begin_comment
comment|//                    firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            else {
end_comment

begin_comment
comment|//                if (selectedEntities.remove(((ObjEntity) currentClass).getName())) {
end_comment

begin_comment
comment|//                    firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        if (currentClass instanceof Embeddable) {
end_comment

begin_comment
comment|//            if (selectedFlag) {
end_comment

begin_comment
comment|//                if (selectedEmbeddables.add(((Embeddable) currentClass).getClassName())) {
end_comment

begin_comment
comment|//                    firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            else {
end_comment

begin_comment
comment|//                if (selectedEmbeddables
end_comment

begin_comment
comment|//                        .remove(((Embeddable) currentClass).getClassName())) {
end_comment

begin_comment
comment|//                    firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        if(currentClass instanceof DataMap) {
end_comment

begin_comment
comment|//            if(selectedFlag) {
end_comment

begin_comment
comment|//                if(selectedDataMaps.add(((DataMap) currentClass).getName())) {
end_comment

begin_comment
comment|//                    firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            } else {
end_comment

begin_comment
comment|//                if(selectedDataMaps.remove(((DataMap) currentClass).getName())) {
end_comment

begin_comment
comment|//                    firePropertyChange(SELECTED_PROPERTY, null, null);
end_comment

begin_comment
comment|//                }
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
comment|//
end_comment

begin_comment
comment|//    public Object getCurrentClass() {
end_comment

begin_comment
comment|//        return currentClass;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setCurrentClass(Object currentClass) {
end_comment

begin_comment
comment|//        this.currentClass = currentClass;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Collection<DataMap> getDataMaps() {
end_comment

begin_comment
comment|//        return dataMaps;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JLabel getItemName(Object obj) {
end_comment

begin_comment
comment|//        String className;
end_comment

begin_comment
comment|//        Icon icon;
end_comment

begin_comment
comment|//        if (obj instanceof Embeddable) {
end_comment

begin_comment
comment|//            className = ((Embeddable) obj).getClassName();
end_comment

begin_comment
comment|//            icon = CellRenderers.iconForObject(new Embeddable());
end_comment

begin_comment
comment|//        } else if(obj instanceof ObjEntity) {
end_comment

begin_comment
comment|//            className = ((ObjEntity) obj).getName();
end_comment

begin_comment
comment|//            icon = CellRenderers.iconForObject(new ObjEntity());
end_comment

begin_comment
comment|//        } else {
end_comment

begin_comment
comment|//            className = ((DataMap) obj).getName();
end_comment

begin_comment
comment|//            icon = CellRenderers.iconForObject(new DataMap());
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        JLabel labelIcon = new JLabel();
end_comment

begin_comment
comment|//        labelIcon.setIcon(icon);
end_comment

begin_comment
comment|//        labelIcon.setVisible(true);
end_comment

begin_comment
comment|//        labelIcon.setText(className);
end_comment

begin_comment
comment|//        return labelIcon;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//}
end_comment

end_unit

