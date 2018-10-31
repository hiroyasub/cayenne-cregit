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
comment|//
end_comment

begin_comment
comment|//import javax.swing.BoxLayout;
end_comment

begin_comment
comment|//import javax.swing.JCheckBox;
end_comment

begin_comment
comment|//import javax.swing.JLabel;
end_comment

begin_comment
comment|//import javax.swing.JPanel;
end_comment

begin_comment
comment|//import javax.swing.JScrollPane;
end_comment

begin_comment
comment|//import javax.swing.JTable;
end_comment

begin_comment
comment|//import javax.swing.ScrollPaneConstants;
end_comment

begin_comment
comment|//import javax.swing.UIManager;
end_comment

begin_comment
comment|//import javax.swing.border.EmptyBorder;
end_comment

begin_comment
comment|//import java.awt.BorderLayout;
end_comment

begin_comment
comment|//import java.awt.Component;
end_comment

begin_comment
comment|//import java.awt.Dimension;
end_comment

begin_comment
comment|//import java.awt.FlowLayout;
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
comment|//public class ClassesTabPanel extends JPanel {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected JCheckBox checkAll;
end_comment

begin_comment
comment|//    protected JLabel checkAllLabel;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private Map<DataMap, JTable> dataMapTables;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private Map<DataMap, JCheckBox> dataMapJCheckBoxMap;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public ClassesTabPanel(Collection<DataMap> dataMaps) {
end_comment

begin_comment
comment|//        dataMapTables = new HashMap<>();
end_comment

begin_comment
comment|//        dataMapJCheckBoxMap = new HashMap<>();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // TODO: andrus 04/07/2006 - is there an easy way to stick that checkbox in the
end_comment

begin_comment
comment|//        // table header????
end_comment

begin_comment
comment|//        this.checkAll = new JCheckBox();
end_comment

begin_comment
comment|//        this.checkAllLabel = new JLabel("Check All Classes");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        checkAll.addItemListener(event -> {
end_comment

begin_comment
comment|//            if (checkAll.isSelected()) {
end_comment

begin_comment
comment|//                checkAllLabel.setText("Uncheck All Classess");
end_comment

begin_comment
comment|//                dataMapJCheckBoxMap.keySet().forEach(val -> dataMapJCheckBoxMap.get(val).setSelected(true));
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//            else {
end_comment

begin_comment
comment|//                checkAllLabel.setText("Check All Classes");
end_comment

begin_comment
comment|//                dataMapJCheckBoxMap.keySet().forEach(val -> dataMapJCheckBoxMap.get(val).setSelected(false));
end_comment

begin_comment
comment|//            }
end_comment

begin_comment
comment|//        });
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // assemble
end_comment

begin_comment
comment|//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
end_comment

begin_comment
comment|//        topPanel.setBorder(UIManager.getBorder("ToolBar.border"));
end_comment

begin_comment
comment|//        topPanel.add(checkAll);
end_comment

begin_comment
comment|//        topPanel.add(checkAllLabel);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JPanel panel = new JPanel();
end_comment

begin_comment
comment|//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
end_comment

begin_comment
comment|//        for(DataMap dataMap : dataMaps) {
end_comment

begin_comment
comment|//            JTable table = new JTable();
end_comment

begin_comment
comment|//            table.setRowHeight(22);
end_comment

begin_comment
comment|//            dataMapTables.put(dataMap, table);
end_comment

begin_comment
comment|//            JPanel scrollTable = new JPanel(new BorderLayout());
end_comment

begin_comment
comment|//            scrollTable.add(dataMapTables.get(dataMap).getTableHeader(), BorderLayout.NORTH);
end_comment

begin_comment
comment|//            scrollTable.add(dataMapTables.get(dataMap), BorderLayout.CENTER);
end_comment

begin_comment
comment|//            scrollTable.setPreferredSize(new Dimension(dataMapTables.get(dataMap).getPreferredSize().width,
end_comment

begin_comment
comment|//                    (dataMap.getEmbeddables().size() + dataMap.getObjEntities().size()) * dataMapTables.get(dataMap).getRowHeight() + 45));
end_comment

begin_comment
comment|//            JPanel labelPanel = new JPanel(new BorderLayout());
end_comment

begin_comment
comment|//            labelPanel.setPreferredSize(new Dimension(dataMapTables.get(dataMap).getPreferredSize().width, 20));
end_comment

begin_comment
comment|//            JLabel dataMapLabel = new JLabel(dataMap.getName());
end_comment

begin_comment
comment|//            dataMapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
end_comment

begin_comment
comment|//            dataMapLabel.setBorder(new EmptyBorder(8, 8, 8, 0));
end_comment

begin_comment
comment|//            labelPanel.add(dataMapLabel, BorderLayout.CENTER);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            JCheckBox dataMapCheckBox = new JCheckBox();
end_comment

begin_comment
comment|//            dataMapJCheckBoxMap.put(dataMap, dataMapCheckBox);
end_comment

begin_comment
comment|//            labelPanel.add(dataMapCheckBox, BorderLayout.WEST);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            JPanel currPanel = new JPanel(new BorderLayout());
end_comment

begin_comment
comment|//            currPanel.add(labelPanel, BorderLayout.NORTH);
end_comment

begin_comment
comment|//            currPanel.add(scrollTable, BorderLayout.CENTER);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//            panel.add(currPanel);
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JScrollPane tablePanel = new JScrollPane(
end_comment

begin_comment
comment|//                panel,
end_comment

begin_comment
comment|//                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
end_comment

begin_comment
comment|//                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // set some minimal preferred size, so that it is smaller than other forms used in
end_comment

begin_comment
comment|//        // the dialog... this way we get the right automated overall size
end_comment

begin_comment
comment|//        tablePanel.setPreferredSize(new Dimension(450, 400));
end_comment

begin_comment
comment|//        setLayout(new BorderLayout());
end_comment

begin_comment
comment|//        add(topPanel, BorderLayout.NORTH);
end_comment

begin_comment
comment|//        add(tablePanel, BorderLayout.CENTER);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public boolean isAllCheckBoxesFromDataMapSelected(DataMap dataMap) {
end_comment

begin_comment
comment|//        JTable table = dataMapTables.get(dataMap);
end_comment

begin_comment
comment|//        for(int i = 0; i< table.getRowCount(); i++) {
end_comment

begin_comment
comment|//            if(!(Boolean)table.getModel().getValueAt(i, 0)) {
end_comment

begin_comment
comment|//                return false;
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
comment|//
end_comment

begin_comment
comment|//    public Map<DataMap, JTable> getDataMapTables() {
end_comment

begin_comment
comment|//        return dataMapTables;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Map<DataMap, JCheckBox> getDataMapJCheckBoxMap() {
end_comment

begin_comment
comment|//        return dataMapJCheckBoxMap;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JCheckBox getCheckAll() {
end_comment

begin_comment
comment|//        return checkAll;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

