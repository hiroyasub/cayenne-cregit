begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|editor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BorderLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|CardLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Container
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JSplitPane
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|ProjectController
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|ProjectTreeView
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|editor
operator|.
name|datanode
operator|.
name|DataNodeEditor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|editor
operator|.
name|dbentity
operator|.
name|DbEntityTabbedView
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|DataMapDisplayEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|DataMapDisplayListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|DataNodeDisplayEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|DataNodeDisplayListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|DbEntityDisplayListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|DomainDisplayEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|DomainDisplayListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|EntityDisplayEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|ObjEntityDisplayListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|ProcedureDisplayEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|ProcedureDisplayListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|QueryDisplayEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|event
operator|.
name|QueryDisplayListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|pref
operator|.
name|ComponentGeometry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|pref
operator|.
name|Domain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|ProcedureQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SQLTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * Main display area split into the project navigation tree on the left and selected  * object editor on the right.  */
end_comment

begin_class
specifier|public
class|class
name|EditorView
extends|extends
name|JPanel
implements|implements
name|ObjEntityDisplayListener
implements|,
name|DbEntityDisplayListener
implements|,
name|DomainDisplayListener
implements|,
name|DataMapDisplayListener
implements|,
name|DataNodeDisplayListener
implements|,
name|ProcedureDisplayListener
implements|,
name|QueryDisplayListener
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_VIEW
init|=
literal|"Empty"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DOMAIN_VIEW
init|=
literal|"Domain"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NODE_VIEW
init|=
literal|"Node"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DATA_MAP_VIEW
init|=
literal|"DataMap"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OBJ_VIEW
init|=
literal|"ObjView"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DB_VIEW
init|=
literal|"DbView"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROCEDURE_VIEW
init|=
literal|"ProcedureView"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SELECT_QUERY_VIEW
init|=
literal|"SelectQueryView"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SQL_TEMPLATE_VIEW
init|=
literal|"SQLTemplateView"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROCEDURE_QUERY_VIEW
init|=
literal|"ProcedureQueryView"
decl_stmt|;
specifier|protected
name|ProjectController
name|eventController
decl_stmt|;
specifier|protected
name|JSplitPane
name|splitPane
decl_stmt|;
specifier|protected
name|Container
name|detailPanel
decl_stmt|;
specifier|protected
name|CardLayout
name|detailLayout
decl_stmt|;
specifier|public
name|EditorView
parameter_list|(
name|ProjectController
name|eventController
parameter_list|)
block|{
name|this
operator|.
name|eventController
operator|=
name|eventController
expr_stmt|;
name|initView
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initView
parameter_list|()
block|{
comment|// init widgets
name|ProjectTreeView
name|treePanel
init|=
operator|new
name|ProjectTreeView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|treePanel
operator|.
name|setMinimumSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|50
argument_list|,
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|detailPanel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|this
operator|.
name|splitPane
operator|=
operator|new
name|JSplitPane
argument_list|(
name|JSplitPane
operator|.
name|HORIZONTAL_SPLIT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// assemble...
name|this
operator|.
name|detailLayout
operator|=
operator|new
name|CardLayout
argument_list|()
expr_stmt|;
name|detailPanel
operator|.
name|setLayout
argument_list|(
name|detailLayout
argument_list|)
expr_stmt|;
comment|// some but not all panels must be wrapped in a scroll pane
comment|// those that are not wrapped usually have there own scrollers
comment|// in subpanels...
name|detailPanel
operator|.
name|add
argument_list|(
operator|new
name|JPanel
argument_list|()
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
name|Component
name|domainView
init|=
operator|new
name|DataDomainView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|domainView
argument_list|)
argument_list|,
name|DOMAIN_VIEW
argument_list|)
expr_stmt|;
name|DataNodeEditor
name|nodeController
init|=
operator|new
name|DataNodeEditor
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|nodeController
operator|.
name|getView
argument_list|()
argument_list|)
argument_list|,
name|NODE_VIEW
argument_list|)
expr_stmt|;
name|Component
name|dataMapView
init|=
operator|new
name|DataMapView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|dataMapView
argument_list|)
argument_list|,
name|DATA_MAP_VIEW
argument_list|)
expr_stmt|;
name|Component
name|procedureView
init|=
operator|new
name|ProcedureTabbedView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
name|procedureView
argument_list|,
name|PROCEDURE_VIEW
argument_list|)
expr_stmt|;
name|Component
name|selectQueryView
init|=
operator|new
name|SelectQueryTabbedView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
name|selectQueryView
argument_list|,
name|SELECT_QUERY_VIEW
argument_list|)
expr_stmt|;
name|Component
name|sqlTemplateView
init|=
operator|new
name|SQLTemplateTabbedView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
name|sqlTemplateView
argument_list|,
name|SQL_TEMPLATE_VIEW
argument_list|)
expr_stmt|;
name|Component
name|procedureQueryView
init|=
operator|new
name|ProcedureQueryView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|procedureQueryView
argument_list|)
argument_list|,
name|PROCEDURE_QUERY_VIEW
argument_list|)
expr_stmt|;
name|Component
name|objDetailView
init|=
operator|new
name|ObjEntityTabbedView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
name|objDetailView
argument_list|,
name|OBJ_VIEW
argument_list|)
expr_stmt|;
name|Component
name|dbDetailView
init|=
operator|new
name|DbEntityTabbedView
argument_list|(
name|eventController
argument_list|)
decl_stmt|;
name|detailPanel
operator|.
name|add
argument_list|(
name|dbDetailView
argument_list|,
name|DB_VIEW
argument_list|)
expr_stmt|;
name|splitPane
operator|.
name|setLeftComponent
argument_list|(
operator|new
name|JScrollPane
argument_list|(
name|treePanel
argument_list|)
argument_list|)
expr_stmt|;
name|splitPane
operator|.
name|setRightComponent
argument_list|(
name|detailPanel
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|splitPane
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initController
parameter_list|()
block|{
name|eventController
operator|.
name|addDomainDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addDataNodeDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addDataMapDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addObjEntityDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addDbEntityDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addProcedureDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|eventController
operator|.
name|addQueryDisplayListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|Domain
name|domain
init|=
name|eventController
operator|.
name|getApplicationPreferenceDomain
argument_list|()
operator|.
name|getSubdomain
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|ComponentGeometry
name|geometry
init|=
operator|(
name|ComponentGeometry
operator|)
name|domain
operator|.
name|getDetail
argument_list|(
literal|"splitPane.divider"
argument_list|,
name|ComponentGeometry
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|geometry
operator|.
name|bindIntProperty
argument_list|(
name|splitPane
argument_list|,
name|JSplitPane
operator|.
name|DIVIDER_LOCATION_PROPERTY
argument_list|,
literal|150
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentProcedureChanged
parameter_list|(
name|ProcedureDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getProcedure
argument_list|()
operator|==
literal|null
condition|)
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
else|else
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|PROCEDURE_VIEW
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentDomainChanged
parameter_list|(
name|DomainDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getDomain
argument_list|()
operator|==
literal|null
condition|)
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
else|else
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|DOMAIN_VIEW
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentDataNodeChanged
parameter_list|(
name|DataNodeDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getDataNode
argument_list|()
operator|==
literal|null
condition|)
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
else|else
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|NODE_VIEW
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentDataMapChanged
parameter_list|(
name|DataMapDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getDataMap
argument_list|()
operator|==
literal|null
condition|)
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
else|else
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|DATA_MAP_VIEW
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentObjEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getEntity
argument_list|()
operator|==
literal|null
condition|)
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
else|else
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|OBJ_VIEW
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentDbEntityChanged
parameter_list|(
name|EntityDisplayEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getEntity
argument_list|()
operator|==
literal|null
condition|)
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
else|else
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|DB_VIEW
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|currentQueryChanged
parameter_list|(
name|QueryDisplayEvent
name|e
parameter_list|)
block|{
name|Query
name|query
init|=
name|e
operator|.
name|getQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|instanceof
name|SelectQuery
condition|)
block|{
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|SELECT_QUERY_VIEW
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|SQLTemplate
condition|)
block|{
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|SQL_TEMPLATE_VIEW
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|ProcedureQuery
condition|)
block|{
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|PROCEDURE_QUERY_VIEW
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|detailLayout
operator|.
name|show
argument_list|(
name|detailPanel
argument_list|,
name|EMPTY_VIEW
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

