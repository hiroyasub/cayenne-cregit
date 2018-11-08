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
comment|//import org.apache.cayenne.modeler.CodeTemplateManager;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.dialog.pref.PreferenceDialog;
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.pref.DataMapDefaults;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.BindingBuilder;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.ObjectBinding;
end_comment

begin_comment
comment|//import org.apache.cayenne.util.Util;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import javax.swing.DefaultComboBoxModel;
end_comment

begin_comment
comment|//import javax.swing.JCheckBox;
end_comment

begin_comment
comment|//import javax.swing.JComboBox;
end_comment

begin_comment
comment|//import java.awt.Component;
end_comment

begin_comment
comment|//import java.util.List;
end_comment

begin_comment
comment|//import java.util.ArrayList;
end_comment

begin_comment
comment|//import java.util.Collection;
end_comment

begin_comment
comment|//import java.util.Collections;
end_comment

begin_comment
comment|//import java.util.Map.Entry;
end_comment

begin_comment
comment|//import java.util.Objects;
end_comment

begin_comment
comment|//import java.util.Set;
end_comment

begin_comment
comment|//import java.util.TreeMap;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import static org.apache.cayenne.modeler.CodeTemplateManager.SINGLE_SERVER_CLASS;
end_comment

begin_comment
comment|//import static org.apache.cayenne.modeler.CodeTemplateManager.STANDARD_SERVER_SUBCLASS;
end_comment

begin_comment
comment|//import static org.apache.cayenne.modeler.CodeTemplateManager.STANDARD_SERVER_SUPERCLASS;
end_comment

begin_comment
comment|//import static org.apache.cayenne.modeler.dialog.pref.PreferenceDialog.TEMPLATES_KEY;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * A controller for the custom generation mode.
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class CustomModeController extends GeneratorController {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	// correspond to non-public constants on MapClassGenerator.
end_comment

begin_comment
comment|//	private static final String MODE_ENTITY = "entity";
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	protected CustomModePanel view;
end_comment

begin_comment
comment|//	private CodeTemplateManager templateManager;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	private ObjectBinding superTemplate;
end_comment

begin_comment
comment|//	private ObjectBinding subTemplate;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	private CustomPreferencesUpdater preferencesUpdater;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	public CustomPreferencesUpdater getCustomPreferencesUpdater() {
end_comment

begin_comment
comment|//		return preferencesUpdater;
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	public CustomModeController(CodeGeneratorControllerBase parent) {
end_comment

begin_comment
comment|//		super(parent);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		// bind preferences and init defaults...
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		Set<Entry<DataMap, DataMapDefaults>> entities = getMapPreferences().entrySet();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		for (Entry<DataMap, DataMapDefaults> entry : entities) {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (Util.isEmptyString(entry.getValue().getSuperclassTemplate())) {
end_comment

begin_comment
comment|//				entry.getValue().setSuperclassTemplate(STANDARD_SERVER_SUPERCLASS);
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (Util.isEmptyString(entry.getValue().getSubclassTemplate())) {
end_comment

begin_comment
comment|//				entry.getValue().setSubclassTemplate(STANDARD_SERVER_SUBCLASS);
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (Util.isEmptyString(entry.getValue().getProperty("mode"))) {
end_comment

begin_comment
comment|//				entry.getValue().setProperty("mode", MODE_ENTITY);
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (Util.isEmptyString(entry.getValue().getProperty("overwrite"))) {
end_comment

begin_comment
comment|//				entry.getValue().setBooleanProperty("overwrite", false);
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (Util.isEmptyString(entry.getValue().getProperty("pairs"))) {
end_comment

begin_comment
comment|//				entry.getValue().setBooleanProperty("pairs", true);
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (Util.isEmptyString(entry.getValue().getProperty("usePackagePath"))) {
end_comment

begin_comment
comment|//				entry.getValue().setBooleanProperty("usePackagePath", true);
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (Util.isEmptyString(entry.getValue().getProperty("outputPattern"))) {
end_comment

begin_comment
comment|//				entry.getValue().setProperty("outputPattern", "*.java");
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//		}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		BindingBuilder builder = new BindingBuilder(getApplication().getBindingFactory(), this);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		builder.bindToAction(view.getManageTemplatesLink(), "popPreferencesAction()");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		builder.bindToStateChange(view.getOverwrite(), "customPreferencesUpdater.overwrite").updateView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		builder.bindToStateChange(view.getPairs(), "customPreferencesUpdater.pairs").updateView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		builder.bindToStateChange(view.getUsePackagePath(), "customPreferencesUpdater.usePackagePath").updateView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		subTemplate = builder.bindToComboSelection(view.getSubclassTemplate(),
end_comment

begin_comment
comment|//				"customPreferencesUpdater.subclassTemplate");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		superTemplate = builder.bindToComboSelection(view.getSuperclassTemplate(),
end_comment

begin_comment
comment|//				"customPreferencesUpdater.superclassTemplate");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		builder.bindToTextField(view.getOutputPattern(), "customPreferencesUpdater.outputPattern").updateView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		builder.bindToStateChange(view.getCreatePropertyNames(), "customPreferencesUpdater.createPropertyNames")
end_comment

begin_comment
comment|//				.updateView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		builder.bindToStateChange(view.getCreatePKProperties(), "customPreferencesUpdater.createPKProperties")
end_comment

begin_comment
comment|//				.updateView();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		updateTemplates();
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	protected void createDefaults() {
end_comment

begin_comment
comment|//		TreeMap<DataMap, DataMapDefaults> map = new TreeMap<DataMap, DataMapDefaults>();
end_comment

begin_comment
comment|//		Collection<DataMap> dataMaps = getParentController().getDataMaps();
end_comment

begin_comment
comment|//		for (DataMap dataMap : dataMaps) {
end_comment

begin_comment
comment|//			DataMapDefaults preferences;
end_comment

begin_comment
comment|//			preferences = getApplication().getFrameController().getProjectController()
end_comment

begin_comment
comment|//					.getDataMapPreferences(this.getClass().getName().replace(".", "/"), dataMap);
end_comment

begin_comment
comment|//			preferences.setSuperclassPackage("");
end_comment

begin_comment
comment|//			preferences.updateSuperclassPackage(dataMap, false);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			map.put(dataMap, preferences);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (getOutputPath() == null) {
end_comment

begin_comment
comment|//				setOutputPath(preferences.getOutputPath());
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//		}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		setMapPreferences(map);
end_comment

begin_comment
comment|//		preferencesUpdater = new CustomPreferencesUpdater(map);
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	protected GeneratorControllerPanel createView() {
end_comment

begin_comment
comment|//		this.view = new CustomModePanel();
end_comment

begin_comment
comment|//		return view;
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	private void updateTemplates() {
end_comment

begin_comment
comment|//		this.templateManager = getApplication().getCodeTemplateManager();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		List<String> customTemplates = new ArrayList<>(templateManager.getCustomTemplates().keySet());
end_comment

begin_comment
comment|//		Collections.sort(customTemplates);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		List<String> superTemplates = new ArrayList<>(templateManager.getStandardSuperclassTemplates());
end_comment

begin_comment
comment|//		Collections.sort(superTemplates);
end_comment

begin_comment
comment|//		superTemplates.addAll(customTemplates);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		List<String> subTemplates = new ArrayList<>(templateManager.getStandardSubclassTemplates());
end_comment

begin_comment
comment|//		Collections.sort(subTemplates);
end_comment

begin_comment
comment|//		subTemplates.addAll(customTemplates);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		this.view.getSubclassTemplate().setModel(new DefaultComboBoxModel<>(subTemplates.toArray(new String[0])));
end_comment

begin_comment
comment|//		this.view.getSuperclassTemplate().setModel(new DefaultComboBoxModel<>(superTemplates.toArray(new String[0])));
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		JCheckBox pairs = this.view.getPairs();
end_comment

begin_comment
comment|//		updateView();
end_comment

begin_comment
comment|//		pairs.addItemListener(e -> updateView());
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		superTemplate.updateView();
end_comment

begin_comment
comment|//		subTemplate.updateView();
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	private void updateView() {
end_comment

begin_comment
comment|//		boolean selected = view.getPairs().isSelected();
end_comment

begin_comment
comment|//		JComboBox<String> subclassTemplate = view.getSubclassTemplate();
end_comment

begin_comment
comment|//		subclassTemplate.setSelectedItem(selected ? STANDARD_SERVER_SUBCLASS : SINGLE_SERVER_CLASS);
end_comment

begin_comment
comment|//		view.getSuperclassTemplate().setEnabled(selected);
end_comment

begin_comment
comment|//		view.getOverwrite().setEnabled(!selected);
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	public Component getView() {
end_comment

begin_comment
comment|//		return view;
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	public Collection<ClassGenerationAction> createConfiguration() {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		Collection<ClassGenerationAction> generators = super.createConfiguration();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		String superKey = Objects.requireNonNull(view.getSuperclassTemplate().getSelectedItem()).toString();
end_comment

begin_comment
comment|//		String superTemplate = templateManager.getTemplatePath(superKey);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		String subKey = Objects.requireNonNull(view.getSubclassTemplate().getSelectedItem()).toString();
end_comment

begin_comment
comment|//		String subTemplate = templateManager.getTemplatePath(subKey);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		for (ClassGenerationAction generator : generators) {
end_comment

begin_comment
comment|//			generator.setSuperTemplate(superTemplate);
end_comment

begin_comment
comment|//			generator.setTemplate(subTemplate);
end_comment

begin_comment
comment|//			generator.setOverwrite(view.getOverwrite().isSelected());
end_comment

begin_comment
comment|//			generator.setUsePkgPath(view.getUsePackagePath().isSelected());
end_comment

begin_comment
comment|//			generator.setMakePairs(view.getPairs().isSelected());
end_comment

begin_comment
comment|//			generator.setCreatePropertyNames(view.getCreatePropertyNames().isSelected());
end_comment

begin_comment
comment|//			generator.setCreatePKProperties(view.getCreatePKProperties().isSelected());
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//			if (!Util.isEmptyString(view.getOutputPattern().getText())) {
end_comment

begin_comment
comment|//				generator.setOutputPattern(view.getOutputPattern().getText());
end_comment

begin_comment
comment|//			}
end_comment

begin_comment
comment|//		}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//		return generators;
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	public void popPreferencesAction() {
end_comment

begin_comment
comment|//		new PreferenceDialog(getApplication().getFrameController()).startupAction(TEMPLATES_KEY);
end_comment

begin_comment
comment|//		updateTemplates();
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//	@Override
end_comment

begin_comment
comment|//	protected ClassGenerationAction newGenerator() {
end_comment

begin_comment
comment|//		ClassGenerationAction action = new ClassGenerationAction();
end_comment

begin_comment
comment|//		getApplication().getInjector().injectMembers(action);
end_comment

begin_comment
comment|//		return action;
end_comment

begin_comment
comment|//	}
end_comment

begin_comment
comment|//}
end_comment

end_unit

