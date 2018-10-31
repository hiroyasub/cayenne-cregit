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
comment|//import org.apache.cayenne.modeler.util.CayenneController;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.BindingBuilder;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.ImageRendererColumn;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.ObjectBinding;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.TableBindingBuilder;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import javax.swing.JCheckBox;
end_comment

begin_comment
comment|//import javax.swing.JLabel;
end_comment

begin_comment
comment|//import javax.swing.JTable;
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
comment|//import java.util.HashMap;
end_comment

begin_comment
comment|//import java.util.Map;
end_comment

begin_comment
comment|//import java.util.List;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//public class ClassesTabController extends CayenneController {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public static final String GENERATE_PROPERTY = "generate";
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected ClassesTabPanel view;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private Map<DataMap, ObjectBinding> objectBindings;
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
comment|//    protected Map<DataMap, List<Object>> objectList;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private List<Object> currentCollection;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public ClassesTabController(CodeGeneratorControllerBase parent, Collection<DataMap> dataMaps) {
end_comment

begin_comment
comment|//        super(parent);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        currentCollection = new ArrayList<>();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.objectList = new HashMap<>();
end_comment

begin_comment
comment|//        for(DataMap dataMap : dataMaps) {
end_comment

begin_comment
comment|//            List<Object> list = new ArrayList<>();
end_comment

begin_comment
comment|//            list.add(dataMap);
end_comment

begin_comment
comment|//            list.addAll(dataMap.getObjEntities());
end_comment

begin_comment
comment|//            list.addAll(dataMap.getEmbeddables());
end_comment

begin_comment
comment|//            objectList.put(dataMap, list);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.objectBindings = new HashMap<>();
end_comment

begin_comment
comment|//        this.dataMaps = dataMaps;
end_comment

begin_comment
comment|//        this.view = new ClassesTabPanel(dataMaps);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        initBindings();
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
comment|//    protected void initBindings() {
end_comment

begin_comment
comment|//
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
comment|//        builder.bindToAction(view.getCheckAll(), "checkAllAction()");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        TableBindingBuilder tableBuilder = new TableBindingBuilder(builder);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        tableBuilder.addColumn(
end_comment

begin_comment
comment|//                "",
end_comment

begin_comment
comment|//                "parent.setCurrentClass(#item), selected",
end_comment

begin_comment
comment|//                Boolean.class,
end_comment

begin_comment
comment|//                true,
end_comment

begin_comment
comment|//                Boolean.TRUE);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        tableBuilder.addColumn(
end_comment

begin_comment
comment|//                "Class",
end_comment

begin_comment
comment|//                "parent.getItemName(#item)",
end_comment

begin_comment
comment|//                JLabel.class,
end_comment

begin_comment
comment|//                false,
end_comment

begin_comment
comment|//                "XXXXXXXXXXXXXX");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        tableBuilder.addColumn(
end_comment

begin_comment
comment|//                "Comments, Warnings",
end_comment

begin_comment
comment|//                "parent.getProblem(#item)",
end_comment

begin_comment
comment|//                String.class,
end_comment

begin_comment
comment|//                false,
end_comment

begin_comment
comment|//                "XXXXXXXXXXXXXXXXXXXXXXXXXXX");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for(DataMap dataMap : dataMaps) {
end_comment

begin_comment
comment|//            JTable table = view.getDataMapTables().get(dataMap);
end_comment

begin_comment
comment|//            if(table != null) {
end_comment

begin_comment
comment|//                currentCollection = objectList.get(dataMap);
end_comment

begin_comment
comment|//                objectBindings.put(dataMap, tableBuilder.bindToTable(table, "currentCollection"));
end_comment

begin_comment
comment|//                table.getColumnModel().getColumn(1).setCellRenderer(new ImageRendererColumn());
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            JCheckBox checkBox = view.getDataMapJCheckBoxMap().get(dataMap);
end_comment

begin_comment
comment|//            if(checkBox != null) {
end_comment

begin_comment
comment|//                checkBox.addActionListener(val -> checkDataMap(dataMap, ((JCheckBox)val.getSource()).isSelected()));
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
comment|//    public List<Object> getCurrentCollection() {
end_comment

begin_comment
comment|//        return currentCollection;
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
comment|//        return getParentController().isSelected();
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setSelected(boolean selected) {
end_comment

begin_comment
comment|//        getParentController().setSelected(selected);
end_comment

begin_comment
comment|//        classSelectedAction();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        for(DataMap dataMap : dataMaps) {
end_comment

begin_comment
comment|//            if(view.isAllCheckBoxesFromDataMapSelected(dataMap)) {
end_comment

begin_comment
comment|//                view.getDataMapJCheckBoxMap().get(dataMap).setSelected(true);
end_comment

begin_comment
comment|//            } else {
end_comment

begin_comment
comment|//                view.getDataMapJCheckBoxMap().get(dataMap).setSelected(false);
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
comment|//    /**
end_comment

begin_comment
comment|//     * A callback action that updates the state of Select All checkbox.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    public void classSelectedAction() {
end_comment

begin_comment
comment|//        int selectedCount = getParentController().getSelectedEntitiesSize()
end_comment

begin_comment
comment|//                + getParentController().getSelectedEmbeddablesSize()
end_comment

begin_comment
comment|//                + getParentController().getSelectedDataMapsSize();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        if (selectedCount == 0) {
end_comment

begin_comment
comment|//            view.getCheckAll().setSelected(false);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//        else if (selectedCount == getParentController().getClasses().size()) {
end_comment

begin_comment
comment|//            view.getCheckAll().setSelected(true);
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
comment|//    /**
end_comment

begin_comment
comment|//     * An action that updates entity check boxes in response to the Select All state
end_comment

begin_comment
comment|//     * change.
end_comment

begin_comment
comment|//     */
end_comment

begin_comment
comment|//    public void checkAllAction() {
end_comment

begin_comment
comment|//        if (getParentController().updateSelection(view.getCheckAll().isSelected() ? o -> true : o -> false)) {
end_comment

begin_comment
comment|//            dataMaps.forEach(dataMap -> {
end_comment

begin_comment
comment|//                ObjectBinding binding = objectBindings.get(dataMap);
end_comment

begin_comment
comment|//                if(binding != null) {
end_comment

begin_comment
comment|//                    currentCollection = objectList.get(dataMap);
end_comment

begin_comment
comment|//                    binding.updateView();
end_comment

begin_comment
comment|//                }
end_comment

begin_comment
comment|//            });
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
comment|//    private void checkDataMap(DataMap dataMap, boolean selected) {
end_comment

begin_comment
comment|//        if (getParentController().updateDataMapSelection(selected ? o -> true : o -> false, dataMap)){
end_comment

begin_comment
comment|//            ObjectBinding binding = objectBindings.get(dataMap);
end_comment

begin_comment
comment|//            if(binding != null) {
end_comment

begin_comment
comment|//                currentCollection = objectList.get(dataMap);
end_comment

begin_comment
comment|//                binding.updateView();
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            if(isAllMapsSelected()) {
end_comment

begin_comment
comment|//                view.getCheckAll().setSelected(true);
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
comment|//    private boolean isAllMapsSelected() {
end_comment

begin_comment
comment|//        for(DataMap dataMap : dataMaps) {
end_comment

begin_comment
comment|//            if(view.getDataMapJCheckBoxMap().get(dataMap) != null) {
end_comment

begin_comment
comment|//                if(!view.getDataMapJCheckBoxMap().get(dataMap).isSelected()) {
end_comment

begin_comment
comment|//                    return false;
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
comment|//        return true;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

