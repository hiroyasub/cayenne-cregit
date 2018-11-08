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
comment|//import org.apache.cayenne.CayenneRuntimeException;
end_comment

begin_comment
comment|//import org.apache.cayenne.gen.ArtifactsGenerationMode;
end_comment

begin_comment
comment|//import org.apache.cayenne.gen.ClassGenerationAction;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.DataMap;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.Embeddable;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.EmbeddableAttribute;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.EmbeddedAttribute;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.ObjAttribute;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.ObjEntity;
end_comment

begin_comment
comment|//import org.apache.cayenne.map.ObjRelationship;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.Application;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.dialog.pref.GeneralPreferences;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.pref.DataMapDefaults;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.pref.FSPath;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.util.CayenneController;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.util.CodeValidationUtil;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.BindingBuilder;
end_comment

begin_comment
comment|//import org.apache.cayenne.util.Util;
end_comment

begin_comment
comment|//import org.apache.cayenne.validation.BeanValidationFailure;
end_comment

begin_comment
comment|//import org.apache.cayenne.validation.SimpleValidationFailure;
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
comment|//import javax.swing.JButton;
end_comment

begin_comment
comment|//import javax.swing.JFileChooser;
end_comment

begin_comment
comment|//import javax.swing.JOptionPane;
end_comment

begin_comment
comment|//import javax.swing.JTextField;
end_comment

begin_comment
comment|//import java.io.File;
end_comment

begin_comment
comment|//import java.util.ArrayList;
end_comment

begin_comment
comment|//import java.util.Collection;
end_comment

begin_comment
comment|//import java.util.LinkedList;
end_comment

begin_comment
comment|//import java.util.Map;
end_comment

begin_comment
comment|//import java.util.Set;
end_comment

begin_comment
comment|//import java.util.function.Predicate;
end_comment

begin_comment
comment|//import java.util.prefs.Preferences;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * A mode-specific part of the code generation dialog.
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public abstract class GeneratorController extends CayenneController {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected String mode = ArtifactsGenerationMode.ALL.getLabel();
end_comment

begin_comment
comment|//    protected Map<DataMap, DataMapDefaults> mapPreferences;
end_comment

begin_comment
comment|//    private String outputPath;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public GeneratorController(CodeGeneratorControllerBase parent) {
end_comment

begin_comment
comment|//        super(parent);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        createDefaults();
end_comment

begin_comment
comment|//        createView();
end_comment

begin_comment
comment|//        initBindings(new BindingBuilder(getApplication().getBindingFactory(), this));
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public String getOutputPath() {
end_comment

begin_comment
comment|//        return outputPath;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setOutputPath(String path) {
end_comment

begin_comment
comment|//        String old = this.outputPath;
end_comment

begin_comment
comment|//        this.outputPath = path;
end_comment

begin_comment
comment|//        if (this.outputPath != null&& !this.outputPath.equals(old)) {
end_comment

begin_comment
comment|//            updatePreferences(path);
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
comment|//    public void updatePreferences(String path) {
end_comment

begin_comment
comment|//        if (mapPreferences == null)
end_comment

begin_comment
comment|//            return;
end_comment

begin_comment
comment|//        Set<DataMap> keys = mapPreferences.keySet();
end_comment

begin_comment
comment|//        for (DataMap key : keys) {
end_comment

begin_comment
comment|//            mapPreferences
end_comment

begin_comment
comment|//                    .get(key)
end_comment

begin_comment
comment|//                    .setOutputPath(path);
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
comment|//    public void setMapPreferences(Map<DataMap, DataMapDefaults> mapPreferences) {
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
comment|//    public Map<DataMap, DataMapDefaults> getMapPreferences() {
end_comment

begin_comment
comment|//        return this.mapPreferences;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected void initBindings(BindingBuilder bindingBuilder) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        initOutputFolder();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JTextField outputFolder = ((GeneratorControllerPanel) getView()).getOutputFolder();
end_comment

begin_comment
comment|//        JButton outputSelect = ((GeneratorControllerPanel) getView()).getSelectOutputFolder();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        outputFolder.setText(getOutputPath());
end_comment

begin_comment
comment|//        bindingBuilder.bindToAction(outputSelect, "selectOutputFolderAction()");
end_comment

begin_comment
comment|//        bindingBuilder.bindToTextField(outputFolder, "outputPath");
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
comment|//    protected abstract GeneratorControllerPanel createView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected abstract void createDefaults();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    /**
end_comment

begin_comment
comment|//     * Creates an appropriate subclass of {@link ClassGenerationAction},
end_comment

begin_comment
comment|//     * returning it in an unconfigured state. Configuration is performed by
end_comment

begin_comment
comment|//     * {@link #createConfiguration()} method.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    protected abstract ClassGenerationAction newGenerator();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    /**
end_comment

begin_comment
comment|//     * Creates a class generator for provided selections.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    public Collection<ClassGenerationAction> createConfiguration() {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        File outputDir = getOutputDir();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // no destination folder
end_comment

begin_comment
comment|//        if (outputDir == null) {
end_comment

begin_comment
comment|//            JOptionPane.showMessageDialog(this.getView(), "Select directory for source files.");
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
comment|//        // no such folder
end_comment

begin_comment
comment|//        if (!outputDir.exists()&& !outputDir.mkdirs()) {
end_comment

begin_comment
comment|//            JOptionPane.showMessageDialog(this.getView(), "Can't create directory " + outputDir
end_comment

begin_comment
comment|//                    + ". Select a different one.");
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
comment|//        // not a directory
end_comment

begin_comment
comment|//        if (!outputDir.isDirectory()) {
end_comment

begin_comment
comment|//            JOptionPane.showMessageDialog(this.getView(), outputDir + " is not a valid directory.");
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
comment|//        // remove generic entities...
end_comment

begin_comment
comment|//        Collection<ObjEntity> selectedEntities = new ArrayList<>(getParentController().getSelectedEntities());
end_comment

begin_comment
comment|//        selectedEntities.removeIf(ObjEntity::isGeneric);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        Collection<ClassGenerationAction> generators = new ArrayList<>();
end_comment

begin_comment
comment|//        for (DataMap map : getParentController().getDataMaps()) {
end_comment

begin_comment
comment|//            try {
end_comment

begin_comment
comment|//                ClassGenerationAction generator = newGenerator();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                if(getParentController().getSelectedDataMaps().contains(map)) {
end_comment

begin_comment
comment|//                    mode = ArtifactsGenerationMode.ALL.getLabel();
end_comment

begin_comment
comment|//                } else {
end_comment

begin_comment
comment|//                    mode = ArtifactsGenerationMode.ENTITY.getLabel();
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                generator.setArtifactsGenerationMode(mode);
end_comment

begin_comment
comment|//                generator.setDataMap(map);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                LinkedList<ObjEntity> objEntities = new LinkedList<>(map.getObjEntities());
end_comment

begin_comment
comment|//                objEntities.retainAll(selectedEntities);
end_comment

begin_comment
comment|//                generator.addEntities(objEntities);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                LinkedList<Embeddable> embeddables = new LinkedList<>(map.getEmbeddables());
end_comment

begin_comment
comment|//                embeddables.retainAll(getParentController().getSelectedEmbeddables());
end_comment

begin_comment
comment|//                generator.addEmbeddables(embeddables);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                generator.addQueries(map.getQueryDescriptors());
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                Preferences preferences = application.getPreferencesNode(GeneralPreferences.class, "");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                if (preferences != null) {
end_comment

begin_comment
comment|//                    generator.setEncoding(preferences.get(GeneralPreferences.ENCODING_PREFERENCE, null));
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                generator.setMakePairs(true);
end_comment

begin_comment
comment|//                generator.setForce(true);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                generators.add(generator);
end_comment

begin_comment
comment|//            } catch (CayenneRuntimeException exception) {
end_comment

begin_comment
comment|//                JOptionPane.showMessageDialog(this.getView(), exception.getUnlabeledMessage());
end_comment

begin_comment
comment|//                return null;
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
comment|//        return generators;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void validateEmbeddable(ValidationResult validationBuffer, Embeddable embeddable) {
end_comment

begin_comment
comment|//        ValidationFailure embeddableFailure = validateEmbeddable(embeddable);
end_comment

begin_comment
comment|//        if (embeddableFailure != null) {
end_comment

begin_comment
comment|//            validationBuffer.addFailure(embeddableFailure);
end_comment

begin_comment
comment|//            return;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for (EmbeddableAttribute attribute : embeddable.getAttributes()) {
end_comment

begin_comment
comment|//            ValidationFailure failure = validateEmbeddableAttribute(attribute);
end_comment

begin_comment
comment|//            if (failure != null) {
end_comment

begin_comment
comment|//                validationBuffer.addFailure(failure);
end_comment

begin_comment
comment|//                return;
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
comment|//    private ValidationFailure validateEmbeddableAttribute(EmbeddableAttribute attribute) {
end_comment

begin_comment
comment|//        String name = attribute.getEmbeddable().getClassName();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyName = BeanValidationFailure.validateNotEmpty(name, "attribute.name",
end_comment

begin_comment
comment|//                attribute.getName());
end_comment

begin_comment
comment|//        if (emptyName != null) {
end_comment

begin_comment
comment|//            return emptyName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badName = CodeValidationUtil.validateJavaIdentifier(name, "attribute.name",
end_comment

begin_comment
comment|//                attribute.getName());
end_comment

begin_comment
comment|//        if (badName != null) {
end_comment

begin_comment
comment|//            return badName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyType = BeanValidationFailure.validateNotEmpty(name, "attribute.type",
end_comment

begin_comment
comment|//                attribute.getType());
end_comment

begin_comment
comment|//        if (emptyType != null) {
end_comment

begin_comment
comment|//            return emptyType;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badType = BeanValidationFailure.validateJavaClassName(name, "attribute.type",
end_comment

begin_comment
comment|//                attribute.getType());
end_comment

begin_comment
comment|//        if (badType != null) {
end_comment

begin_comment
comment|//            return badType;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return null;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private ValidationFailure validateEmbeddable(Embeddable embeddable) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        String name = embeddable.getClassName();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyClass = BeanValidationFailure.validateNotEmpty(name, "className",
end_comment

begin_comment
comment|//                embeddable.getClassName());
end_comment

begin_comment
comment|//        if (emptyClass != null) {
end_comment

begin_comment
comment|//            return emptyClass;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badClass = BeanValidationFailure.validateJavaClassName(name, "className",
end_comment

begin_comment
comment|//                embeddable.getClassName());
end_comment

begin_comment
comment|//        if (badClass != null) {
end_comment

begin_comment
comment|//            return badClass;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return null;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void validateEntity(ValidationResult validationBuffer, ObjEntity entity, boolean clientValidation) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure entityFailure = validateEntity(clientValidation ? entity.getClientEntity() : entity);
end_comment

begin_comment
comment|//        if (entityFailure != null) {
end_comment

begin_comment
comment|//            validationBuffer.addFailure(entityFailure);
end_comment

begin_comment
comment|//            return;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for (ObjAttribute attribute : entity.getAttributes()) {
end_comment

begin_comment
comment|//            if (attribute instanceof EmbeddedAttribute) {
end_comment

begin_comment
comment|//                EmbeddedAttribute embeddedAttribute = (EmbeddedAttribute) attribute;
end_comment

begin_comment
comment|//                for (ObjAttribute subAttribute : embeddedAttribute.getAttributes()) {
end_comment

begin_comment
comment|//                    ValidationFailure failure = validateEmbeddedAttribute(subAttribute);
end_comment

begin_comment
comment|//                    if (failure != null) {
end_comment

begin_comment
comment|//                        validationBuffer.addFailure(failure);
end_comment

begin_comment
comment|//                        return;
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
comment|//
end_comment

begin_comment
comment|//                ValidationFailure failure = validateAttribute(attribute);
end_comment

begin_comment
comment|//                if (failure != null) {
end_comment

begin_comment
comment|//                    validationBuffer.addFailure(failure);
end_comment

begin_comment
comment|//                    return;
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
comment|//
end_comment

begin_comment
comment|//        for (ObjRelationship rel : entity.getRelationships()) {
end_comment

begin_comment
comment|//            ValidationFailure failure = validateRelationship(rel, clientValidation);
end_comment

begin_comment
comment|//            if (failure != null) {
end_comment

begin_comment
comment|//                validationBuffer.addFailure(failure);
end_comment

begin_comment
comment|//                return;
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
comment|//    private ValidationFailure validateEntity(ObjEntity entity) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        String name = entity.getName();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (entity.isGeneric()) {
end_comment

begin_comment
comment|//            return new SimpleValidationFailure(name, "Generic class");
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyClass = BeanValidationFailure.validateNotEmpty(name, "className", entity.getClassName());
end_comment

begin_comment
comment|//        if (emptyClass != null) {
end_comment

begin_comment
comment|//            return emptyClass;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badClass = BeanValidationFailure.validateJavaClassName(name, "className",
end_comment

begin_comment
comment|//                entity.getClassName());
end_comment

begin_comment
comment|//        if (badClass != null) {
end_comment

begin_comment
comment|//            return badClass;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (entity.getSuperClassName() != null) {
end_comment

begin_comment
comment|//            ValidationFailure badSuperClass = BeanValidationFailure.validateJavaClassName(name, "superClassName",
end_comment

begin_comment
comment|//                    entity.getSuperClassName());
end_comment

begin_comment
comment|//            if (badSuperClass != null) {
end_comment

begin_comment
comment|//                return badSuperClass;
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
comment|//        return null;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private ValidationFailure validateAttribute(ObjAttribute attribute) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        String name = attribute.getEntity().getName();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyName = BeanValidationFailure.validateNotEmpty(name, "attribute.name",
end_comment

begin_comment
comment|//                attribute.getName());
end_comment

begin_comment
comment|//        if (emptyName != null) {
end_comment

begin_comment
comment|//            return emptyName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badName = CodeValidationUtil.validateJavaIdentifier(name, "attribute.name",
end_comment

begin_comment
comment|//                attribute.getName());
end_comment

begin_comment
comment|//        if (badName != null) {
end_comment

begin_comment
comment|//            return badName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyType = BeanValidationFailure.validateNotEmpty(name, "attribute.type",
end_comment

begin_comment
comment|//                attribute.getType());
end_comment

begin_comment
comment|//        if (emptyType != null) {
end_comment

begin_comment
comment|//            return emptyType;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badType = BeanValidationFailure.validateJavaClassName(name, "attribute.type",
end_comment

begin_comment
comment|//                attribute.getType());
end_comment

begin_comment
comment|//        if (badType != null) {
end_comment

begin_comment
comment|//            return badType;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return null;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private ValidationFailure validateEmbeddedAttribute(ObjAttribute attribute) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        String name = attribute.getEntity().getName();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // validate embeddedAttribute and attribute names
end_comment

begin_comment
comment|//        // embeddedAttribute returned attibute as
end_comment

begin_comment
comment|//        // [name_embeddedAttribute].[name_attribute]
end_comment

begin_comment
comment|//        String[] attributes = attribute.getName().split("\\.");
end_comment

begin_comment
comment|//        String nameEmbeddedAttribute = attributes[0];
end_comment

begin_comment
comment|//        int beginIndex = attributes[0].length();
end_comment

begin_comment
comment|//        String attr = attribute.getName().substring(beginIndex + 1);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyEmbeddedName = BeanValidationFailure.validateNotEmpty(name, "attribute.name",
end_comment

begin_comment
comment|//                nameEmbeddedAttribute);
end_comment

begin_comment
comment|//        if (emptyEmbeddedName != null) {
end_comment

begin_comment
comment|//            return emptyEmbeddedName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badEmbeddedName = CodeValidationUtil.validateJavaIdentifier(name, "attribute.name",
end_comment

begin_comment
comment|//                nameEmbeddedAttribute);
end_comment

begin_comment
comment|//        if (badEmbeddedName != null) {
end_comment

begin_comment
comment|//            return badEmbeddedName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyName = BeanValidationFailure.validateNotEmpty(name, "attribute.name", attr);
end_comment

begin_comment
comment|//        if (emptyName != null) {
end_comment

begin_comment
comment|//            return emptyName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badName = CodeValidationUtil.validateJavaIdentifier(name, "attribute.name", attr);
end_comment

begin_comment
comment|//        if (badName != null) {
end_comment

begin_comment
comment|//            return badName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyType = BeanValidationFailure.validateNotEmpty(name, "attribute.type",
end_comment

begin_comment
comment|//                attribute.getType());
end_comment

begin_comment
comment|//        if (emptyType != null) {
end_comment

begin_comment
comment|//            return emptyType;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badType = BeanValidationFailure.validateJavaClassName(name, "attribute.type",
end_comment

begin_comment
comment|//                attribute.getType());
end_comment

begin_comment
comment|//        if (badType != null) {
end_comment

begin_comment
comment|//            return badType;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return null;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private ValidationFailure validateRelationship(ObjRelationship relationship, boolean clientValidation) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        String name = relationship.getSourceEntity().getName();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure emptyName = BeanValidationFailure.validateNotEmpty(name, "relationship.name",
end_comment

begin_comment
comment|//                relationship.getName());
end_comment

begin_comment
comment|//        if (emptyName != null) {
end_comment

begin_comment
comment|//            return emptyName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        ValidationFailure badName = CodeValidationUtil.validateJavaIdentifier(name, "relationship.name",
end_comment

begin_comment
comment|//                relationship.getName());
end_comment

begin_comment
comment|//        if (badName != null) {
end_comment

begin_comment
comment|//            return badName;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (!relationship.isToMany()) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            ObjEntity targetEntity = relationship.getTargetEntity();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            if (clientValidation&& targetEntity != null) {
end_comment

begin_comment
comment|//                targetEntity = targetEntity.getClientEntity();
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            if (targetEntity == null) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                return new BeanValidationFailure(name, "relationship.targetEntity", "No target entity");
end_comment

begin_comment
comment|//            } else if (!targetEntity.isGeneric()) {
end_comment

begin_comment
comment|//                ValidationFailure emptyClass = BeanValidationFailure.validateNotEmpty(name,
end_comment

begin_comment
comment|//                        "relationship.targetEntity.className", targetEntity.getClassName());
end_comment

begin_comment
comment|//                if (emptyClass != null) {
end_comment

begin_comment
comment|//                    return emptyClass;
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                ValidationFailure badClass = BeanValidationFailure.validateJavaClassName(name,
end_comment

begin_comment
comment|//                        "relationship.targetEntity.className", targetEntity.getClassName());
end_comment

begin_comment
comment|//                if (badClass != null) {
end_comment

begin_comment
comment|//                    return badClass;
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
comment|//
end_comment

begin_comment
comment|//        return null;
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
comment|//     * Returns a predicate for default entity selection in a given mode.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    public Predicate getDefaultClassFilter() {
end_comment

begin_comment
comment|//        return object -> {
end_comment

begin_comment
comment|//            if (object instanceof ObjEntity) {
end_comment

begin_comment
comment|//                return getParentController().getProblem(((ObjEntity) object).getName()) == null;
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            if (object instanceof Embeddable) {
end_comment

begin_comment
comment|//                return getParentController().getProblem(((Embeddable) object).getClassName()) == null;
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            return false;
end_comment

begin_comment
comment|//        };
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private File getOutputDir() {
end_comment

begin_comment
comment|//        String dir = ((GeneratorControllerPanel) getView()).getOutputFolder().getText();
end_comment

begin_comment
comment|//        return dir != null ? new File(dir) : new File(System.getProperty("user.dir"));
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
comment|//     * An action method that pops up a file chooser dialog to pick the
end_comment

begin_comment
comment|//     * generation directory.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    public void selectOutputFolderAction() {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JTextField outputFolder = ((GeneratorControllerPanel) getView()).getOutputFolder();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        String currentDir = outputFolder.getText();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JFileChooser chooser = new JFileChooser();
end_comment

begin_comment
comment|//        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
end_comment

begin_comment
comment|//        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // guess start directory
end_comment

begin_comment
comment|//        if (!Util.isEmptyString(currentDir)) {
end_comment

begin_comment
comment|//            chooser.setCurrentDirectory(new File(currentDir));
end_comment

begin_comment
comment|//        } else {
end_comment

begin_comment
comment|//            FSPath lastDir = Application.getInstance().getFrameController().getLastDirectory();
end_comment

begin_comment
comment|//            lastDir.updateChooser(chooser);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        int result = chooser.showOpenDialog(getView());
end_comment

begin_comment
comment|//        if (result == JFileChooser.APPROVE_OPTION) {
end_comment

begin_comment
comment|//            File selected = chooser.getSelectedFile();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            // update model
end_comment

begin_comment
comment|//            String path = selected.getAbsolutePath();
end_comment

begin_comment
comment|//            outputFolder.setText(path);
end_comment

begin_comment
comment|//            setOutputPath(path);
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
comment|//    private void initOutputFolder() {
end_comment

begin_comment
comment|//        String path;
end_comment

begin_comment
comment|//        if (getOutputPath() == null) {
end_comment

begin_comment
comment|//            if (System.getProperty("cayenne.cgen.destdir") != null) {
end_comment

begin_comment
comment|//                setOutputPath(System.getProperty("cayenne.cgen.destdir"));
end_comment

begin_comment
comment|//            } else {
end_comment

begin_comment
comment|//                // init default directory..
end_comment

begin_comment
comment|//                FSPath lastPath = Application.getInstance().getFrameController().getLastDirectory();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                path = checkDefaultMavenResourceDir(lastPath, "test");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//                if (path != null || (path = checkDefaultMavenResourceDir(lastPath, "main")) != null) {
end_comment

begin_comment
comment|//                    setOutputPath(path);
end_comment

begin_comment
comment|//                } else {
end_comment

begin_comment
comment|//                    File lastDir = (lastPath != null) ? lastPath.getExistingDirectory(false) : null;
end_comment

begin_comment
comment|//                    setOutputPath(lastDir != null ? lastDir.getAbsolutePath() : null);
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
comment|//    private String checkDefaultMavenResourceDir(FSPath lastPath, String dirType) {
end_comment

begin_comment
comment|//        String path = lastPath.getPath();
end_comment

begin_comment
comment|//        String resourcePath = buildFilePath("src", dirType, "resources");
end_comment

begin_comment
comment|//        int idx = path.indexOf(resourcePath);
end_comment

begin_comment
comment|//        if (idx< 0) {
end_comment

begin_comment
comment|//            return null;
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        return path.substring(0, idx) + buildFilePath("src", dirType, "java");
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private static String buildFilePath(String... pathElements) {
end_comment

begin_comment
comment|//        if (pathElements.length == 0) {
end_comment

begin_comment
comment|//            return "";
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        StringBuilder path = new StringBuilder(pathElements[0]);
end_comment

begin_comment
comment|//        for (int i = 1; i< pathElements.length; i++) {
end_comment

begin_comment
comment|//            path.append(File.separator).append(pathElements[i]);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        return path.toString();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

